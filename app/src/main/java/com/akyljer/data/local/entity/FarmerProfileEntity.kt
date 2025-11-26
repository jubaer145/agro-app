package com.akyljer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for Farmer Profile
 * 
 * Stores farmer personal information and preferences.
 */
@Entity(tableName = "farmer_profiles")
data class FarmerProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "phone")
    val phone: String? = null,
    
    @ColumnInfo(name = "location")
    val location: String? = null,
    
    @ColumnInfo(name = "farm_size_hectares")
    val farmSizeHectares: Double? = null,
    
    @ColumnInfo(name = "preferred_language")
    val preferredLanguage: String = "ky", // ky, ru, en
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
