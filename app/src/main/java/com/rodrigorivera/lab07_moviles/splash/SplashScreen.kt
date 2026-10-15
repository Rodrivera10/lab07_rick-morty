package com.rodrigorivera.lab07_moviles.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.rodrigorivera.lab07_moviles.data.UserPreferences
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    onLoggedIn: () -> Unit,
    onLoggedOut: () -> Unit
) {
    val context = LocalContext.current

    // Se ejecuta una sola vez al entrar a la pantalla
    LaunchedEffect(Unit) {
        // first() lee el valor guardado una vez y termina
        val name = UserPreferences(context.applicationContext).name.first()
        if (name.isNullOrBlank()) onLoggedOut() else onLoggedIn()
    }

    // Mientras decide, muestra un circulito (dura una fracción de segundo)
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}