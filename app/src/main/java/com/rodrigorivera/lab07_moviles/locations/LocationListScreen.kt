package com.rodrigorivera.lab07_moviles.locations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodrigorivera.lab07_moviles.components.ErrorLayout
import com.rodrigorivera.lab07_moviles.components.LoadingLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationListScreen(
    onLocationClick: (Int) -> Unit,
    viewModel: LocationListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val visible = state.data.filter {
        when (state.filter) {
            LocationFilter.ALL -> true
            LocationFilter.FAVORITES -> it.isFavorite
            LocationFilter.NOT_FAVORITES -> !it.isFavorite
        }
    }
    var filterMenu by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locations") },
                actions = {
                    Box {
                        TextButton(onClick = { filterMenu = true }) { Text(when (state.filter) {
                            LocationFilter.ALL -> "Todas"
                            LocationFilter.FAVORITES -> "Favoritas"
                            LocationFilter.NOT_FAVORITES -> "No favoritas"
                        }) }
                        DropdownMenu(expanded = filterMenu, onDismissRequest = { filterMenu = false }) {
                            listOf(LocationFilter.ALL to "Todas", LocationFilter.FAVORITES to "Favoritas", LocationFilter.NOT_FAVORITES to "No favoritas").forEach { (filter, label) ->
                                DropdownMenuItem(text = { Text(label) }, onClick = { viewModel.setFilter(filter); filterMenu = false })
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingLayout(
                onClick = viewModel::onLoadingClick,
                modifier = Modifier.padding(padding)
            )
            state.hasError -> ErrorLayout(
                message = "Error al obtener listado de ubicaciones. Intenta de nuevo",
                onRetry = viewModel::loadLocations,
                modifier = Modifier.padding(padding)
            )
            visible.isEmpty() -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("No hay ubicaciones para este filtro") }
            else -> LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
                items(visible, key = { it.id }) { location ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLocationClick(location.id) }
                            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(text = location.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = location.type, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { viewModel.toggleFavorite(location.id) }) {
                            Icon(if (location.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (location.isFavorite) "Quitar de favoritas" else "Agregar a favoritas")
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
