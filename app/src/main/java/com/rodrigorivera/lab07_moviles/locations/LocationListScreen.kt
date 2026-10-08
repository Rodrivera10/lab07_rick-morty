package com.rodrigorivera.lab07_moviles.locations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locations") },
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
            else -> LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
                items(state.data) { location ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLocationClick(location.id) }
                            .padding(16.dp)
                    ) {
                        Text(text = location.name, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = location.type,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}