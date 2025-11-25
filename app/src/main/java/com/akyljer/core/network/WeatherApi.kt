package com.akyljer.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast")
    suspend fun forecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): WeatherResponse
}

@Serializable
data class WeatherResponse(
    @SerialName("min_temp_c") val minTempC: Double,
    @SerialName("max_temp_c") val maxTempC: Double,
    @SerialName("precip_mm") val precipitationMm: Double,
    val humidity: Double,
    val summary: String = ""
)

object WeatherApiFactory {
    fun create(baseUrl: String, retrofitBuilder: Retrofit.Builder): WeatherApi =
        retrofitBuilder.baseUrl(baseUrl).build().create(WeatherApi::class.java)
}
