package com.akyljer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.akyljer.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Alert operations
 * 
 * Manages alerts from various sources (weather, diseases, tasks, etc.).
 * Supports filtering by read status, severity, and type.
 */
@Dao
interface AlertDao {
    
    /**
     * Get all alerts as Flow (reactive)
     */
    @Query("SELECT * FROM alerts WHERE is_dismissed = 0 ORDER BY created_at DESC")
    fun getAllAlerts(): Flow<List<AlertEntity>>
    
    /**
     * Get unread alerts
     */
    @Query("SELECT * FROM alerts WHERE is_read = 0 AND is_dismissed = 0 ORDER BY created_at DESC")
    fun getUnreadAlerts(): Flow<List<AlertEntity>>
    
    /**
     * Get read alerts
     */
    @Query("SELECT * FROM alerts WHERE is_read = 1 AND is_dismissed = 0 ORDER BY created_at DESC")
    fun getReadAlerts(): Flow<List<AlertEntity>>
    
    /**
     * Get alerts by severity
     */
    @Query("SELECT * FROM alerts WHERE severity = :severity AND is_dismissed = 0 ORDER BY created_at DESC")
    fun getAlertsBySeverity(severity: String): Flow<List<AlertEntity>>
    
    /**
     * Get critical and high severity alerts
     */
    @Query("SELECT * FROM alerts WHERE severity IN ('HIGH', 'CRITICAL') AND is_dismissed = 0 ORDER BY created_at DESC")
    fun getHighSeverityAlerts(): Flow<List<AlertEntity>>
    
    /**
     * Get alerts by type
     */
    @Query("SELECT * FROM alerts WHERE alert_type = :alertType AND is_dismissed = 0 ORDER BY created_at DESC")
    fun getAlertsByType(alertType: String): Flow<List<AlertEntity>>
    
    /**
     * Get alerts for a specific field
     */
    @Query("SELECT * FROM alerts WHERE field_id = :fieldId AND is_dismissed = 0 ORDER BY created_at DESC")
    fun getAlertsByField(fieldId: String): Flow<List<AlertEntity>>
    
    /**
     * Get alerts requiring action
     */
    @Query("SELECT * FROM alerts WHERE action_required = 1 AND is_dismissed = 0 ORDER BY created_at DESC")
    fun getAlertsRequiringAction(): Flow<List<AlertEntity>>
    
    /**
     * Get alert by ID as Flow
     */
    @Query("SELECT * FROM alerts WHERE id = :alertId")
    fun getAlertById(alertId: String): Flow<AlertEntity?>
    
    /**
     * Get alert by ID (one-time)
     */
    @Query("SELECT * FROM alerts WHERE id = :alertId")
    suspend fun getAlertByIdOnce(alertId: String): AlertEntity?
    
    /**
     * Insert new alert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity)
    
    /**
     * Insert multiple alerts
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerts(alerts: List<AlertEntity>)
    
    /**
     * Update alert
     */
    @Update
    suspend fun updateAlert(alert: AlertEntity)
    
    /**
     * Mark alert as read
     */
    @Query("UPDATE alerts SET is_read = 1 WHERE id = :alertId")
    suspend fun markAlertRead(alertId: String)
    
    /**
     * Mark all alerts as read
     */
    @Query("UPDATE alerts SET is_read = 1 WHERE is_read = 0")
    suspend fun markAllAlertsRead()
    
    /**
     * Dismiss alert
     */
    @Query("UPDATE alerts SET is_dismissed = 1 WHERE id = :alertId")
    suspend fun dismissAlert(alertId: String)
    
    /**
     * Delete alert
     */
    @Delete
    suspend fun deleteAlert(alert: AlertEntity)
    
    /**
     * Delete alert by ID
     */
    @Query("DELETE FROM alerts WHERE id = :alertId")
    suspend fun deleteAlertById(alertId: String)
    
    /**
     * Delete expired alerts
     */
    @Query("DELETE FROM alerts WHERE expires_at IS NOT NULL AND expires_at < :currentTime")
    suspend fun deleteExpiredAlerts(currentTime: Long = System.currentTimeMillis())
    
    /**
     * Delete dismissed alerts older than specified time
     */
    @Query("DELETE FROM alerts WHERE is_dismissed = 1 AND created_at < :olderThan")
    suspend fun deleteDismissedAlerts(olderThan: Long)
    
    /**
     * Get unread alert count
     */
    @Query("SELECT COUNT(*) FROM alerts WHERE is_read = 0 AND is_dismissed = 0")
    suspend fun getUnreadAlertCount(): Int
    
    /**
     * Get unread alert count as Flow
     */
    @Query("SELECT COUNT(*) FROM alerts WHERE is_read = 0 AND is_dismissed = 0")
    fun getUnreadAlertCountFlow(): Flow<Int>
    
    /**
     * Get critical alert count
     */
    @Query("SELECT COUNT(*) FROM alerts WHERE severity = 'CRITICAL' AND is_dismissed = 0")
    suspend fun getCriticalAlertCount(): Int
    
    /**
     * Delete all alerts (for testing)
     */
    @Query("DELETE FROM alerts")
    suspend fun deleteAllAlerts()
}
