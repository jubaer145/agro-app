package com.akyljer.di

import androidx.room.Room
import com.akyljer.core.db.AppDatabase
import com.akyljer.core.network.WeatherApi
import com.akyljer.core.network.WeatherApiFactory
import com.akyljer.core.repository.AdvisorRepository
import com.akyljer.core.repository.AdvisorRepositoryImpl
import com.akyljer.core.repository.DiagnosisRepository
import com.akyljer.core.repository.DiagnosisRepositoryImpl
import com.akyljer.core.repository.TaskRepository
import com.akyljer.core.repository.TaskRepositoryImpl
import com.akyljer.core.repository.WeatherRepository
import com.akyljer.core.repository.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
import android.app.Application
import okhttp3.MediaType.Companion.toMediaType

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "akyljer.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTaskDao(db: AppDatabase) = db.taskDao()

    @Provides
    fun provideWeatherDao(db: AppDatabase) = db.weatherDao()

    @Provides
    fun provideDiagnosisDao(db: AppDatabase) = db.diagnosisDao()

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15.seconds.toJavaDuration())
            .readTimeout(15.seconds.toJavaDuration())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
    }

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi =
        WeatherApiFactory.create("https://example.com/", retrofit)

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: com.akyljer.core.db.TaskDao): TaskRepository =
        TaskRepositoryImpl(taskDao)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherDao: com.akyljer.core.db.WeatherDao,
        api: WeatherApi
    ): WeatherRepository = WeatherRepositoryImpl(weatherDao, api)

    @Provides
    @Singleton
    fun provideDiagnosisRepository(dao: com.akyljer.core.db.DiagnosisDao): DiagnosisRepository =
        DiagnosisRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideAdvisorRepository(
        engine: com.akyljer.core.rules.AdvisorRuleEngine,
        taskRepository: TaskRepository
    ): AdvisorRepository = AdvisorRepositoryImpl(engine, taskRepository)
}
