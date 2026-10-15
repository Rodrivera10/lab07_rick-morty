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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodrigorivera.lab07_moviles.R
import com.rodrigorivera.lab07_moviles.data.UserPreferences
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onNavigateToCharacters: () -> Unit) {
    // Lo que el usuario escribe en el campo de nombre
    var name by remember { mutableStateOf("") }

    // Herramientas para lanzar corutinas y acceder a DataStore
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context.applicationContext) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Bloque central: logo + campo + botón
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

            // Guarda el nombre y luego navega; deshabilitado si el campo está vacío
            Button(
                onClick = {
                    scope.launch {
                        prefs.saveName(name.trim())
                        onNavigateToCharacters()
                    }
                },
                enabled = name.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Entrar", fontSize = 18.sp)
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