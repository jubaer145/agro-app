package com.akyljer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.akyljer.data.local.entity.FarmerProfileEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Farmer Profile operations
 * 
 * Provides CRUD operations for farmer profile data.
 * Uses Flow for reactive queries.
 */
@Dao
interface FarmerProfileDao {
    
    /**
     * Get the current farmer profile (assumes single user per device)
     */
    @Query("SELECT * FROM farmer_profiles LIMIT 1")
    fun getProfile(): Flow<FarmerProfileEntity?>
    
    /**
     * Get profile by ID (for future multi-user support)
     */
    @Query("SELECT * FROM farmer_profiles WHERE id = :profileId")
    suspend fun getProfileById(profileId: String): FarmerProfileEntity?
    
    /**
     * Get profile by ID as Flow
     */
    @Query("SELECT * FROM farmer_profiles WHERE id = :profileId")
    fun getProfileByIdFlow(profileId: String): Flow<FarmerProfileEntity?>
    
    /**
     * Insert or replace farmer profile
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: FarmerProfileEntity)
    
    /**
     * Update existing profile
     */
    @Update
    suspend fun updateProfile(profile: FarmerProfileEntity)
    
    /**
     * Delete profile
     */
    @Delete
    suspend fun deleteProfile(profile: FarmerProfileEntity)
    
    /**
     * Delete all profiles (for testing/reset)
     */
    @Query("DELETE FROM farmer_profiles")
    suspend fun deleteAllProfiles()
    
    /**
     * Check if any profile exists
     */
    @Query("SELECT COUNT(*) FROM farmer_profiles")
    suspend fun getProfileCount(): Int
    
    /**
     * Get all profiles (for future multi-user support)
     */
    @Query("SELECT * FROM farmer_profiles ORDER BY created_at DESC")
    fun getAllProfiles(): Flow<List<FarmerProfileEntity>>
}
