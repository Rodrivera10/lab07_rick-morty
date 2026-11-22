package com.rodrigorivera.lab07_moviles.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodrigorivera.lab07_moviles.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun LoginScreen(onNavigateToCharacters: () -> Unit, viewModel: SessionViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.completed) { if (state.completed) onNavigateToCharacters() }
    // Lo que el usuario escribe en el campo de nombre
    var name by remember { mutableStateOf("") }

    // Herramientas para lanzar corutinas y acceder a DataStore

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Bloque central: logo + campo + botÃƒÂ³n
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo de Rick and Morty
            Image(
                painter = painterResource(id = R.drawable.rick_morty_logo),
                contentDescription = "Logo Rick and Morty",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Input del nombre del usuario
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.loading) { CircularProgressIndicator(); Text("Sincronizando datos...") }
            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            // Guarda el nombre y luego navega; deshabilitado si el campo estÃƒÂ¡ vacÃƒÂ­o
            Button(
                onClick = {
                    viewModel.login(name.trim())
                },
                enabled = name.isNotBlank() && !state.loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Iniciar sesión", fontSize = 18.sp)
            }
        }

        // Pie de pantalla con tus datos
        Text(
            text = "Rodrigo Rivera | 251023 | UVG",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}