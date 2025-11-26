package com.akyljer.data.repository

import com.akyljer.data.local.entity.AnimalCaseEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for AgroVet (Livestock health management)
 * 
 * Provides:
 * - Symptom-to-disease mapping
 * - Triage recommendations (self-care vs vet visit)
 * - Treatment guidance
 * - Vaccination schedules
 * - Health records management
 * 
 * Future ML Integration:
 * - TODO: ML model for disease diagnosis based on symptoms
 * - TODO: Image recognition for livestock health assessment
 * - TODO: Integration with veterinary telemedicine platform
 * - TODO: Predictive analytics for disease outbreaks
 */
interface AgroVetRepository {
    
    /**
     * Analyze symptoms and provide triage recommendation
     * 
     * @param animalType Type of animal ("cattle", "sheep", "goat", "chicken", etc.)
     * @param symptoms List of observed symptoms
     * @param severity User-reported severity (1-10)
     * @return Triage result with urgency and recommendations
     * 
     * TODO: Replace with ML model trained on veterinary data
     * TODO: Add image analysis for visual symptoms
     */
    suspend fun analyzeSymptoms(
        animalType: String,
        symptoms: List<String>,
        severity: Int,
        additionalInfo: Map<String, String> = emptyMap()
    ): TriageResult
    
    /**
     * Get common diseases for an animal type
     * 
     * @param animalType Type of animal
     * @param season Current season (affects disease prevalence)
     * @return List of common diseases with prevention tips
     */
    suspend fun getCommonDiseases(
        animalType: String,
        season: String? = null
    ): List<DiseaseInfo>
    
    /**
     * Get vaccination schedule for an animal
     * 
     * @param animalType Type of animal
     * @param age Age of animal in months
     * @return Recommended vaccination schedule
     */
    suspend fun getVaccinationSchedule(
        animalType: String,
        age: Int
    ): VaccinationSchedule
    
    /**
     * Save animal health case
     * 
     * @param animalCase Case details
     */
    suspend fun saveAnimalCase(animalCase: AnimalCaseEntity)
    
    /**
     * Get all animal health cases
     * 
     * @return Flow of all cases
     */
    fun getAllCases(): Flow<List<AnimalCaseEntity>>
    
    /**
     * Get cases for a specific animal type
     * 
     * @param animalType Type of animal
     * @return Flow of filtered cases
     */
    fun getCasesByAnimalType(animalType: String): Flow<List<AnimalCaseEntity>>
    
    /**
     * Get active/unresolved cases
     * 
     * @return Flow of active cases
     */
    fun getActiveCases(): Flow<List<AnimalCaseEntity>>
    
    /**
     * Update case status
     * 
     * @param caseId Case ID
     * @param status New status
     * @param notes Additional notes
     */
    suspend fun updateCaseStatus(
        caseId: String,
        status: String,
        notes: String? = null
    )
    
    /**
     * Delete case
     * 
     * @param animalCase Case to delete
     */
    suspend fun deleteCase(animalCase: AnimalCaseEntity)
    
    /**
     * Get emergency vet contacts
     * 
     * @param location User location (for nearby vets)
     * @return List of vet contacts
     * 
     * TODO: Integrate with location services and vet directory API
     */
    suspend fun getEmergencyContacts(location: String? = null): List<VetContact>
    
    /**
     * Get treatment recommendations
     * 
     * @param diagnosis Disease diagnosis
     * @return Treatment recommendations
     */
    suspend fun getTreatmentRecommendations(diagnosis: String): List<TreatmentRecommendation>
}

/**
 * Triage result from symptom analysis
 */
data class TriageResult(
    val urgency: UrgencyLevel,
    val possibleDiseases: List<String>,
    val recommendations: List<String>,
    val requiresVet: Boolean,
    val selfCareAdvice: List<String>,
    val warningSigns: List<String>, // Signs that require immediate vet visit
    val confidence: Double // 0.0 to 1.0, from ML model
)

enum class UrgencyLevel {
    LOW,       // Monitor at home, routine care
    MEDIUM,    // Schedule vet visit within 1-2 days
    HIGH,      // Visit vet within 24 hours
    CRITICAL   // Emergency - immediate vet attention required
}

/**
 * Disease information
 */
data class DiseaseInfo(
    val name: String,
    val commonSymptoms: List<String>,
    val description: String,
    val prevention: List<String>,
    val seasonalRisk: String, // "high", "medium", "low"
    val contagious: Boolean,
    val vaccinationAvailable: Boolean
)

/**
 * Vaccination schedule
 */
data class VaccinationSchedule(
    val animalType: String,
    val vaccines: List<VaccineInfo>
)

data class VaccineInfo(
    val name: String,
    val disease: String,
    val ageInMonths: Int, // When to administer
    val boosterRequired: Boolean,
    val boosterIntervalMonths: Int? = null,
    val mandatory: Boolean,
    val notes: String
)

/**
 * Veterinary contact
 */
data class VetContact(
    val name: String,
    val phone: String,
    val address: String,
    val specialization: String,
    val emergency24h: Boolean,
    val distance: Double? = null, // km from user
    val rating: Double? = null // 0.0 to 5.0
)

/**
 * Treatment recommendation
 */
data class TreatmentRecommendation(
    val treatment: String,
    val dosage: String,
    val duration: String,
    val frequency: String,
    val instructions: List<String>,
    val precautions: List<String>,
    val requiresPrescription: Boolean
)
