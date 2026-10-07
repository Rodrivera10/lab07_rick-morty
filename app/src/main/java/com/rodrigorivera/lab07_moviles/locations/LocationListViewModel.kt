package com.rodrigorivera.lab07_moviles.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationListState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationListViewModel : ViewModel() {

    private val _state = MutableStateFlow(LocationListState())
    val state: StateFlow<LocationListState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocations()
    }

    fun loadLocations() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = LocationListState(isLoading = true)
            delay(4000)
            _state.value = LocationListState(
                isLoading = false,
                data = LocationDb().getAllLocations()
            )
        }
    }

    fun onLoadingClick() {
        if (_state.value.isLoading) {
            loadJob?.cancel()
            _state.value = LocationListState(isLoading = false, hasError = true)
        }
    }
}