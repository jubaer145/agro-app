package com.akyljer.core.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akyljer.core.model.CropTask
import com.akyljer.core.model.Diagnosis
import com.akyljer.core.model.RiskLevel
import com.akyljer.core.model.WeatherForecast

@Entity(tableName = "crop_tasks")
data class CropTaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val dueDateMillis: Long,
    val riskLevel: String
)

fun CropTaskEntity.toDomain(): CropTask =
    CropTask(
        id = id,
        title = title,
        description = description,
        dueDateMillis = dueDateMillis,
        riskLevel = RiskLevel.valueOf(riskLevel)
    )

fun CropTask.toEntity(): CropTaskEntity =
    CropTaskEntity(
        id = id,
        title = title,
        description = description,
        dueDateMillis = dueDateMillis,
        riskLevel = riskLevel.name
    )

@Entity(tableName = "weather_forecast")
data class WeatherForecastEntity(
    @PrimaryKey val location: String,
    val dateMillis: Long,
    val minTempC: Double,
    val maxTempC: Double,
    val precipitationMm: Double,
    val humidity: Double,
    val summary: String
)

fun WeatherForecastEntity.toDomain(): WeatherForecast =
    WeatherForecast(
        location = location,
        dateMillis = dateMillis,
        minTempC = minTempC,
        maxTempC = maxTempC,
        precipitationMm = precipitationMm,
        humidity = humidity,
        summary = summary
    )

fun WeatherForecast.toEntity(): WeatherForecastEntity =
    WeatherForecastEntity(
        location = location,
        dateMillis = dateMillis,
        minTempC = minTempC,
        maxTempC = maxTempC,
        precipitationMm = precipitationMm,
        humidity = humidity,
        summary = summary
    )

@Entity(tableName = "diagnosis_history")
data class DiagnosisEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String,
    val confidence: Float,
    val recommendation: String
)

fun DiagnosisEntity.toDomain(): Diagnosis =
    Diagnosis(label = label, confidence = confidence, recommendation = recommendation)

fun Diagnosis.toEntity(): DiagnosisEntity =
    DiagnosisEntity(label = label, confidence = confidence, recommendation = recommendation)
