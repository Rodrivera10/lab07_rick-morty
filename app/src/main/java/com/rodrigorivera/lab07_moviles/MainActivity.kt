package com.rodrigorivera.lab07_moviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.rodrigorivera.lab07_moviles.ui.theme.Lab07_movilesTheme
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object CharacterListRoute

@Serializable
data class CharacterDetailRoute(val id: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab07_movilesTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = LoginRoute
                    ) {
                        composable<LoginRoute> {
                            LoginScreen(
                                onNavigateToCharacters = {
                                    navController.navigate(CharacterListRoute) {
                                        popUpTo(LoginRoute) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable<CharacterListRoute> {
                            CharacterListScreen(
                                onCharacterClick = { id ->
                                    navController.navigate(CharacterDetailRoute(id))
                                }
                            )
                        }
                        composable<CharacterDetailRoute> { backStackEntry ->
                            val args = backStackEntry.toRoute<CharacterDetailRoute>()
                            CharacterDetailScreen(
                                characterId = args.id,
                                onNavigateBack = {navController.popBackStack()}

                            )
                        }
                    }
                }
            }
        }
    }
}