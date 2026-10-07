package com.rodrigorivera.lab07_moviles.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rodrigorivera.lab07_moviles.data.Character
import com.rodrigorivera.lab07_moviles.data.CharacterDb
import com.rodrigorivera.lab07_moviles.navigation.CharacterDetailRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterDetailState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: Int = savedStateHandle.toRoute<CharacterDetailRoute>().id

    private val _state = MutableStateFlow(CharacterDetailState())
    val state: StateFlow<CharacterDetailState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = CharacterDetailState(isLoading = true)
            delay(2000) // 2 segundos para el perfil
            _state.value = CharacterDetailState(
                isLoading = false,
                data = CharacterDb().getCharacterById(characterId)
            )
        }
    }

    fun onLoadingClick() {
        if (_state.value.isLoading) {
            loadJob?.cancel()
            _state.value = CharacterDetailState(isLoading = false, hasError = true)
        }
    }
}