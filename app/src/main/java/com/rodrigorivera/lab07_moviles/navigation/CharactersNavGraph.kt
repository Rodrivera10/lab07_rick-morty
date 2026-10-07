package com.rodrigorivera.lab07_moviles.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.rodrigorivera.lab07_moviles.characters.CharacterDetailScreen
import com.rodrigorivera.lab07_moviles.characters.CharacterListScreen

fun NavGraphBuilder.charactersNavGraph(navController: NavController) {
    navigation<CharactersGraphRoute>(startDestination = CharacterListRoute) {
        composable<CharacterListRoute> {
            CharacterListScreen(
                onCharacterClick = { id -> navController.navigate(CharacterDetailRoute(id)) }
            )
        }
        composable<CharacterDetailRoute> {
            CharacterDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}