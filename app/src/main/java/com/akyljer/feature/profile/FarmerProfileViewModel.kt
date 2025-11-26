package com.akyljer.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FarmerProfileState(
    val name: String = "",
    val phone: String = "",
    val location: String = "",
    val farmSize: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class FarmerProfileViewModel @Inject constructor(
    // TODO: Inject FarmerRepository when available
) : ViewModel() {

    private val _state = MutableStateFlow(FarmerProfileState())
    val state: StateFlow<FarmerProfileState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            // TODO: Load from repository/Room
            // For now, use placeholder data
            _state.value = _state.value.copy(
                name = "Demo Farmer",
                phone = "+996 XXX XXX XXX",
                location = "Issyk-Kul",
                farmSize = "10"
            )
        }
    }

    fun updateName(name: String) {
        _state.value = _state.value.copy(name = name, saveSuccess = false)
    }

    fun updatePhone(phone: String) {
        _state.value = _state.value.copy(phone = phone, saveSuccess = false)
    }

    fun updateLocation(location: String) {
        _state.value = _state.value.copy(location = location, saveSuccess = false)
    }

    fun updateFarmSize(size: String) {
        _state.value = _state.value.copy(farmSize = size, saveSuccess = false)
    }

    fun saveProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, saveSuccess = false)
            
            // TODO: Save to repository/Room
            kotlinx.coroutines.delay(500) // Simulate network/DB operation
            
            _state.value = _state.value.copy(isSaving = false, saveSuccess = true)
        }
    }
}
