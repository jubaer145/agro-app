package com.akyljer.data.repository

import android.graphics.Bitmap
import com.akyljer.data.local.entity.PhotoDiagnosisEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Photo Doctor (Plant disease detection using AI)
 * 
 * Uses TensorFlow Lite model to analyze plant images and detect:
 * - Diseases
 * - Pest damage
 * - Nutrient deficiencies
 * - Growth abnormalities
 * 
 * Future ML Integration:
 * - TODO: Integrate TensorFlow Lite plant disease detection model
 * - TODO: Add cloud-based model for more accurate diagnosis (when online)
 * - TODO: Support multiple crop types with specialized models
 * - TODO: Add image quality validation before processing
 * - TODO: Implement model updates/downloads
 * - TODO: Add training data collection from user feedback
 */
interface PhotoDoctorRepository {
    
    /**
     * Analyze plant photo for diseases and issues
     * 
     * This is the main entry point for Photo Doctor feature.
     * 
     * Process:
     * 1. Validate image quality
     * 2. Preprocess image (resize, normalize)
     * 3. Run TFLite model inference
     * 4. Post-process results
     * 5. Save diagnosis to database
     * 6. Generate alert if severe issue detected
     * 
     * @param bitmap Image to analyze
     * @param fieldId Associated field (optional)
     * @param cropType Type of crop (helps select appropriate model)
     * @return Diagnosis result with disease detection and recommendations
     * 
     * TODO: Replace stub with actual TFLite model inference
     * TODO: Add support for multiple model versions
     * TODO: Implement confidence threshold tuning
     */
    suspend fun analyzePlantPhoto(
        bitmap: Bitmap,
        fieldId: String? = null,
        cropType: String? = null
    ): PhotoDiagnosisResult
    
    /**
     * Get diagnosis history
     * 
     * @return Flow of all saved diagnoses
     */
    fun getAllDiagnoses(): Flow<List<PhotoDiagnosisEntity>>
    
    /**
     * Get diagnoses for a specific field
     * 
     * @param fieldId Field ID
     * @return Flow of field-specific diagnoses
     */
    fun getDiagnosesByField(fieldId: String): Flow<List<PhotoDiagnosisEntity>>
    
    /**
     * Get diagnoses with severe issues (HIGH or CRITICAL severity)
     * 
     * @return Flow of severe diagnoses
     */
    fun getSevereDiagnoses(): Flow<List<PhotoDiagnosisEntity>>
    
    /**
     * Save diagnosis to database
     * 
     * @param diagnosis Diagnosis to save
     */
    suspend fun saveDiagnosis(diagnosis: PhotoDiagnosisEntity)
    
    /**
     * Delete diagnosis
     * 
     * @param diagnosis Diagnosis to delete
     */
    suspend fun deleteDiagnosis(diagnosis: PhotoDiagnosisEntity)
    
    /**
     * Update diagnosis notes (user can add their own observations)
     * 
     * @param diagnosisId Diagnosis ID
     * @param notes User notes
     */
    suspend fun updateDiagnosisNotes(diagnosisId: String, notes: String)
    
    /**
     * Validate image quality before processing
     * 
     * Checks:
     * - Resolution (must be sufficient for model)
     * - Blur detection
     * - Lighting conditions
     * - Plant visibility
     * 
     * @param bitmap Image to validate
     * @return Validation result with quality score
     * 
     * TODO: Implement image quality checks
     */
    suspend fun validateImageQuality(bitmap: Bitmap): ImageQualityResult
    
    /**
     * Get supported crop types for disease detection
     * 
     * @return List of crop types the model can analyze
     * 
     * TODO: Load from model metadata
     */
    suspend fun getSupportedCrops(): List<String>
    
    /**
     * Check if TFLite model is available and loaded
     * 
     * @return True if model is ready
     */
    suspend fun isModelReady(): Boolean
    
    /**
     * Download or update disease detection model
     * 
     * @param modelVersion Version to download (optional, latest if null)
     * @return Download progress (0.0 to 1.0)
     * 
     * TODO: Implement model download from cloud storage
     */
    suspend fun downloadModel(modelVersion: String? = null): Flow<Float>
    
    /**
     * Get disease information and treatment recommendations
     * 
     * @param diseaseName Disease name from diagnosis
     * @return Detailed disease info with treatment options
     */
    suspend fun getDiseaseInfo(diseaseName: String): DiseaseDetailInfo
}

/**
 * Photo diagnosis result from TFLite model
 */
data class PhotoDiagnosisResult(
    val diagnosis: String,
    val confidence: Double, // 0.0 to 1.0
    val severity: String, // "LOW", "MEDIUM", "HIGH", "CRITICAL"
    val diseaseType: String, // "fungal", "bacterial", "viral", "pest", "nutrient", "environmental"
    val affectedPart: String, // "leaves", "stem", "fruit", "roots", "whole_plant"
    val treatments: List<TreatmentOption>,
    val recommendations: List<String>,
    val preventionTips: List<String>,
    val spreadRisk: String, // "none", "low", "medium", "high"
    val actionRequired: Boolean, // If immediate action needed
    val alternativeDiagnoses: List<AlternativeDiagnosis> = emptyList(), // Other possible diseases
    val modelVersion: String = "1.0.0",
    val processingTimeMs: Long = 0,
    val imageMetadata: ImageMetadata? = null
)

/**
 * Alternative diagnosis with lower confidence
 */
data class AlternativeDiagnosis(
    val diagnosis: String,
    val confidence: Double,
    val diseaseType: String
)

/**
 * Treatment option for detected disease
 */
data class TreatmentOption(
    val name: String,
    val type: String, // "organic", "chemical", "cultural", "biological"
    val description: String,
    val effectiveness: String, // "low", "medium", "high"
    val cost: String, // "low", "medium", "high"
    val applicationMethod: String,
    val safetyPrecautions: List<String>,
    val organic: Boolean
)

/**
 * Image quality validation result
 */
data class ImageQualityResult(
    val isValid: Boolean,
    val qualityScore: Double, // 0.0 to 1.0
    val issues: List<String>, // "too_blurry", "poor_lighting", "plant_not_visible", etc.
    val recommendations: List<String>
)

/**
 * Image metadata for diagnosis
 */
data class ImageMetadata(
    val width: Int,
    val height: Int,
    val fileSize: Long,
    val captureTime: Long,
    val location: String? = null, // GPS coordinates if available
    val brightness: Double? = null,
    val contrast: Double? = null
)

/**
 * Detailed disease information
 */
data class DiseaseDetailInfo(
    val name: String,
    val scientificName: String,
    val description: String,
    val symptoms: List<String>,
    val causes: List<String>,
    val affectedCrops: List<String>,
    val treatmentOptions: List<TreatmentOption>,
    val preventionMeasures: List<String>,
    val culturalPractices: List<String>,
    val seasonalOccurrence: String,
    val economicImpact: String,
    val resources: List<ExternalResource> = emptyList() // Links to scientific papers, extension services, etc.
)

/**
 * External resource for more information
 */
data class ExternalResource(
    val title: String,
    val url: String,
    val type: String // "article", "video", "pdf", "website"
)
