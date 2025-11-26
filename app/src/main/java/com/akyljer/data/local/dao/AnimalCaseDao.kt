package com.akyljer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.akyljer.data.local.entity.AnimalCaseEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Animal Case operations (AgroVet)
 * 
 * Manages animal health cases for the AgroVet module.
 * Tracks symptoms, diagnoses, and treatment history.
 */
@Dao
interface AnimalCaseDao {
    
    /**
     * Get all animal cases as Flow (reactive)
     */
    @Query("SELECT * FROM animal_cases ORDER BY created_at DESC")
    fun getAllCases(): Flow<List<AnimalCaseEntity>>
    
    /**
     * Get cases by farmer
     */
    @Query("SELECT * FROM animal_cases WHERE farmer_id = :farmerId ORDER BY created_at DESC")
    fun getCasesByFarmer(farmerId: String): Flow<List<AnimalCaseEntity>>
    
    /**
     * Get active cases (not resolved)
     */
    @Query("SELECT * FROM animal_cases WHERE status != 'RESOLVED' ORDER BY created_at DESC")
    fun getActiveCases(): Flow<List<AnimalCaseEntity>>
    
    /**
     * Get active cases for a farmer
     */
    @Query("SELECT * FROM animal_cases WHERE farmer_id = :farmerId AND status != 'RESOLVED' ORDER BY created_at DESC")
    fun getActiveCasesByFarmer(farmerId: String): Flow<List<AnimalCaseEntity>>
    
    /**
     * Get cases by animal type
     */
    @Query("SELECT * FROM animal_cases WHERE animal_type = :animalType ORDER BY created_at DESC")
    fun getCasesByAnimalType(animalType: String): Flow<List<AnimalCaseEntity>>
    
    /**
     * Get cases by severity
     */
    @Query("SELECT * FROM animal_cases WHERE severity = :severity ORDER BY created_at DESC")
    fun getCasesBySeverity(severity: String): Flow<List<AnimalCaseEntity>>
    
    /**
     * Get emergency cases
     */
    @Query("SELECT * FROM animal_cases WHERE severity IN ('SEVERE', 'EMERGENCY') AND status != 'RESOLVED' ORDER BY created_at DESC")
    fun getEmergencyCases(): Flow<List<AnimalCaseEntity>>
    
    /**
     * Get case by ID as Flow
     */
    @Query("SELECT * FROM animal_cases WHERE id = :caseId")
    fun getCaseById(caseId: String): Flow<AnimalCaseEntity?>
    
    /**
     * Get case by ID (one-time)
     */
    @Query("SELECT * FROM animal_cases WHERE id = :caseId")
    suspend fun getCaseByIdOnce(caseId: String): AnimalCaseEntity?
    
    /**
     * Insert new case
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(animalCase: AnimalCaseEntity)
    
    /**
     * Insert multiple cases
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCases(cases: List<AnimalCaseEntity>)
    
    /**
     * Update case
     */
    @Update
    suspend fun updateCase(animalCase: AnimalCaseEntity)
    
    /**
     * Mark case as resolved
     */
    @Query("UPDATE animal_cases SET status = 'RESOLVED', resolved_at = :resolvedAt WHERE id = :caseId")
    suspend fun markCaseResolved(caseId: String, resolvedAt: Long = System.currentTimeMillis())
    
    /**
     * Update case status
     */
    @Query("UPDATE animal_cases SET status = :status, updated_at = :updatedAt WHERE id = :caseId")
    suspend fun updateCaseStatus(caseId: String, status: String, updatedAt: Long = System.currentTimeMillis())
    
    /**
     * Delete case
     */
    @Delete
    suspend fun deleteCase(animalCase: AnimalCaseEntity)
    
    /**
     * Delete case by ID
     */
    @Query("DELETE FROM animal_cases WHERE id = :caseId")
    suspend fun deleteCaseById(caseId: String)
    
    /**
     * Get active case count for a farmer
     */
    @Query("SELECT COUNT(*) FROM animal_cases WHERE farmer_id = :farmerId AND status != 'RESOLVED'")
    suspend fun getActiveCaseCount(farmerId: String): Int
    
    /**
     * Get emergency case count
     */
    @Query("SELECT COUNT(*) FROM animal_cases WHERE severity IN ('SEVERE', 'EMERGENCY') AND status != 'RESOLVED'")
    suspend fun getEmergencyCaseCount(): Int
    
    /**
     * Delete all cases (for testing)
     */
    @Query("DELETE FROM animal_cases")
    suspend fun deleteAllCases()
}
