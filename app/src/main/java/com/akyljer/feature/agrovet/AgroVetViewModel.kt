package com.akyljer.feature.agrovet

import androidx.lifecycle.ViewModel
import com.akyljer.core.model.AgroVetAnswer
import com.akyljer.core.rules.AgroVetTriage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AgroVetUiState(
    val symptoms: String = "",
    val answer: AgroVetAnswer? = null
)

@HiltViewModel
class AgroVetViewModel @Inject constructor(
    private val triage: AgroVetTriage
) : ViewModel() {

    private val _state = MutableStateFlow(AgroVetUiState())
    val state: StateFlow<AgroVetUiState> = _state.asStateFlow()

    fun updateSymptoms(value: String) {
        _state.update { it.copy(symptoms = value) }
    }

    fun submit() {
        val symptomsList = _state.value.symptoms.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        val answer = triage.triage(symptomsList)
        _state.update { it.copy(answer = answer) }
    }
}
