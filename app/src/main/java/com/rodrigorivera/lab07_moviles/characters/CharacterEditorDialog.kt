package com.rodrigorivera.lab07_moviles.characters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rodrigorivera.lab07_moviles.data.Character

@Composable
fun CharacterEditorDialog(
    initial: Character? = null,
    onDismiss: () -> Unit,
    onSave: (Character) -> Unit
) {
    var name by remember(initial) { mutableStateOf(initial?.name.orEmpty()) }
    var status by remember(initial) { mutableStateOf(initial?.status ?: "Alive") }
    var species by remember(initial) { mutableStateOf(initial?.species ?: "Human") }
    var gender by remember(initial) { mutableStateOf(initial?.gender ?: "unknown") }
    var image by remember(initial) { mutableStateOf(initial?.image.orEmpty()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Crear personaje" else "Editar personaje") },
        text = {
            Column(Modifier.heightIn(max = 440.dp).verticalScroll(rememberScrollState())) {
                OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(status, { status = it }, label = { Text("Estado") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(species, { species = it }, label = { Text("Especie") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(gender, { gender = it }, label = { Text("Género") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(image, { image = it }, label = { Text("URL de imagen") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank() && status.isNotBlank() && species.isNotBlank() && gender.isNotBlank(),
                onClick = {
                    val base = initial
                    onSave(Character(base?.id ?: 0, name.trim(), status.trim(), species.trim(), gender.trim(), image.trim(), base?.isFavorite ?: false))
                }
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
