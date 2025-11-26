package com.akyljer.data.repository

import com.akyljer.data.local.entity.FarmerProfileEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Farmer Profile operations
 * 
 * Provides abstraction layer between data sources and ViewModels.
 * Currently uses Room as the single source of truth.
 * 
 * Future: Can add remote data source for cloud sync.
 */
interface FarmerRepository {
    
    /**
     * Get current farmer profile as Flow (reactive)
     */
    fun getProfile(): Flow<FarmerProfileEntity?>
    
    /**
     * Get profile by ID
     */
    suspend fun getProfileById(profileId: String): FarmerProfileEntity?
    
    /**
     * Save or update farmer profile
     */
    suspend fun saveProfile(profile: FarmerProfileEntity)
    
    /**
     * Update existing profile
     */
    suspend fun updateProfile(profile: FarmerProfileEntity)
    
    /**
     * Delete profile
     */
    suspend fun deleteProfile(profile: FarmerProfileEntity)
    
    /**
     * Check if profile exists
     */
    suspend fun hasProfile(): Boolean
    
    /**
     * Create default profile for first-time users
     */
    suspend fun createDefaultProfile(): FarmerProfileEntity
}
