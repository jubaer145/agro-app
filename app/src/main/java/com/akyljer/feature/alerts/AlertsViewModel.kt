package com.akyljer.feature.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akyljer.core.model.CropTask
import com.akyljer.core.model.Diagnosis
import com.akyljer.core.repository.DiagnosisRepository
import com.akyljer.core.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AlertsUiState(
    val tasks: List<CropTask> = emptyList(),
    val diagnoses: List<Diagnosis> = emptyList()
)

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val diagnosisRepository: DiagnosisRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AlertsUiState())
    val state: StateFlow<AlertsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                taskRepository.tasks,
                diagnosisRepository.history
            ) { tasks, diagnoses -> tasks to diagnoses }
                .collect { (tasks, diagnoses) ->
                    _state.update { it.copy(tasks = tasks, diagnoses = diagnoses) }
                }
        }
    }
}
