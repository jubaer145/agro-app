package com.akyljer.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akyljer.navigation.Destinations

@Composable
fun DashboardScreen(onNavigate: (String) -> Unit) {
    val entries = listOf(
        "👤 Farmer Profile" to Destinations.FarmerProfile.route,
        "🌾 My Fields" to Destinations.FieldsList.route,
        "🧠 Smart Farming" to Destinations.SmartFarming.route,
        "🌦️ Weather & Risk" to Destinations.Weather.route,
        "🐄 AgroVet" to Destinations.AgroVet.route,
        "🔔 Alerts / Tasks" to Destinations.Alerts.route,
        "⚙️ Settings" to Destinations.Settings.route
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Акыл Жер",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Smart farming tools for Kyrgyz farmers",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        items(entries.size) { index ->
            val entry = entries[index]
            DashboardCard(title = entry.first, onClick = { onNavigate(entry.second) })
        }
    }
}

@Composable
private fun DashboardCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Open",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
