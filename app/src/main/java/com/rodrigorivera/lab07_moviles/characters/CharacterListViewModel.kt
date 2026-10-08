package com.rodrigorivera.lab07_moviles.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigorivera.lab07_moviles.data.Character
import com.rodrigorivera.lab07_moviles.data.CharacterDb
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterListState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

class CharacterListViewModel : ViewModel() {

    private val _state = MutableStateFlow(CharacterListState())
    val state: StateFlow<CharacterListState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = CharacterListState(isLoading = true)
            delay(4000) // simula la llamada a internet
            _state.value = CharacterListState(
                isLoading = false,
                data = CharacterDb().getAllCharacters()
            )
        }
    }

    // Se llama cuando el usuario toca el layout de Loading
    fun onLoadingClick() {
        if (_state.value.isLoading) {
            loadJob?.cancel() // evita que los datos lleguen después del error
            _state.value = CharacterListState(isLoading = false, hasError = true)
        }
    }
}