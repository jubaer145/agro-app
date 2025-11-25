package com.akyljer.core.model

enum class RiskLevel { LOW, MEDIUM, HIGH }

data class FarmerInput(
    val crop: String = "",
    val growthStage: String = "",
    val location: String = "",
    val notes: String = ""
)

data class CropTask(
    val id: String,
    val title: String,
    val description: String,
    val dueDateMillis: Long,
    val riskLevel: RiskLevel = RiskLevel.LOW
)

data class RiskFlag(
    val id: String,
    val label: String,
    val level: RiskLevel,
    val details: String
)

data class WeatherForecast(
    val location: String,
    val dateMillis: Long,
    val minTempC: Double,
    val maxTempC: Double,
    val precipitationMm: Double,
    val humidity: Double,
    val summary: String = ""
)

data class Diagnosis(
    val label: String,
    val confidence: Float,
    val recommendation: String
)

data class AgroVetAnswer(
    val condition: String,
    val urgency: RiskLevel,
    val recommendation: String
)
