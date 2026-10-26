package com.rodrigorivera.lab07_moviles.locations
import android.app.Application
import androidx.lifecycle.*
import androidx.navigation.toRoute
import com.rodrigorivera.lab07_moviles.data.AppContainer
import com.rodrigorivera.lab07_moviles.data.Character
import com.rodrigorivera.lab07_moviles.navigation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
data class LocationListState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)


class LocationListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppContainer.get(application).locations
    private val _state = MutableStateFlow(LocationListState())
    val state = _state.asStateFlow()
    private var loadJob: Job? = null

    init { loadLocations() }
    fun loadLocations() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, hasError = false)
            repository.observeAll()
                .catch { _state.value = _state.value.copy(isLoading = false, hasError = true) }
                .collect { _state.value = _state.value.copy(isLoading = false, data = it, hasError = false) }
        }
    }
    fun onLoadingClick() {
        if (_state.value.isLoading) { loadJob?.cancel(); _state.value = _state.value.copy(isLoading = false, hasError = true) }
    }

}
