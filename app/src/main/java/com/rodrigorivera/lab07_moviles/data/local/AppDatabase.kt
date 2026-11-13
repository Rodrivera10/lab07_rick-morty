package com.rodrigorivera.lab07_moviles.data.local
import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [CharacterEntity::class, LocationEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characters(): CharacterDao
    abstract fun locations(): LocationDao
}
