package com.akyljer.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.akyljer.feature.agrovet.AgroVetScreen
import com.akyljer.feature.alerts.AlertsScreen
import com.akyljer.feature.dashboard.DashboardScreen
import com.akyljer.feature.photodoctor.PhotoDoctorScreen
import com.akyljer.feature.smartfarming.advisor.AdvisorScreen
import com.akyljer.feature.weather.WeatherScreen

sealed class Destinations(val route: String) {
    data object Dashboard : Destinations("dashboard")
    data object Advisor : Destinations("advisor")
    data object PhotoDoctor : Destinations("photo_doctor")
    data object Weather : Destinations("weather")
    data object AgroVet : Destinations("agrovet")
    data object Alerts : Destinations("alerts")
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Destinations.Dashboard.route) {
        composable(Destinations.Dashboard.route) {
            DashboardScreen(
                onNavigate = { navController.navigate(it) }
            )
        }
        composable(Destinations.Advisor.route) {
            AdvisorScreen(viewModel = hiltViewModel())
        }
        composable(Destinations.PhotoDoctor.route) {
            PhotoDoctorScreen(viewModel = hiltViewModel())
        }
        composable(Destinations.Weather.route) {
            WeatherScreen(viewModel = hiltViewModel())
        }
        composable(Destinations.AgroVet.route) {
            AgroVetScreen(viewModel = hiltViewModel())
        }
        composable(Destinations.Alerts.route) {
            AlertsScreen(viewModel = hiltViewModel())
        }
    }
}
