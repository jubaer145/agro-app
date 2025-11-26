package com.akyljer.data.repository

import com.akyljer.data.local.entity.AlertEntity
import com.akyljer.data.local.entity.CropTaskEntity
import com.akyljer.data.local.entity.FieldEntity
import com.akyljer.data.local.entity.FarmerProfileEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Crop Advisor (AI-powered farming recommendations)
 * 
 * This repository combines:
 * - Farmer profile data
 * - Field data (crop, soil, location)
 * - Weather data (from API)
 * - Historical patterns
 * 
 * To generate:
 * - Smart crop task recommendations
 * - Risk alerts (pest, disease, weather)
 * - Optimal planting/harvest timing
 * 
 * Future ML Integration:
 * - TODO: Replace rule-based logic with ML model (TensorFlow Lite or cloud API)
 * - TODO: Integrate weather API for real-time forecasts
 * - TODO: Add satellite imagery analysis for field health
 * - TODO: Incorporate IoT sensor data (soil moisture, temperature)
 * - TODO: Learn from farmer actions and outcomes to improve recommendations
 */
interface CropAdvisorRepository {
    
    /**
     * Generate crop task recommendations for a field
     * 
     * Takes into account:
     * - Current crop and growth stage
     * - Season and weather forecast
     * - Soil conditions
     * - Historical data
     * 
     * @param fieldId Field to analyze
     * @param weatherData Current and forecast weather (from Weather API)
     * @return List of recommended tasks with priority and timing
     * 
     * TODO: Replace with ML model that considers:
     * - Regional best practices
     * - Crop-specific requirements
     * - Climate patterns
     * - Disease/pest risk prediction
     */
    suspend fun generateCropTasks(
        fieldId: String,
        weatherData: WeatherData? = null
    ): List<CropTaskEntity>
    
    /**
     * Generate risk alerts for a field
     * 
     * Analyzes:
     * - Weather patterns (frost, drought, excessive rain)
     * - Pest/disease risk based on season and conditions
     * - Optimal irrigation needs
     * 
     * @param fieldId Field to analyze
     * @param weatherData Current and forecast weather
     * @return List of alerts with severity and recommendations
     * 
     * TODO: Integrate with:
     * - Weather API for real-time alerts
     * - ML model for disease/pest prediction
     * - IoT sensors for real-time field conditions
     */
    suspend fun generateRiskAlerts(
        fieldId: String,
        weatherData: WeatherData? = null
    ): List<AlertEntity>
    
    /**
     * Get crop recommendations for a field
     * 
     * Based on:
     * - Soil type
     * - Climate zone
     * - Available resources
     * - Market demand
     * 
     * @param fieldId Field to analyze
     * @param season Target planting season
     * @return List of recommended crops with expected yield
     * 
     * TODO: ML model for crop optimization based on:
     * - Historical yield data
     * - Market prices
     * - Climate change projections
     * - Farmer preferences and constraints
     */
    suspend fun getCropRecommendations(
        fieldId: String,
        season: String
    ): List<CropRecommendation>
    
    /**
     * Analyze field health and provide insights
     * 
     * @param fieldId Field to analyze
     * @return Field health score and recommendations
     * 
     * TODO: Integrate satellite imagery and NDVI analysis
     */
    suspend fun analyzeFieldHealth(fieldId: String): FieldHealthAnalysis
    
    /**
     * Get optimal irrigation schedule
     * 
     * @param fieldId Field to analyze
     * @param weatherData Weather forecast
     * @return Irrigation recommendations
     * 
     * TODO: ML model considering:
     * - Soil moisture sensors
     * - Crop water requirements
     * - Weather forecast
     * - Evapotranspiration rates
     */
    suspend fun getIrrigationSchedule(
        fieldId: String,
        weatherData: WeatherData? = null
    ): IrrigationSchedule
}

/**
 * Weather data model
 * TODO: Replace with actual Weather API model (OpenWeather, etc.)
 */
data class WeatherData(
    val temperature: Double, // Celsius
    val humidity: Double, // Percentage
    val rainfall: Double, // mm
    val windSpeed: Double, // km/h
    val forecast: List<WeatherForecast> = emptyList()
)

data class WeatherForecast(
    val date: Long, // Unix timestamp
    val tempMin: Double,
    val tempMax: Double,
    val rainfall: Double,
    val conditions: String // "sunny", "rainy", "cloudy", etc.
)

/**
 * Crop recommendation model
 */
data class CropRecommendation(
    val cropName: String,
    val variety: String,
    val suitabilityScore: Double, // 0.0 to 1.0
    val expectedYield: Double, // tons per hectare
    val growthDuration: Int, // days
    val waterRequirement: String, // "low", "medium", "high"
    val difficultyLevel: String, // "easy", "medium", "hard"
    val marketDemand: String, // "low", "medium", "high"
    val recommendations: List<String>
)

/**
 * Field health analysis model
 */
data class FieldHealthAnalysis(
    val healthScore: Double, // 0.0 to 1.0
    val ndviScore: Double? = null, // Normalized Difference Vegetation Index
    val soilHealth: String, // "poor", "fair", "good", "excellent"
    val stressIndicators: List<String>, // "drought", "nutrient_deficiency", "pest_pressure"
    val recommendations: List<String>
)

/**
 * Irrigation schedule model
 */
data class IrrigationSchedule(
    val nextIrrigationDate: Long, // Unix timestamp
    val waterAmount: Double, // liters or mm
    val frequency: String, // "daily", "every_2_days", "weekly"
    val method: String, // "drip", "sprinkler", "flood"
    val schedule: List<IrrigationEvent>
)

data class IrrigationEvent(
    val date: Long,
    val waterAmount: Double,
    val duration: Int, // minutes
    val notes: String
)
