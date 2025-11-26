package com.akyljer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for Field
 * 
 * Represents a farmer's field with crop information.
 */
@Entity(
    tableName = "fields",
    foreignKeys = [
        ForeignKey(
            entity = FarmerProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["farmer_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["farmer_id"])]
)
data class FieldEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "farmer_id")
    val farmerId: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "crop_type")
    val cropType: String,
    
    @ColumnInfo(name = "size_hectares")
    val sizeHectares: Double,
    
    @ColumnInfo(name = "location")
    val location: String? = null,
    
    @ColumnInfo(name = "planting_date")
    val plantingDate: Long? = null,
    
    @ColumnInfo(name = "expected_harvest_date")
    val expectedHarvestDate: Long? = null,
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
