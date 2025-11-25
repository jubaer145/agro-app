package com.akyljer.feature.photodoctor

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akyljer.core.cv.PhotoDoctorClassifier
import com.akyljer.core.model.Diagnosis
import com.akyljer.core.repository.DiagnosisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PhotoDoctorState(
    val latest: Diagnosis? = null,
    val history: List<Diagnosis> = emptyList(),
    val isProcessing: Boolean = false
)

@HiltViewModel
class PhotoDoctorViewModel @Inject constructor(
    private val classifier: PhotoDoctorClassifier,
    private val repository: DiagnosisRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PhotoDoctorState())
    val state: StateFlow<PhotoDoctorState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.history.collect { history ->
                _state.update { it.copy(history = history) }
            }
        }
    }

    fun analyze(bitmap: Bitmap) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }
            val diagnosis = classifier.analyze(bitmap)
            repository.save(diagnosis)
            _state.update { it.copy(latest = diagnosis, isProcessing = false) }
        }
    }
}
