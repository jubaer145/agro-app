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

data class Alert(
    val id: String,
    val title: String,
    val message: String,
    val riskLevel: RiskLevel,
    val createdAtMillis: Long
)

data class FarmerProfile(
    val id: String,
    val name: String,
    val phone: String? = null,
    val preferredLanguage: String = "ky"
)

data class Field(
    val id: String,
    val farmerId: String,
    val name: String,
    val location: String,
    val crop: String,
    val areaHectares: Double? = null
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

data class PhotoDiagnosisResult(
    val id: String,
    val fieldId: String? = null,
    val label: String,
    val confidence: Float,
    val recommendation: String,
    val capturedAtMillis: Long
)

data class AgroVetAnswer(
    val condition: String,
    val urgency: RiskLevel,
    val recommendation: String
)

data class AnimalCase(
    val id: String,
    val symptoms: List<String>,
    val recordedAtMillis: Long
)

data class VetTriageResult(
    val caseId: String,
    val condition: String,
    val urgency: RiskLevel,
    val recommendation: String
)
