package com.akyljer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.akyljer.data.local.entity.PhotoDiagnosisEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Photo Diagnosis operations (Photo Doctor)
 * 
 * Manages photo diagnosis history from the Photo Doctor feature.
 * Tracks plant disease detection using TensorFlow Lite.
 */
@Dao
interface PhotoDiagnosisDao {
    
    /**
     * Get all photo diagnoses as Flow (reactive)
     */
    @Query("SELECT * FROM photo_diagnoses ORDER BY created_at DESC")
    fun getAllDiagnoses(): Flow<List<PhotoDiagnosisEntity>>
    
    /**
     * Get diagnoses for a specific field
     */
    @Query("SELECT * FROM photo_diagnoses WHERE field_id = :fieldId ORDER BY created_at DESC")
    fun getDiagnosesByField(fieldId: String): Flow<List<PhotoDiagnosisEntity>>
    
    /**
     * Get diagnoses by crop type
     */
    @Query("SELECT * FROM photo_diagnoses WHERE crop_type = :cropType ORDER BY created_at DESC")
    fun getDiagnosesByCropType(cropType: String): Flow<List<PhotoDiagnosisEntity>>
    
    /**
     * Get recent diagnoses (last N records)
     */
    @Query("SELECT * FROM photo_diagnoses ORDER BY created_at DESC LIMIT :limit")
    fun getRecentDiagnoses(limit: Int = 10): Flow<List<PhotoDiagnosisEntity>>
    
    /**
     * Get diagnoses with diseases (not healthy)
     */
    @Query("SELECT * FROM photo_diagnoses WHERE diagnosis_label != 'HEALTHY' AND diagnosis_label != 'UNKNOWN' ORDER BY created_at DESC")
    fun getDiseaseDetections(): Flow<List<PhotoDiagnosisEntity>>
    
    /**
     * Get high confidence diagnoses
     */
    @Query("SELECT * FROM photo_diagnoses WHERE confidence >= :minConfidence ORDER BY created_at DESC")
    fun getHighConfidenceDiagnoses(minConfidence: Float = 0.7f): Flow<List<PhotoDiagnosisEntity>>
    
    /**
     * Get diagnoses by severity
     */
    @Query("SELECT * FROM photo_diagnoses WHERE severity = :severity ORDER BY created_at DESC")
    fun getDiagnosesBySeverity(severity: String): Flow<List<PhotoDiagnosisEntity>>
    
    /**
     * Get diagnosis by ID as Flow
     */
    @Query("SELECT * FROM photo_diagnoses WHERE id = :diagnosisId")
    fun getDiagnosisById(diagnosisId: String): Flow<PhotoDiagnosisEntity?>
    
    /**
     * Get diagnosis by ID (one-time)
     */
    @Query("SELECT * FROM photo_diagnoses WHERE id = :diagnosisId")
    suspend fun getDiagnosisByIdOnce(diagnosisId: String): PhotoDiagnosisEntity?
    
    /**
     * Get latest diagnosis
     */
    @Query("SELECT * FROM photo_diagnoses ORDER BY created_at DESC LIMIT 1")
    suspend fun getLatestDiagnosis(): PhotoDiagnosisEntity?
    
    /**
     * Get latest diagnosis as Flow
     */
    @Query("SELECT * FROM photo_diagnoses ORDER BY created_at DESC LIMIT 1")
    fun getLatestDiagnosisFlow(): Flow<PhotoDiagnosisEntity?>
    
    /**
     * Insert new diagnosis
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnosis(diagnosis: PhotoDiagnosisEntity)
    
    /**
     * Insert multiple diagnoses
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnoses(diagnoses: List<PhotoDiagnosisEntity>)
    
    /**
     * Update diagnosis
     */
    @Update
    suspend fun updateDiagnosis(diagnosis: PhotoDiagnosisEntity)
    
    /**
     * Delete diagnosis
     */
    @Delete
    suspend fun deleteDiagnosis(diagnosis: PhotoDiagnosisEntity)
    
    /**
     * Delete diagnosis by ID
     */
    @Query("DELETE FROM photo_diagnoses WHERE id = :diagnosisId")
    suspend fun deleteDiagnosisById(diagnosisId: String)
    
    /**
     * Delete old diagnoses (older than specified time)
     */
    @Query("DELETE FROM photo_diagnoses WHERE created_at < :olderThan")
    suspend fun deleteOldDiagnoses(olderThan: Long)
    
    /**
     * Get diagnosis count
     */
    @Query("SELECT COUNT(*) FROM photo_diagnoses")
    suspend fun getDiagnosisCount(): Int
    
    /**
     * Get disease detection count (not healthy or unknown)
     */
    @Query("SELECT COUNT(*) FROM photo_diagnoses WHERE diagnosis_label != 'HEALTHY' AND diagnosis_label != 'UNKNOWN'")
    suspend fun getDiseaseDetectionCount(): Int
    
    /**
     * Delete all diagnoses (for testing)
     */
    @Query("DELETE FROM photo_diagnoses")
    suspend fun deleteAllDiagnoses()
}
