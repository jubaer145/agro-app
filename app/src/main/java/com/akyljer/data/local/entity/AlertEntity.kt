package com.akyljer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for Alert
 * 
 * Stores risk alerts and notifications from various sources:
 * - Weather risks (drought, frost, heavy rain)
 * - Crop health alerts from Photo Doctor
 * - General farming alerts
 */
@Entity(
    tableName = "alerts",
    foreignKeys = [
        ForeignKey(
            entity = FieldEntity::class,
            parentColumns = ["id"],
            childColumns = ["field_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["field_id"]), Index(value = ["created_at"])]
)
data class AlertEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "field_id")
    val fieldId: String? = null,
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "message")
    val message: String,
    
    @ColumnInfo(name = "alert_type")
    val alertType: String, // WEATHER, DISEASE, PEST, IRRIGATION, GENERAL
    
    @ColumnInfo(name = "severity")
    val severity: String, // LOW, MEDIUM, HIGH, CRITICAL
    
    @ColumnInfo(name = "is_read")
    val isRead: Boolean = false,
    
    @ColumnInfo(name = "is_dismissed")
    val isDismissed: Boolean = false,
    
    @ColumnInfo(name = "action_required")
    val actionRequired: Boolean = false,
    
    @ColumnInfo(name = "source")
    val source: String, // WEATHER_API, PHOTO_DOCTOR, CROP_ADVISOR, SYSTEM
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "expires_at")
    val expiresAt: Long? = null
)
