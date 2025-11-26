package com.akyljer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for Animal Case (AgroVet)
 * 
 * Stores animal health cases and diagnoses for the AgroVet module.
 * Optional entity for MVP - tracks history of animal health issues.
 */
@Entity(
    tableName = "animal_cases",
    indices = [Index(value = ["created_at"])]
)
data class AnimalCaseEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "farmer_id")
    val farmerId: String,
    
    @ColumnInfo(name = "animal_type")
    val animalType: String, // CATTLE, SHEEP, GOAT, HORSE, POULTRY, OTHER
    
    @ColumnInfo(name = "animal_count")
    val animalCount: Int = 1,
    
    @ColumnInfo(name = "symptoms")
    val symptoms: String,
    
    @ColumnInfo(name = "diagnosis")
    val diagnosis: String? = null,
    
    @ColumnInfo(name = "severity")
    val severity: String, // MILD, MODERATE, SEVERE, EMERGENCY
    
    @ColumnInfo(name = "recommended_action")
    val recommendedAction: String? = null,
    
    @ColumnInfo(name = "status")
    val status: String, // ACTIVE, MONITORING, RESOLVED, VETERINARIAN_NEEDED
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "resolved_at")
    val resolvedAt: Long? = null
)
