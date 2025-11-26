package com.akyljer.ui.features.photodoctor

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akyljer.data.repository.AlertRepository
import com.akyljer.data.repository.PhotoDoctorRepository
import com.akyljer.data.repository.PhotoDiagnosisResult
import com.akyljer.data.local.entity.AlertEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Photo Doctor feature
 * 
 * Handles:
 * - Camera/gallery image capture
 * - TFLite model inference
 * - Diagnosis result display
 * - Alert generation for severe diseases
 */
@HiltViewModel
class PhotoDoctorViewModel @Inject constructor(
    private val photoDoctorRepository: PhotoDoctorRepository,
    private val alertRepository: AlertRepository
) : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow<PhotoDoctorUiState>(PhotoDoctorUiState.Idle)
    val uiState: StateFlow<PhotoDoctorUiState> = _uiState.asStateFlow()
    
    // Diagnosis result
    private val _diagnosisResult = MutableStateFlow<PhotoDiagnosisResult?>(null)
    val diagnosisResult: StateFlow<PhotoDiagnosisResult?> = _diagnosisResult.asStateFlow()
    
    // Model status
    private val _isModelReady = MutableStateFlow(false)
    val isModelReady: StateFlow<Boolean> = _isModelReady.asStateFlow()
    
    // Diagnosis history
    val diagnosisHistory = photoDoctorRepository.getAllDiagnoses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    init {
        checkModelStatus()
    }
    
    /**
     * Check if TFLite model is ready
     */
    private fun checkModelStatus() {
        viewModelScope.launch {
            _isModelReady.value = photoDoctorRepository.isModelReady()
        }
    }
    
    /**
     * Analyze plant photo from camera or gallery
     */
    fun analyzePlantPhoto(
        bitmap: Bitmap,
        fieldId: String? = null,
        cropType: String? = null
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = PhotoDoctorUiState.Analyzing
                
                // Run disease detection
                val result = photoDoctorRepository.analyzePlantPhoto(
                    bitmap = bitmap,
                    fieldId = fieldId,
                    cropType = cropType
                )
                
                _diagnosisResult.value = result
                _uiState.value = PhotoDoctorUiState.Success(result)
                
                // Generate alert if disease is severe
                if (result.actionRequired && result.severity in listOf("HIGH", "CRITICAL")) {
                    generateDiseaseAlert(result, fieldId)
                }
                
            } catch (e: Exception) {
                _uiState.value = PhotoDoctorUiState.Error(
                    message = "Failed to analyze image: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Validate image quality before analysis
     */
    fun validateImageQuality(bitmap: Bitmap) {
        viewModelScope.launch {
            val qualityResult = photoDoctorRepository.validateImageQuality(bitmap)
            
            if (!qualityResult.isValid) {
                _uiState.value = PhotoDoctorUiState.Error(
                    message = "Image quality issues detected:\n${qualityResult.recommendations.joinToString("\n")}"
                )
            }
        }
    }
    
    /**
     * Generate alert for severe disease detection
     */
    private suspend fun generateDiseaseAlert(result: PhotoDiagnosisResult, fieldId: String?) {
        try {
            val alert = AlertEntity(
                id = java.util.UUID.randomUUID().toString(),
                fieldId = fieldId,
                title = "🦠 Disease Detected: ${result.diagnosis}",
                message = "Detected ${result.diagnosis} with ${(result.confidence * 100).toInt()}% confidence. " +
                        "Severity: ${result.severity}. Immediate action recommended: ${result.recommendations.firstOrNull() ?: "See details"}",
                severity = result.severity,
                alertType = "disease",
                isRead = false,
                actionRequired = true,
                source = "PHOTO_DOCTOR",
                createdAt = System.currentTimeMillis()
            )
            
            alertRepository.createAlert(alert)
        } catch (e: Exception) {
            // Log error but don't fail the diagnosis
            e.printStackTrace()
        }
    }
    
    /**
     * Clear current diagnosis result
     */
    fun clearResult() {
        _diagnosisResult.value = null
        _uiState.value = PhotoDoctorUiState.Idle
    }
    
    /**
     * Reset to idle state
     */
    fun resetState() {
        _uiState.value = PhotoDoctorUiState.Idle
    }
    
    /**
     * Get disease information details
     */
    fun getDiseaseInfo(diseaseName: String) {
        viewModelScope.launch {
            try {
                val diseaseInfo = photoDoctorRepository.getDiseaseInfo(diseaseName)
                _uiState.value = PhotoDoctorUiState.DiseaseInfoLoaded(diseaseInfo)
            } catch (e: Exception) {
                _uiState.value = PhotoDoctorUiState.Error("Failed to load disease info")
            }
        }
    }
    
    /**
     * Save diagnosis notes
     */
    fun saveDiagnosisNotes(diagnosisId: String, notes: String) {
        viewModelScope.launch {
            try {
                photoDoctorRepository.updateDiagnosisNotes(diagnosisId, notes)
            } catch (e: Exception) {
                _uiState.value = PhotoDoctorUiState.Error("Failed to save notes")
            }
        }
    }
}

/**
 * UI State for Photo Doctor screen
 */
sealed class PhotoDoctorUiState {
    object Idle : PhotoDoctorUiState()
    object Analyzing : PhotoDoctorUiState()
    data class Success(val result: PhotoDiagnosisResult) : PhotoDoctorUiState()
    data class Error(val message: String) : PhotoDoctorUiState()
    data class DiseaseInfoLoaded(val info: com.akyljer.data.repository.DiseaseDetailInfo) : PhotoDoctorUiState()
}
