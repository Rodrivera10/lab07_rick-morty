package com.rodrigorivera.lab07_moviles.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rodrigorivera.lab07_moviles.data.Character
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodrigorivera.lab07_moviles.components.ErrorLayout
import com.rodrigorivera.lab07_moviles.components.LoadingLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharacterListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val visible = state.data.filter { !state.favoritesOnly || it.isFavorite }
    var showCreate by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
                actions = { FilterChip(selected = state.favoritesOnly, onClick = viewModel::toggleFilter, label = { Text("Solo favoritos") }) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = { FloatingActionButton(onClick = { showCreate = true }) { Icon(Icons.Default.Add, contentDescription = "Crear personaje") } }
    ) { padding ->
        when {
            state.isLoading -> LoadingLayout(
                onClick = viewModel::onLoadingClick,
                modifier = Modifier.padding(padding)
            )
            state.hasError -> ErrorLayout(
                message = "Error al obtener listado de personajes. Intenta de nuevo",
                onRetry = viewModel::loadCharacters,
                modifier = Modifier.padding(padding)
            )
            visible.isEmpty() -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(if (state.favoritesOnly) "No tienes personajes favoritos" else "No hay personajes disponibles")
            }
            else -> LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize()
            ) {
                items(visible, key = { it.id }) { character ->
                    CharacterItem(
                        character = character,
                        onClick = { onCharacterClick(character.id) },
                        onFavorite = { viewModel.toggleFavorite(character.id) }
                    )
                    HorizontalDivider()
                }
            }
    }
    if (showCreate) CharacterEditorDialog(onDismiss = { showCreate = false }, onSave = {
        viewModel.create(it)
        showCreate = false
    })

}
}

@Composable
fun CharacterItem(character: Character, onClick: () -> Unit, onFavorite: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)

        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium

            )
            Text(
                text = "${character.species} - ${character.status}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onFavorite) {
            Icon(if (character.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (character.isFavorite) "Quitar favorito" else "Agregar favorito")
        }
    }
}
