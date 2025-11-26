package com.akyljer.data.repository

import com.akyljer.data.local.entity.FieldEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Field operations
 * 
 * Manages field data with Room as local storage.
 * 
 * Future integrations:
 * - Remote API for field data sync
 * - GPS/location services for field boundaries
 * - Weather data per field location
 */
interface FieldRepository {
    
    /**
     * Get all fields for a farmer as Flow (reactive)
     */
    fun getFieldsByFarmer(farmerId: String): Flow<List<FieldEntity>>
    
    /**
     * Get single field by ID as Flow
     */
    fun getFieldById(fieldId: String): Flow<FieldEntity?>
    
    /**
     * Get single field by ID (one-time)
     */
    suspend fun getFieldByIdOnce(fieldId: String): FieldEntity?
    
    /**
     * Get all fields
     */
    fun getAllFields(): Flow<List<FieldEntity>>
    
    /**
     * Save new field or update existing
     */
    suspend fun saveField(field: FieldEntity)
    
    /**
     * Update existing field
     */
    suspend fun updateField(field: FieldEntity)
    
    /**
     * Delete field
     */
    suspend fun deleteField(fieldId: String)
    
    /**
     * Get total field area for a farmer
     */
    suspend fun getTotalFieldArea(farmerId: String): Double
    
    /**
     * Get field count for a farmer
     */
    suspend fun getFieldCount(farmerId: String): Int
    
    /**
     * Check if farmer has any fields
     */
    suspend fun hasFields(farmerId: String): Boolean
    
    /**
     * Create demo fields for first-time users
     */
    suspend fun createDemoFields(farmerId: String): List<FieldEntity>
}
