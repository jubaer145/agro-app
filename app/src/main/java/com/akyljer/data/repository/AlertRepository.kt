package com.akyljer.data.repository

import com.akyljer.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Alert operations
 * 
 * Aggregates alerts from multiple sources:
 * - Weather API alerts
 * - Photo Doctor disease detection alerts
 * - Crop Advisor risk alerts
 * - System notifications
 * 
 * Future integrations:
 * - Push notifications
 * - SMS alerts for critical issues
 * - Weather API integration
 * - IoT sensor alerts (when available)
 */
interface AlertRepository {
    
    /**
     * Get all active alerts as Flow (reactive)
     */
    fun getAllAlerts(): Flow<List<AlertEntity>>
    
    /**
     * Get unread alerts
     */
    fun getUnreadAlerts(): Flow<List<AlertEntity>>
    
    /**
     * Get high severity alerts (HIGH, CRITICAL)
     */
    fun getHighSeverityAlerts(): Flow<List<AlertEntity>>
    
    /**
     * Get alerts requiring action
     */
    fun getAlertsRequiringAction(): Flow<List<AlertEntity>>
    
    /**
     * Get alerts for a specific field
     */
    fun getAlertsByField(fieldId: String): Flow<List<AlertEntity>>
    
    /**
     * Get unread alert count as Flow
     */
    fun getUnreadAlertCount(): Flow<Int>
    
    /**
     * Create new alert
     */
    suspend fun createAlert(alert: AlertEntity)
    
    /**
     * Mark alert as read
     */
    suspend fun markAlertRead(alertId: String)
    
    /**
     * Mark all alerts as read
     */
    suspend fun markAllAlertsRead()
    
    /**
     * Dismiss alert
     */
    suspend fun dismissAlert(alertId: String)
    
    /**
     * Delete alert
     */
    suspend fun deleteAlert(alertId: String)
    
    /**
     * Clean up old/expired alerts
     */
    suspend fun cleanupExpiredAlerts()
    
    /**
     * Create demo alerts for first-time users
     */
    suspend fun createDemoAlerts(fieldId: String? = null): List<AlertEntity>
    
    /**
     * Generate weather alert (placeholder for future API integration)
     * 
     * @param fieldId Field to create alert for
     * @param weatherCondition Weather condition (e.g., "HEAVY_RAIN", "FROST", "DROUGHT")
     * @return Created alert
     */
    suspend fun generateWeatherAlert(fieldId: String, weatherCondition: String): AlertEntity
    
    /**
     * Generate disease alert from Photo Doctor
     * 
     * @param fieldId Field where disease detected
     * @param diseaseName Detected disease
     * @param severity Disease severity
     * @return Created alert
     */
    suspend fun generateDiseaseAlert(
        fieldId: String,
        diseaseName: String,
        severity: String
    ): AlertEntity
}
