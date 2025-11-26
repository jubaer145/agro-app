package com.akyljer.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.akyljer.feature.agrovet.AgroVetScreen
import com.akyljer.feature.alerts.AlertsScreen
import com.akyljer.feature.dashboard.DashboardScreen
import com.akyljer.feature.fields.FieldDetailScreen
import com.akyljer.feature.fields.FieldsListScreen
import com.akyljer.feature.photodoctor.PhotoDoctorScreen
import com.akyljer.feature.profile.FarmerProfileScreen
import com.akyljer.feature.settings.SettingsScreen
import com.akyljer.feature.smartfarming.SmartFarmingScreen
import com.akyljer.feature.smartfarming.advisor.AdvisorScreen
import com.akyljer.feature.weather.WeatherScreen

/**
 * Navigation routes for the Акыл Жер MVP app.
 * 
 * Route structure:
 * - Dashboard: Main hub for navigation
 * - Profile: Farmer profile management
 * - Fields: List and detail views for farmer's fields
 * - SmartFarming: Hub for Crop Advisor and Photo Doctor
 * - Weather: Weather and risk intelligence
 * - AgroVet: Animal health triage
 * - Alerts: Aggregated tasks and alerts
 * - Settings: App settings
 */
sealed class Destinations(val route: String) {
    data object Dashboard : Destinations("dashboard")
    data object FarmerProfile : Destinations("farmer_profile")
    data object FieldsList : Destinations("fields_list")
    data object FieldDetail : Destinations("field_detail/{fieldId}") {
        fun createRoute(fieldId: String) = "field_detail/$fieldId"
    }
    data object SmartFarming : Destinations("smart_farming")
    data object Advisor : Destinations("advisor")
    data object PhotoDoctor : Destinations("photo_doctor")
    data object Weather : Destinations("weather")
    data object AgroVet : Destinations("agrovet")
    data object Alerts : Destinations("alerts")
    data object Settings : Destinations("settings")
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Destinations.Dashboard.route) {
        // Main Dashboard
        composable(Destinations.Dashboard.route) {
            DashboardScreen(
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        // Farmer Profile
        composable(Destinations.FarmerProfile.route) {
            FarmerProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                viewModel = hiltViewModel()
            )
        }

        // Fields Management
        composable(Destinations.FieldsList.route) {
            FieldsListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { fieldId ->
                    navController.navigate(Destinations.FieldDetail.createRoute(fieldId))
                },
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = Destinations.FieldDetail.route,
            arguments = listOf(
                navArgument("fieldId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val fieldId = backStackEntry.arguments?.getString("fieldId") ?: "new"
            FieldDetailScreen(
                fieldId = fieldId,
                onNavigateBack = { navController.popBackStack() },
                viewModel = hiltViewModel()
            )
        }

        // Smart Farming Hub
        composable(Destinations.SmartFarming.route) {
            SmartFarmingScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAdvisor = { navController.navigate(Destinations.Advisor.route) },
                onNavigateToPhotoDoctor = { navController.navigate(Destinations.PhotoDoctor.route) }
            )
        }

        // Crop Advisor
        composable(Destinations.Advisor.route) {
            AdvisorScreen(viewModel = hiltViewModel())
        }

        // Photo Doctor
        composable(Destinations.PhotoDoctor.route) {
            PhotoDoctorScreen(viewModel = hiltViewModel())
        }

        // Weather & Risk
        composable(Destinations.Weather.route) {
            WeatherScreen(viewModel = hiltViewModel())
        }

        // AgroVet
        composable(Destinations.AgroVet.route) {
            AgroVetScreen(viewModel = hiltViewModel())
        }

        // Alerts / Tasks
        composable(Destinations.Alerts.route) {
            AlertsScreen(viewModel = hiltViewModel())
        }

        // Settings
        composable(Destinations.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
