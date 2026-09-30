package com.rodrigorivera.lab07_moviles.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.rodrigorivera.lab07_moviles.locations.LocationDetailScreen
import com.rodrigorivera.lab07_moviles.locations.LocationListScreen

fun NavGraphBuilder.locationsNavGraph(navController: NavController) {
    navigation<LocationsGraphRoute>(startDestination = LocationListRoute) {
        composable<LocationListRoute> {
            LocationListScreen(
                onLocationClick = { id -> navController.navigate(LocationDetailRoute(id)) }
            )
        }
        composable<LocationDetailRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<LocationDetailRoute>()
            LocationDetailScreen(
                locationId = args.id,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}