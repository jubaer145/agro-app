package com.akyljer.core.repository

import com.akyljer.core.db.DiagnosisDao
import com.akyljer.core.db.TaskDao
import com.akyljer.core.db.WeatherDao
import com.akyljer.core.db.toDomain
import com.akyljer.core.db.toEntity
import com.akyljer.core.model.CropTask
import com.akyljer.core.model.Diagnosis
import com.akyljer.core.model.WeatherForecast
import com.akyljer.core.network.WeatherApi
import com.akyljer.core.rules.AdviceResult
import com.akyljer.core.rules.AdvisorRuleEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface TaskRepository {
    val tasks: Flow<List<CropTask>>
    suspend fun saveTasks(tasks: List<CropTask>)
}

interface WeatherRepository {
    val forecast: Flow<WeatherForecast?>
    suspend fun refresh(lat: Double, lon: Double)
}

interface DiagnosisRepository {
    val history: Flow<List<Diagnosis>>
    suspend fun save(diagnosis: Diagnosis)
}

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override val tasks: Flow<List<CropTask>> =
        taskDao.observeTasks().map { list -> list.map { it.toDomain() } }

    override suspend fun saveTasks(tasks: List<CropTask>) {
        taskDao.upsertAll(tasks.map { it.toEntity() })
    }
}

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    private val api: WeatherApi
) : WeatherRepository {
    override val forecast: Flow<WeatherForecast?> =
        weatherDao.observeForecast().map { it.firstOrNull()?.toDomain() }

    override suspend fun refresh(lat: Double, lon: Double) {
        val response = api.forecast(lat, lon)
        val domain = WeatherForecast(
            location = "$lat,$lon",
            dateMillis = System.currentTimeMillis(),
            minTempC = response.minTempC,
            maxTempC = response.maxTempC,
            precipitationMm = response.precipitationMm,
            humidity = response.humidity,
            summary = response.summary
        )
        weatherDao.upsert(domain.toEntity())
    }
}

@Singleton
class DiagnosisRepositoryImpl @Inject constructor(
    private val dao: DiagnosisDao
) : DiagnosisRepository {
    override val history: Flow<List<Diagnosis>> =
        dao.observeHistory().map { list -> list.map { it.toDomain() } }

    override suspend fun save(diagnosis: Diagnosis) {
        dao.insert(diagnosis.toEntity())
    }
}

data class AdvisorResult(
    val advice: AdviceResult,
    val tasks: List<CropTask>
)

interface AdvisorRepository {
    suspend fun generate(input: com.akyljer.core.model.FarmerInput, weather: WeatherForecast?): AdvisorResult
}

@Singleton
class AdvisorRepositoryImpl @Inject constructor(
    private val engine: AdvisorRuleEngine,
    private val taskRepository: TaskRepository
) : AdvisorRepository {
    override suspend fun generate(
        input: com.akyljer.core.model.FarmerInput,
        weather: WeatherForecast?
    ): AdvisorResult {
        val advice = engine.generateAdvice(input, weather)
        taskRepository.saveTasks(advice.tasks)
        return AdvisorResult(advice, advice.tasks)
    }
}
