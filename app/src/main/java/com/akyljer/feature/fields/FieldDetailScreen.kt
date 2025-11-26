package com.akyljer.feature.fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Field Detail Screen
 * 
 * Add or edit a field.
 * If fieldId == "new", creates a new field.
 * Otherwise, loads and edits existing field.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldDetailScreen(
    fieldId: String,
    onNavigateBack: () -> Unit,
    viewModel: FieldDetailViewModel
) {
    val state by viewModel.state.collectAsState()
    val isNewField = fieldId == "new"

    LaunchedEffect(fieldId) {
        viewModel.loadField(fieldId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNewField) "Add Field" else "Edit Field") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = if (isNewField) "Create a new field" else "Edit field details",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Field Name") },
                placeholder = { Text("e.g., North Field") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.cropType,
                onValueChange = { viewModel.updateCropType(it) },
                label = { Text("Crop Type") },
                placeholder = { Text("e.g., Wheat, Barley, Potatoes") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.size,
                onValueChange = { viewModel.updateSize(it) },
                label = { Text("Size (hectares)") },
                placeholder = { Text("e.g., 5.5") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.location,
                onValueChange = { viewModel.updateLocation(it) },
                label = { Text("Location (optional)") },
                placeholder = { Text("GPS coordinates or description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.saveField()
                    if (state.saveSuccess) {
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving && state.name.isNotBlank()
            ) {
                Text(if (state.isSaving) "Saving..." else "Save Field")
            }

            if (state.saveSuccess) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "✓ Field saved successfully",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
