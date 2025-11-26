package com.akyljer.feature.fields

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FieldsListState(
    val fields: List<FieldListItem> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class FieldsListViewModel @Inject constructor(
    // TODO: Inject FieldRepository when available
) : ViewModel() {

    private val _state = MutableStateFlow(FieldsListState())
    val state: StateFlow<FieldsListState> = _state.asStateFlow()

    init {
        loadFields()
    }

    private fun loadFields() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            // TODO: Load from repository/Room
            // Placeholder data
            val demoFields = listOf(
                FieldListItem(
                    id = "1",
                    name = "North Field",
                    cropType = "Wheat",
                    size = 5.5
                ),
                FieldListItem(
                    id = "2",
                    name = "South Field",
                    cropType = "Barley",
                    size = 3.2
                ),
                FieldListItem(
                    id = "3",
                    name = "East Field",
                    cropType = "Potatoes",
                    size = 2.0
                )
            )
            
            _state.value = _state.value.copy(
                fields = demoFields,
                isLoading = false
            )
        }
    }

    fun refresh() {
        loadFields()
    }
}
