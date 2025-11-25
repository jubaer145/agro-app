package com.akyljer.feature.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akyljer.core.model.WeatherForecast
import com.akyljer.core.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val forecast: WeatherForecast? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherUiState())
    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.forecast.collect { forecast ->
                _state.update { it.copy(forecast = forecast) }
            }
        }
    }

    fun refresh(lat: Double = 42.8746, lon: Double = 74.5698) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { repository.refresh(lat, lon) }
                .onFailure { throwable ->
                    _state.update { it.copy(error = throwable.message) }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }
}
