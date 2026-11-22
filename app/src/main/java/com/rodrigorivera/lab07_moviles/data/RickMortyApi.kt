package com.rodrigorivera.lab07_moviles.data

import com.rodrigorivera.lab07_moviles.locations.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/** Descarga todos los resultados paginados. Solo se invoca durante el inicio de sesión. */
class RickMortyApi {
    suspend fun fetchCharacters(): List<Character> = fetchAll("character") { item ->
        Character(
            id = item.getInt("id"), name = item.getString("name"),
            status = item.getString("status"), species = item.getString("species"),
            gender = item.getString("gender"), image = item.getString("image")
        )
    }

    suspend fun fetchLocations(): List<Location> = fetchAll("location") { item ->
        Location(
            id = item.getInt("id"), name = item.getString("name"),
            type = item.getString("type"), dimension = item.getString("dimension")
        )
    }

    private suspend fun <T> fetchAll(path: String, convert: (JSONObject) -> T): List<T> = withContext(Dispatchers.IO) {
        val result = mutableListOf<T>()
        var next: String? = "https://rickandmortyapi.com/api/$path"
        while (next != null) {
            val connection = (URL(next).openConnection() as HttpURLConnection).apply {
                connectTimeout = 15_000
                readTimeout = 20_000
                requestMethod = "GET"
            }
            try {
                val status = connection.responseCode
                if (status !in 200..299) throw IllegalStateException("Rick and Morty API respondió HTTP $status")
                val body = connection.inputStream.bufferedReader().use { it.readText() }
                val page = JSONObject(body)
                val rows = page.getJSONArray("results")
                for (i in 0 until rows.length()) result += convert(rows.getJSONObject(i))
                next = page.getJSONObject("info").opt("next")
                    ?.takeUnless { it == JSONObject.NULL }
                    ?.toString()
            } finally {
                connection.disconnect()
            }
        }
        result
    }
}
