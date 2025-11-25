package com.akyljer.core.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM crop_tasks ORDER BY dueDateMillis ASC")
    fun observeTasks(): Flow<List<CropTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(tasks: List<CropTaskEntity>)
}

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_forecast LIMIT 1")
    fun observeForecast(): Flow<List<WeatherForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(forecast: WeatherForecastEntity)
}

@Dao
interface DiagnosisDao {
    @Query("SELECT * FROM diagnosis_history ORDER BY id DESC")
    fun observeHistory(): Flow<List<DiagnosisEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diagnosis: DiagnosisEntity)
}

@Database(
    entities = [CropTaskEntity::class, WeatherForecastEntity::class, DiagnosisEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun weatherDao(): WeatherDao
    abstract fun diagnosisDao(): DiagnosisDao
}
