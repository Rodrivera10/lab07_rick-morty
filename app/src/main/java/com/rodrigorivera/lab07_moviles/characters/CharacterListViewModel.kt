package com.rodrigorivera.lab07_moviles.characters
import android.app.Application
import androidx.lifecycle.*
import androidx.navigation.toRoute
import com.rodrigorivera.lab07_moviles.data.AppContainer
import com.rodrigorivera.lab07_moviles.data.Character
import com.rodrigorivera.lab07_moviles.navigation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
data class CharacterListState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val favoritesOnly: Boolean = false
)


class CharacterListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppContainer.get(application).characters
    private val _state = MutableStateFlow(CharacterListState())
    val state = _state.asStateFlow()
    private var loadJob: Job? = null

    init { loadCharacters() }
    fun loadCharacters() {
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

    fun toggleFavorite(id: Int) { viewModelScope.launch {
        try { repository.toggleFavorite(id) } catch (e: CancellationException) { throw e }
        catch (e: Exception) { _state.value = _state.value.copy(hasError = true) }
    } }
    fun create(character: Character) { viewModelScope.launch { repository.create(character) } }
    fun toggleFilter() { _state.value = _state.value.copy(favoritesOnly = !_state.value.favoritesOnly) }

}
