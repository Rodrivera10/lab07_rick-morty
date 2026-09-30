package com.rodrigorivera.lab07_moviles.navigation

import kotlinx.serialization.Serializable

@Serializable object LoginRoute

@Serializable object CharactersGraphRoute
@Serializable object CharacterListRoute
@Serializable data class CharacterDetailRoute(val id: Int)

@Serializable object LocationsGraphRoute
@Serializable object LocationListRoute
@Serializable data class LocationDetailRoute(val id: Int)

@Serializable object ProfileRoute