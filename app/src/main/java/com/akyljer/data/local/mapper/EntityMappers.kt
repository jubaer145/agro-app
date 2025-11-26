package com.akyljer.data.local.mapper

import com.akyljer.core.model.FarmerProfile
import com.akyljer.core.model.Field
import com.akyljer.core.model.CropTask
import com.akyljer.core.model.Alert
import com.akyljer.core.model.RiskLevel
import com.akyljer.data.local.entity.FarmerProfileEntity
import com.akyljer.data.local.entity.FieldEntity
import com.akyljer.data.local.entity.CropTaskEntity
import com.akyljer.data.local.entity.AlertEntity
import java.util.UUID

/**
 * Mapper functions to convert between Room entities and domain models
 * 
 * These functions provide a clean separation between the data layer
 * and the domain/presentation layers.
 */

// ==================== Farmer Profile Mappers ====================

/**
 * Convert FarmerProfileEntity to domain model
 */
fun FarmerProfileEntity.toDomainModel(): FarmerProfile {
    return FarmerProfile(
        id = id,
        name = name,
        phone = phone,
        preferredLanguage = preferredLanguage
    )
}

/**
 * Convert domain model to FarmerProfileEntity
 */
fun FarmerProfile.toEntity(
    location: String? = null,
    farmSizeHectares: Double? = null
): FarmerProfileEntity {
    return FarmerProfileEntity(
        id = id,
        name = name,
        phone = phone,
        location = location,
        farmSizeHectares = farmSizeHectares,
        preferredLanguage = preferredLanguage,
        updatedAt = System.currentTimeMillis()
    )
}

// ==================== Field Mappers ====================

/**
 * Convert FieldEntity to domain model
 */
fun FieldEntity.toDomainModel(): Field {
    return Field(
        id = id,
        farmerId = farmerId,
        name = name,
        location = location ?: "",
        crop = cropType,
        areaHectares = sizeHectares
    )
}

/**
 * Convert domain model to FieldEntity
 */
fun Field.toEntity(
    cropType: String = crop,
    sizeHectares: Double = areaHectares ?: 0.0,
    plantingDate: Long? = null,
    expectedHarvestDate: Long? = null,
    notes: String? = null
): FieldEntity {
    return FieldEntity(
        id = id,
        farmerId = farmerId,
        name = name,
        cropType = cropType,
        sizeHectares = sizeHectares,
        location = location,
        plantingDate = plantingDate,
        expectedHarvestDate = expectedHarvestDate,
        notes = notes,
        updatedAt = System.currentTimeMillis()
    )
}

// ==================== Crop Task Mappers ====================

/**
 * Convert CropTaskEntity to domain model
 */
fun CropTaskEntity.toDomainModel(): CropTask {
    return CropTask(
        id = id,
        title = title,
        description = description,
        dueDateMillis = dueDate,
        riskLevel = mapPriorityToRiskLevel(priority)
    )
}

/**
 * Convert domain model to CropTaskEntity
 */
fun CropTask.toEntity(
    fieldId: String? = null,
    taskType: String = "OTHER",
    source: String = "MANUAL"
): CropTaskEntity {
    return CropTaskEntity(
        id = id,
        fieldId = fieldId,
        title = title,
        description = description,
        taskType = taskType,
        priority = mapRiskLevelToPriority(riskLevel),
        dueDate = dueDateMillis,
        source = source
    )
}

// ==================== Alert Mappers ====================

/**
 * Convert AlertEntity to domain model
 */
fun AlertEntity.toDomainModel(): Alert {
    return Alert(
        id = id,
        title = title,
        message = message,
        riskLevel = mapSeverityToRiskLevel(severity),
        createdAtMillis = createdAt
    )
}

/**
 * Convert domain model to AlertEntity
 */
fun Alert.toEntity(
    fieldId: String? = null,
    alertType: String = "GENERAL",
    source: String = "SYSTEM",
    actionRequired: Boolean = false
): AlertEntity {
    return AlertEntity(
        id = id,
        fieldId = fieldId,
        title = title,
        message = message,
        alertType = alertType,
        severity = mapRiskLevelToSeverity(riskLevel),
        source = source,
        actionRequired = actionRequired,
        createdAt = createdAtMillis
    )
}

// ==================== Helper Functions ====================

/**
 * Map priority string to RiskLevel
 */
private fun mapPriorityToRiskLevel(priority: String): RiskLevel {
    return when (priority) {
        "LOW" -> RiskLevel.LOW
        "MEDIUM" -> RiskLevel.MEDIUM
        "HIGH", "CRITICAL" -> RiskLevel.HIGH
        else -> RiskLevel.MEDIUM
    }
}

/**
 * Map RiskLevel to priority string
 */
private fun mapRiskLevelToPriority(riskLevel: RiskLevel): String {
    return when (riskLevel) {
        RiskLevel.LOW -> "LOW"
        RiskLevel.MEDIUM -> "MEDIUM"
        RiskLevel.HIGH -> "HIGH"
    }
}

/**
 * Map severity string to RiskLevel
 */
private fun mapSeverityToRiskLevel(severity: String): RiskLevel {
    return when (severity) {
        "LOW" -> RiskLevel.LOW
        "MEDIUM" -> RiskLevel.MEDIUM
        "HIGH", "CRITICAL" -> RiskLevel.HIGH
        else -> RiskLevel.MEDIUM
    }
}

/**
 * Map RiskLevel to severity string
 */
private fun mapRiskLevelToSeverity(riskLevel: RiskLevel): String {
    return when (riskLevel) {
        RiskLevel.LOW -> "LOW"
        RiskLevel.MEDIUM -> "MEDIUM"
        RiskLevel.HIGH -> "HIGH"
    }
}

// ==================== Extension Functions for Lists ====================

/**
 * Convert list of entities to domain models
 */
fun List<FarmerProfileEntity>.toFarmerProfileModels() = map { it.toDomainModel() }
fun List<FieldEntity>.toFieldModels() = map { it.toDomainModel() }
fun List<CropTaskEntity>.toCropTaskModels() = map { it.toDomainModel() }
fun List<AlertEntity>.toAlertModels() = map { it.toDomainModel() }

/**
 * Convert list of domain models to entities
 */
fun List<Field>.toFieldEntities() = map { it.toEntity() }
fun List<CropTask>.toCropTaskEntities(fieldId: String? = null) = map { it.toEntity(fieldId = fieldId) }
fun List<Alert>.toAlertEntities(fieldId: String? = null) = map { it.toEntity(fieldId = fieldId) }
