package com.rodrigorivera.lab07_moviles.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Crea el archivo "user_prefs" donde se guardan los datos. Debe ir fuera de la clase.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    // La "llave" con la que guardamos y buscamos el nombre
    private val nameKey = stringPreferencesKey("name")

    // Flujo que entrega el nombre guardado (o null si no existe, o sea, no hay sesión)
    val name: Flow<String?> = context.dataStore.data.map { prefs -> prefs[nameKey] }

    // Guarda el nombre (equivale a "iniciar sesión")
    suspend fun saveName(name: String) {
        context.dataStore.edit { prefs -> prefs[nameKey] = name }
    }

    // Borra el nombre (equivale a "cerrar sesión")
    suspend fun clearName() {
        context.dataStore.edit { prefs -> prefs.remove(nameKey) }
    }
}
