package com.akyljer.feature.fields

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FieldDetailState(
    val name: String = "",
    val cropType: String = "",
    val size: String = "",
    val location: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class FieldDetailViewModel @Inject constructor(
    // TODO: Inject FieldRepository when available
) : ViewModel() {

    private val _state = MutableStateFlow(FieldDetailState())
    val state: StateFlow<FieldDetailState> = _state.asStateFlow()

    fun loadField(fieldId: String) {
        if (fieldId == "new") {
            // New field - start with empty state
            _state.value = FieldDetailState()
        } else {
            // Load existing field
            viewModelScope.launch {
                // TODO: Load from repository/Room
                // Placeholder data for demo
                _state.value = FieldDetailState(
                    name = "Demo Field $fieldId",
                    cropType = "Wheat",
                    size = "5.5",
                    location = "42.8746° N, 74.5698° E"
                )
            }
        }
    }

    fun updateName(name: String) {
        _state.value = _state.value.copy(name = name, saveSuccess = false)
    }

    fun updateCropType(cropType: String) {
        _state.value = _state.value.copy(cropType = cropType, saveSuccess = false)
    }

    fun updateSize(size: String) {
        _state.value = _state.value.copy(size = size, saveSuccess = false)
    }

    fun updateLocation(location: String) {
        _state.value = _state.value.copy(location = location, saveSuccess = false)
    }

    fun saveField() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, saveSuccess = false)
            
            // TODO: Save to repository/Room
            kotlinx.coroutines.delay(500) // Simulate network/DB operation
            
            _state.value = _state.value.copy(isSaving = false, saveSuccess = true)
        }
    }
}
