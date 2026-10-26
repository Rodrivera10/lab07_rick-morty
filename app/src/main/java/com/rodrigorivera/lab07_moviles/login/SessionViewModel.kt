package com.rodrigorivera.lab07_moviles.login
import android.app.Application
import androidx.lifecycle.*
import com.rodrigorivera.lab07_moviles.data.AppContainer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
data class SessionState(val loading: Boolean = false, val error: String? = null, val completed: Boolean = false)
class SessionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppContainer.get(application).session
    private val _state = MutableStateFlow(SessionState())
    val state = _state.asStateFlow()
    val name = repository.name.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    fun login(name: String) = perform { repository.login(name) }
    fun logout() = perform { repository.logout() }
    private fun perform(action: suspend () -> Unit) {
        if (_state.value.loading) return
        _state.value = SessionState(loading = true)
        viewModelScope.launch {
            try { action(); _state.value = SessionState(completed = true) }
            catch (e: CancellationException) { throw e }
            catch (e: Exception) { _state.value = SessionState(error = "No se pudo completar la operación. Comprueba el almacenamiento e intenta de nuevo.") }
        }
    }
}
