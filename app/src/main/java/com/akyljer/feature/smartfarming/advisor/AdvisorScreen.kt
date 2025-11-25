package com.akyljer.feature.smartfarming.advisor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akyljer.core.model.CropTask
import com.akyljer.core.model.RiskFlag

@Composable
fun AdvisorScreen(viewModel: AdvisorViewModel) {
    val state = viewModel.state.collectAsState().value
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text(text = "Crop Advisor", modifier = Modifier.padding(bottom = 12.dp))
            AdvisorForm(state, onInputChange = { viewModel.updateInput(it) }) {
                viewModel.submit()
            }
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 12.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Tasks")
        }
        items(state.tasks) { task ->
            TaskCard(task)
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Risk Flags")
        }
        items(state.risks) { risk ->
            RiskCard(risk)
        }
    }
}

@Composable
private fun AdvisorForm(
    state: AdvisorUiState,
    onInputChange: (builder: com.akyljer.core.model.FarmerInput.() -> com.akyljer.core.model.FarmerInput) -> Unit,
    onSubmit: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = state.input.crop,
            onValueChange = { onInputChange { copy(crop = it) } },
            label = { Text("Crop") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.input.growthStage,
            onValueChange = { onInputChange { copy(growthStage = it) } },
            label = { Text("Growth stage") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        OutlinedTextField(
            value = state.input.location,
            onValueChange = { onInputChange { copy(location = it) } },
            label = { Text("Location (optional)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        OutlinedTextField(
            value = state.input.notes,
            onValueChange = { onInputChange { copy(notes = it) } },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Button(
            onClick = onSubmit,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text(text = "Generate tasks")
        }
    }
}

@Composable
private fun TaskCard(task: CropTask) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = task.title)
            Text(text = task.description)
            Text(text = "Risk: ${task.riskLevel}")
        }
    }
}

@Composable
private fun RiskCard(risk: RiskFlag) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = risk.label)
            Text(text = risk.details)
            Text(text = "Level: ${risk.level}")
        }
    }
}
