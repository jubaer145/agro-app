package com.akyljer.navigation

/**
 * Navigation Test - Verification that all routes compile
 * 
 * This file documents all implemented routes and their usage.
 * Can be used as a reference for navigation throughout the app.
 */

object NavigationTest {
    
    /**
     * All available routes in the app:
     * 
     * 1. Dashboard - Main entry point
     * 2. FarmerProfile - User profile management
     * 3. FieldsList - List all fields
     * 4. FieldDetail - Add/edit field (takes fieldId parameter)
     * 5. SmartFarming - Hub for AI features
     * 6. Advisor - Crop recommendations
     * 7. PhotoDoctor - Disease detection
     * 8. Weather - Weather and risk info
     * 9. AgroVet - Animal health triage
     * 10. Alerts - Tasks and notifications
     * 11. Settings - App configuration
     */
    
    fun getAllRoutes(): List<String> = listOf(
        Destinations.Dashboard.route,
        Destinations.FarmerProfile.route,
        Destinations.FieldsList.route,
        Destinations.FieldDetail.route,
        Destinations.SmartFarming.route,
        Destinations.Advisor.route,
        Destinations.PhotoDoctor.route,
        Destinations.Weather.route,
        Destinations.AgroVet.route,
        Destinations.Alerts.route,
        Destinations.Settings.route
    )
    
    /**
     * Example usage for parameterized routes:
     * - New field: Destinations.FieldDetail.createRoute("new")
     * - Edit field: Destinations.FieldDetail.createRoute(fieldId)
     */
    fun exampleParameterizedRoutes(): List<String> = listOf(
        Destinations.FieldDetail.createRoute("new"),
        Destinations.FieldDetail.createRoute("1"),
        Destinations.FieldDetail.createRoute("123")
    )
}
