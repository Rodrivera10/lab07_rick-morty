package com.rodrigorivera.lab07_moviles.characters
import android.app.Application
import androidx.lifecycle.*
import androidx.navigation.toRoute
import com.rodrigorivera.lab07_moviles.data.AppContainer
import com.rodrigorivera.lab07_moviles.data.Character
import com.rodrigorivera.lab07_moviles.navigation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
data class CharacterDetailState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)


class CharacterDetailViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val repository = AppContainer.get(application).characters
    private val _state = MutableStateFlow(CharacterDetailState())
    val state = _state.asStateFlow()
    private var loadJob: Job? = null
    private val source = repository.observeById(savedStateHandle.toRoute<CharacterDetailRoute>().id)
    init { loadCharacter() }
    fun loadCharacter() {
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

    fun toggleFavorite(id: Int) { viewModelScope.launch {
        try { repository.toggleFavorite(id) } catch (e: CancellationException) { throw e }
        catch (e: Exception) { _state.value = _state.value.copy(hasError = true) }
    } }

}
