package com.rodrigorivera.lab07_moviles.locations

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodrigorivera.lab07_moviles.characters.DetailRow
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodrigorivera.lab07_moviles.components.ErrorLayout
import com.rodrigorivera.lab07_moviles.components.LoadingLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: LocationDetailViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
        val location = state.data
        when {
            state.isLoading -> LoadingLayout(
                onClick = viewModel::onLoadingClick,
                modifier = Modifier.padding(padding)
            )
            state.hasError || location == null -> ErrorLayout(
                message = "Error al obtener el perfil de la ubicación. Intenta de nuevo",
                onRetry = viewModel::loadLocation,
                modifier = Modifier.padding(padding)
            )
            else -> Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))
                Text(text = location.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(24.dp))
                DetailRow(label = "ID:", value = location.id.toString())
                DetailRow(label = "Type:", value = location.type)
                DetailRow(label = "Dimensions:", value = location.dimension)
            }
        }
    }
}