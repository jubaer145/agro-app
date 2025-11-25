package com.akyljer.feature.alerts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AlertsScreen(viewModel: AlertsViewModel) {
    val state = viewModel.state.collectAsState().value
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Alerts / Tasks", style = MaterialTheme.typography.titleLarge)
        Text(text = "Tasks", modifier = Modifier.padding(top = 12.dp))
        LazyColumn {
            items(state.tasks) { task ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(task.title)
                        Text(task.description)
                        Text("Risk: ${task.riskLevel}")
                    }
                }
            }
        }
        Text(text = "Recent diagnoses", modifier = Modifier.padding(top = 12.dp))
        LazyColumn {
            items(state.diagnoses) { diagnosis ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(diagnosis.label)
                        Text("Confidence: ${(diagnosis.confidence * 100).toInt()}%")
                        Text(diagnosis.recommendation)
                    }
                }
            }
        }
    }
}
