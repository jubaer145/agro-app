package com.akyljer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.akyljer.data.local.entity.FieldEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Field operations
 * 
 * Provides CRUD operations for field data.
 * Supports querying by farmer and reactive updates.
 */
@Dao
interface FieldDao {
    
    /**
     * Get all fields for a farmer as Flow (reactive)
     */
    @Query("SELECT * FROM fields WHERE farmer_id = :farmerId ORDER BY created_at DESC")
    fun getFieldsByFarmer(farmerId: String): Flow<List<FieldEntity>>
    
    /**
     * Get all fields for a farmer (one-time)
     */
    @Query("SELECT * FROM fields WHERE farmer_id = :farmerId ORDER BY created_at DESC")
    suspend fun getFieldsByFarmerOnce(farmerId: String): List<FieldEntity>
    
    /**
     * Get field by ID as Flow
     */
    @Query("SELECT * FROM fields WHERE id = :fieldId")
    fun getFieldById(fieldId: String): Flow<FieldEntity?>
    
    /**
     * Get field by ID (one-time)
     */
    @Query("SELECT * FROM fields WHERE id = :fieldId")
    suspend fun getFieldByIdOnce(fieldId: String): FieldEntity?
    
    /**
     * Get all fields (for admin/overview)
     */
    @Query("SELECT * FROM fields ORDER BY created_at DESC")
    fun getAllFields(): Flow<List<FieldEntity>>
    
    /**
     * Get fields by crop type
     */
    @Query("SELECT * FROM fields WHERE crop_type = :cropType ORDER BY created_at DESC")
    fun getFieldsByCropType(cropType: String): Flow<List<FieldEntity>>
    
    /**
     * Insert new field
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertField(field: FieldEntity)
    
    /**
     * Insert multiple fields
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFields(fields: List<FieldEntity>)
    
    /**
     * Update existing field
     */
    @Update
    suspend fun updateField(field: FieldEntity)
    
    /**
     * Delete field
     */
    @Delete
    suspend fun deleteField(field: FieldEntity)
    
    /**
     * Delete field by ID
     */
    @Query("DELETE FROM fields WHERE id = :fieldId")
    suspend fun deleteFieldById(fieldId: String)
    
    /**
     * Get total field area for a farmer
     */
    @Query("SELECT SUM(size_hectares) FROM fields WHERE farmer_id = :farmerId")
    suspend fun getTotalFieldArea(farmerId: String): Double?
    
    /**
     * Get field count for a farmer
     */
    @Query("SELECT COUNT(*) FROM fields WHERE farmer_id = :farmerId")
    suspend fun getFieldCount(farmerId: String): Int
    
    /**
     * Delete all fields (for testing)
     */
    @Query("DELETE FROM fields")
    suspend fun deleteAllFields()
}
