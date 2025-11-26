package com.akyljer.data.seeding

import com.akyljer.data.repository.AlertRepository
import com.akyljer.data.repository.FarmerRepository
import com.akyljer.data.repository.FieldRepository
import com.akyljer.data.repository.impl.CropTaskRepositoryImpl
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data Seeding Manager
 * 
 * Handles first-time app launch data seeding.
 * Creates demo data to help users understand the app.
 * 
 * This is called on app startup to ensure users have
 * sample data to explore features.
 */
@Singleton
class DataSeedingManager @Inject constructor(
    private val farmerRepository: FarmerRepository,
    private val fieldRepository: FieldRepository,
    private val alertRepository: AlertRepository,
    private val taskRepository: CropTaskRepositoryImpl
) {
    
    /**
     * Check if app needs seeding and seed if necessary
     * 
     * This should be called from Application.onCreate() or
     * from a splash screen/initialization flow.
     */
    suspend fun seedIfNeeded() {
        if (!farmerRepository.hasProfile()) {
            seedInitialData()
        }
    }
    
    /**
     * Seed complete initial data for new users
     */
    suspend fun seedInitialData() {
        // 1. Create default farmer profile
        val profile = farmerRepository.createDefaultProfile()
        
        // 2. Create demo fields
        val fields = fieldRepository.createDemoFields(profile.id)
        
        // 3. Create demo tasks for first field
        if (fields.isNotEmpty()) {
            taskRepository.createDemoTasks(fields[0].id)
        }
        
        // 4. Create demo alerts
        if (fields.isNotEmpty()) {
            alertRepository.createDemoAlerts(fields[0].id)
        }
        
        // 5. Create a general welcome alert
        createWelcomeAlert()
    }
    
    /**
     * Clear all data (for testing or reset)
     */
    suspend fun clearAllData() {
        val profile = farmerRepository.getProfile().first()
        profile?.let {
            farmerRepository.deleteProfile(it)
        }
        // Fields, tasks, and alerts will be cascade deleted
    }
    
    /**
     * Reseed data (clear and seed again)
     */
    suspend fun reseedData() {
        clearAllData()
        seedInitialData()
    }
    
    /**
     * Check if database has data
     */
    suspend fun hasData(): Boolean {
        return farmerRepository.hasProfile()
    }
    
    /**
     * Create welcome alert for first-time users
     */
    private suspend fun createWelcomeAlert() {
        val welcomeAlert = com.akyljer.data.local.entity.AlertEntity(
            id = java.util.UUID.randomUUID().toString(),
            fieldId = null,
            title = "Welcome to Акыл Жер! 🌾",
            message = "Welcome to your smart farming assistant! " +
                    "We've created some demo data to help you get started. " +
                    "Explore the features and feel free to add your own fields.",
            alertType = "GENERAL",
            severity = "LOW",
            isRead = false,
            isDismissed = false,
            actionRequired = false,
            source = "SYSTEM",
            createdAt = System.currentTimeMillis(),
            expiresAt = null
        )
        
        alertRepository.createAlert(welcomeAlert)
    }
    
    /**
     * Seed scenario-specific data for testing features
     */
    suspend fun seedScenario(scenario: DataScenario) {
        when (scenario) {
            DataScenario.DROUGHT_WARNING -> seedDroughtScenario()
            DataScenario.DISEASE_OUTBREAK -> seedDiseaseScenario()
            DataScenario.HARVEST_SEASON -> seedHarvestScenario()
            DataScenario.EMPTY -> clearAllData()
        }
    }
    
    private suspend fun seedDroughtScenario() {
        seedInitialData()
        val profile = farmerRepository.getProfile().first() ?: return
        val fields = fieldRepository.getFieldsByFarmer(profile.id).first()
        
        if (fields.isNotEmpty()) {
            alertRepository.generateWeatherAlert(fields[0].id, "DROUGHT")
            // Add irrigation tasks
            taskRepository.createTask(
                com.akyljer.data.local.entity.CropTaskEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    fieldId = fields[0].id,
                    title = "Emergency irrigation needed",
                    description = "Soil moisture critically low. Irrigate immediately.",
                    taskType = "IRRIGATION",
                    priority = "CRITICAL",
                    dueDate = System.currentTimeMillis(),
                    isCompleted = false,
                    source = "WEATHER_ALERT",
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }
    
    private suspend fun seedDiseaseScenario() {
        seedInitialData()
        val profile = farmerRepository.getProfile().first() ?: return
        val fields = fieldRepository.getFieldsByFarmer(profile.id).first()
        
        if (fields.isNotEmpty()) {
            alertRepository.generateDiseaseAlert(
                fields[0].id,
                "Wheat Rust",
                "MODERATE"
            )
        }
    }
    
    private suspend fun seedHarvestScenario() {
        seedInitialData()
        val profile = farmerRepository.getProfile().first() ?: return
        val fields = fieldRepository.getFieldsByFarmer(profile.id).first()
        
        if (fields.isNotEmpty()) {
            taskRepository.createTask(
                com.akyljer.data.local.entity.CropTaskEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    fieldId = fields[0].id,
                    title = "Harvest wheat field",
                    description = "Wheat has reached maturity. Begin harvest operations.",
                    taskType = "HARVESTING",
                    priority = "HIGH",
                    dueDate = System.currentTimeMillis() + (2L * 24 * 60 * 60 * 1000),
                    isCompleted = false,
                    source = "AI",
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }
}

/**
 * Predefined data scenarios for testing and demos
 */
enum class DataScenario {
    DROUGHT_WARNING,
    DISEASE_OUTBREAK,
    HARVEST_SEASON,
    EMPTY
}
