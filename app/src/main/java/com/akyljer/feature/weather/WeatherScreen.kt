package com.akyljer.feature.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val state = viewModel.state.collectAsState().value
    LaunchedEffect(Unit) { viewModel.refresh() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = "Weather & Risk", style = MaterialTheme.typography.titleLarge)
        Button(onClick = { viewModel.refresh() }, modifier = Modifier.padding(vertical = 12.dp)) {
            Text("Refresh")
        }
        if (state.isLoading) {
            CircularProgressIndicator()
        }
        state.error?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        state.forecast?.let { forecast ->
            WeatherCard(state = forecast)
        } ?: Text("No forecast yet")
    }
}

@Composable
private fun WeatherCard(state: com.akyljer.core.model.WeatherForecast) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Location: ${state.location}")
            Text("Min/Max: ${state.minTempC}°C / ${state.maxTempC}°C")
            Text("Precip: ${state.precipitationMm}mm, Humidity: ${state.humidity}%")
            Text("Summary: ${state.summary}")
        }
    }
}
