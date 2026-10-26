package com.rodrigorivera.lab07_moviles.locations
import android.app.Application
import androidx.lifecycle.*
import androidx.navigation.toRoute
import com.rodrigorivera.lab07_moviles.data.AppContainer
import com.rodrigorivera.lab07_moviles.data.Character
import com.rodrigorivera.lab07_moviles.navigation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
data class LocationDetailState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)


class LocationDetailViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val repository = AppContainer.get(application).locations
    private val _state = MutableStateFlow(LocationDetailState())
    val state = _state.asStateFlow()
    private var loadJob: Job? = null
    private val source = repository.observeById(savedStateHandle.toRoute<LocationDetailRoute>().id)
    init { loadLocation() }
    fun loadLocation() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, hasError = false)
            source
                .catch { _state.value = _state.value.copy(isLoading = false, hasError = true) }
                .collect { _state.value = _state.value.copy(isLoading = false, data = it, hasError = it == null) }
        }
    }
    fun onLoadingClick() {
        if (_state.value.isLoading) { loadJob?.cancel(); _state.value = _state.value.copy(isLoading = false, hasError = true) }
    }

}
