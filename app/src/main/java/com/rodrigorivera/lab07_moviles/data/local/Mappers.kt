package com.rodrigorivera.lab07_moviles.data.local

import com.rodrigorivera.lab07_moviles.data.Character
import com.rodrigorivera.lab07_moviles.locations.Location

fun CharacterEntity.toModel() = Character(id, name, status, species, gender, image, isFavorite)
fun Character.toEntity() = CharacterEntity(id, name, status, species, gender, image, isFavorite)
fun LocationEntity.toModel() = Location(id, name, type, dimension, isFavorite)
fun Location.toEntity() = LocationEntity(id, name, type, dimension, isFavorite)
