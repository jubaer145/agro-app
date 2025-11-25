package com.akyljer.feature.agrovet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AgroVetScreen(viewModel: AgroVetViewModel) {
    val state = viewModel.state.collectAsState().value
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "AgroVet Triage", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = state.symptoms,
            onValueChange = { viewModel.updateSymptoms(it) },
            label = { Text("Symptoms (comma separated)") },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        )
        Button(onClick = { viewModel.submit() }, modifier = Modifier.padding(top = 12.dp)) {
            Text("Get advice")
        }
        state.answer?.let { answer ->
            Card(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Condition: ${answer.condition}")
                    Text("Urgency: ${answer.urgency}")
                    Text(answer.recommendation)
                }
            }
        }
    }
}
