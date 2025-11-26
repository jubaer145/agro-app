package com.akyljer.ai

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Disease Information Mapper
 * 
 * Maps disease labels from TFLite model to detailed information
 * including severity, type, treatments, and recommendations.
 */
@Singleton
class DiseaseInfoMapper @Inject constructor() {
    
    /**
     * Map disease label to detailed diagnosis information
     */
    fun mapLabelToDiagnosis(
        label: String,
        confidence: Float
    ): DiagnosisInfo {
        val normalizedLabel = label.lowercase().trim()
        
        // Determine severity based on disease type and confidence
        val severity = determineSeverity(normalizedLabel, confidence)
        
        // Get disease type
        val diseaseType = getDiseaseType(normalizedLabel)
        
        // Get affected part
        val affectedPart = getAffectedPart(normalizedLabel)
        
        // Get spread risk
        val spreadRisk = getSpreadRisk(diseaseType)
        
        return DiagnosisInfo(
            diagnosis = formatDiagnosisName(label),
            confidence = confidence.toDouble(),
            severity = severity,
            diseaseType = diseaseType,
            affectedPart = affectedPart,
            spreadRisk = spreadRisk,
            actionRequired = severity in listOf("HIGH", "CRITICAL")
        )
    }
    
    /**
     * Determine severity based on disease and confidence
     */
    private fun determineSeverity(label: String, confidence: Float): String {
        // Healthy plants
        if (label.contains("healthy")) {
            return "LOW"
        }
        
        // Critical diseases
        val criticalDiseases = listOf(
            "late blight", "bacterial wilt", "virus", "mosaic", "wilt",
            "root rot", "stem rot", "canker"
        )
        if (criticalDiseases.any { label.contains(it) }) {
            return if (confidence > 0.8f) "CRITICAL" else "HIGH"
        }
        
        // High severity diseases
        val highSeverityDiseases = listOf(
            "early blight", "rust", "anthracnose", "scab", "fire blight"
        )
        if (highSeverityDiseases.any { label.contains(it) }) {
            return if (confidence > 0.8f) "HIGH" else "MEDIUM"
        }
        
        // Medium severity
        val mediumSeverityDiseases = listOf(
            "leaf spot", "mildew", "mold", "leaf curl"
        )
        if (mediumSeverityDiseases.any { label.contains(it) }) {
            return "MEDIUM"
        }
        
        // Low severity issues
        val lowSeverityIssues = listOf(
            "nutrient", "deficiency", "environmental", "minor"
        )
        if (lowSeverityIssues.any { label.contains(it) }) {
            return "LOW"
        }
        
        // Default based on confidence
        return when {
            confidence > 0.9f -> "HIGH"
            confidence > 0.7f -> "MEDIUM"
            else -> "LOW"
        }
    }
    
    /**
     * Get disease type from label
     */
    private fun getDiseaseType(label: String): String {
        return when {
            label.contains("healthy") -> "none"
            label.contains("virus") || label.contains("mosaic") -> "viral"
            label.contains("bacteria") || label.contains("wilt") && !label.contains("fusarium") -> "bacterial"
            label.contains("fungus") || label.contains("blight") || label.contains("rust") ||
            label.contains("mildew") || label.contains("spot") || label.contains("rot") -> "fungal"
            label.contains("nutrient") || label.contains("deficiency") -> "nutrient"
            label.contains("pest") || label.contains("insect") -> "pest"
            label.contains("environmental") || label.contains("stress") -> "environmental"
            else -> "unknown"
        }
    }
    
    /**
     * Get affected plant part from label
     */
    private fun getAffectedPart(label: String): String {
        return when {
            label.contains("leaf") || label.contains("foliage") -> "leaves"
            label.contains("stem") || label.contains("stalk") -> "stem"
            label.contains("fruit") || label.contains("berry") -> "fruit"
            label.contains("root") -> "roots"
            label.contains("flower") || label.contains("blossom") -> "flowers"
            else -> "whole_plant"
        }
    }
    
    /**
     * Get spread risk based on disease type
     */
    private fun getSpreadRisk(diseaseType: String): String {
        return when (diseaseType) {
            "viral", "bacterial" -> "high"
            "fungal" -> "medium"
            "pest" -> "medium"
            "nutrient", "environmental" -> "none"
            else -> "low"
        }
    }
    
    /**
     * Format disease name for display
     */
    private fun formatDiagnosisName(label: String): String {
        // Remove underscores and capitalize words
        return label
            .replace("_", " ")
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }
    }
}

/**
 * Diagnosis information mapped from model output
 */
data class DiagnosisInfo(
    val diagnosis: String,
    val confidence: Double,
    val severity: String,
    val diseaseType: String,
    val affectedPart: String,
    val spreadRisk: String,
    val actionRequired: Boolean
)
