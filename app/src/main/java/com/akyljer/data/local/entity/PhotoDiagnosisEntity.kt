package com.akyljer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for Photo Diagnosis
 * 
 * Stores photo diagnosis history from the Photo Doctor feature.
 * Tracks plant disease detection results using TensorFlow Lite.
 */
@Entity(
    tableName = "photo_diagnoses",
    foreignKeys = [
        ForeignKey(
            entity = FieldEntity::class,
            parentColumns = ["id"],
            childColumns = ["field_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["field_id"]), Index(value = ["created_at"])]
)
data class PhotoDiagnosisEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "field_id")
    val fieldId: String? = null,
    
    @ColumnInfo(name = "image_path")
    val imagePath: String, // Local file path to the captured image
    
    @ColumnInfo(name = "crop_type")
    val cropType: String? = null,
    
    @ColumnInfo(name = "diagnosis_label")
    val diagnosisLabel: String, // HEALTHY, DISEASE_NAME, UNKNOWN
    
    @ColumnInfo(name = "confidence")
    val confidence: Float, // 0.0 to 1.0
    
    @ColumnInfo(name = "disease_category")
    val diseaseCategory: String? = null, // FUNGAL, BACTERIAL, VIRAL, PEST, NUTRIENT_DEFICIENCY
    
    @ColumnInfo(name = "recommendation")
    val recommendation: String? = null,
    
    @ColumnInfo(name = "severity")
    val severity: String? = null, // MILD, MODERATE, SEVERE
    
    @ColumnInfo(name = "model_version")
    val modelVersion: String, // Track which ML model version was used
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
