package com.rodrigorivera.lab07_moviles.locations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rodrigorivera.lab07_moviles.navigation.LocationDetailRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationDetailState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val locationId: Int = savedStateHandle.toRoute<LocationDetailRoute>().id

    private val _state = MutableStateFlow(LocationDetailState())
    val state: StateFlow<LocationDetailState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocation()
    }

    fun loadLocation() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = LocationDetailState(isLoading = true)
            delay(2000)
            _state.value = LocationDetailState(
                isLoading = false,
                data = LocationDb().getLocationById(locationId)
            )
        }
    }

    fun onLoadingClick() {
        if (_state.value.isLoading) {
            loadJob?.cancel()
            _state.value = LocationDetailState(isLoading = false, hasError = true)
        }
    }
}