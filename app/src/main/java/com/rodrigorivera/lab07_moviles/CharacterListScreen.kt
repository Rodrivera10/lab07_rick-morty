package com.rodrigorivera.lab07_moviles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rodrigorivera.lab07_moviles.data.Character
import com.rodrigorivera.lab07_moviles.data.CharacterDb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(onCharacterClick: (Int) -> Unit) {
    val characters = CharacterDb().getAllCharacters()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
            )
        }
    ){ padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(characters) { character ->
                CharacterItem(
                    character = character,
                    onClick = { onCharacterClick(character.id) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun CharacterItem(character: Character, onClick: () -> Unit) {
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
        Column {
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
    }
}

class CharacterListScreen {
}