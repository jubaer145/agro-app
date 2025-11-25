package com.akyljer.feature.smartfarming.advisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akyljer.core.model.CropTask
import com.akyljer.core.model.FarmerInput
import com.akyljer.core.model.RiskFlag
import com.akyljer.core.repository.AdvisorRepository
import com.akyljer.core.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdvisorUiState(
    val input: FarmerInput = FarmerInput(),
    val tasks: List<CropTask> = emptyList(),
    val risks: List<RiskFlag> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class AdvisorViewModel @Inject constructor(
    private val advisorRepository: AdvisorRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdvisorUiState())
    val state: StateFlow<AdvisorUiState> = _state.asStateFlow()

    fun updateInput(builder: FarmerInput.() -> FarmerInput) {
        _state.update { current -> current.copy(input = current.input.builder()) }
    }

    fun submit() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val weather = weatherRepository.forecast.firstOrNull()
            val result = advisorRepository.generate(_state.value.input, weather)
            _state.update {
                it.copy(
                    tasks = result.tasks,
                    risks = result.advice.risks,
                    isLoading = false
                )
            }
        }
    }
}
