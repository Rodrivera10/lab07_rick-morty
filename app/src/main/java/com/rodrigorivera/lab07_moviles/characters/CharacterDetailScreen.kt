package com.rodrigorivera.lab07_moviles.characters

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodrigorivera.lab07_moviles.components.ErrorLayout
import com.rodrigorivera.lab07_moviles.components.LoadingLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: CharacterDetailViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showEditor by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var showDelete by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    state.data?.let { character ->
                        IconButton(onClick = { showEditor = true }) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                        IconButton(onClick = { showDelete = true }) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
                        IconButton(onClick = { viewModel.toggleFavorite(character.id) }) {
                            Icon(if (character.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (character.isFavorite) "Quitar favorito" else "Agregar favorito")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        val character = state.data
        when {
            state.isLoading -> LoadingLayout(
                onClick = viewModel::onLoadingClick,
                modifier = Modifier.padding(padding)
            )
            state.hasError || character == null -> ErrorLayout(
                message = "Error al obtener el perfil del personaje. Intenta de nuevo",
                onRetry = viewModel::loadCharacter,
                modifier = Modifier.padding(padding)
            )
            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    model = character.image,
                    contentDescription = character.name,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = character.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                DetailRow(label = "Species:", value = character.species)
                DetailRow(label = "Status:", value = character.status)
                DetailRow(label = "Gender:", value = character.gender)
            }
        }
    }
    state.data?.let { character ->
        if (showEditor) CharacterEditorDialog(initial = character, onDismiss = { showEditor = false }, onSave = {
            viewModel.save(it)
            showEditor = false
        })
        if (showDelete) AlertDialog(
            onDismissRequest = { showDelete = false },
            title = { Text("Eliminar personaje") },
            text = { Text("¿Deseas eliminar a ${character.name}? Esta acción es local.") },
            confirmButton = { TextButton(onClick = { viewModel.delete(character.id, onNavigateBack); showDelete = false }) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { showDelete = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp
        )
        Text(
            text = value,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}
