package com.rodrigorivera.lab07_moviles.data
import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rodrigorivera.lab07_moviles.data.local.*
import com.rodrigorivera.lab07_moviles.locations.LocationDb
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CharacterRepository(private val db: AppDatabase) {
    fun observeAll() = db.characters().observeAll().map { rows -> rows.map { it.toModel() } }
    fun observeById(id: Int) = db.characters().observeById(id).map { it?.toModel() }
    suspend fun toggleFavorite(id: Int) = withContext(Dispatchers.IO) { db.characters().toggleFavorite(id) }
    suspend fun save(character: Character) = withContext(Dispatchers.IO) { db.characters().update(character.toEntity()) }
    suspend fun create(character: Character): Int = withContext(Dispatchers.IO) {
        val id = (db.characters().observeAll().first().maxOfOrNull { it.id } ?: 0) + 1
        db.characters().insertAll(listOf(character.copy(id = id).toEntity()))
        id
    }
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) { db.characters().deleteById(id) }
}
class LocationRepository(private val db: AppDatabase) {
    fun observeAll() = db.locations().observeAll().map { rows -> rows.map { it.toModel() } }
    fun observeById(id: Int) = db.locations().observeById(id).map { it?.toModel() }
    suspend fun toggleFavorite(id: Int) = withContext(Dispatchers.IO) { db.locations().toggleFavorite(id) }
}

// One shared coordinator serializes session mutations, including logout during synchronization.
class SessionRepository(private val db: AppDatabase, private val prefs: UserPreferences,
    private val synchronizationDelay: suspend () -> Unit = {},
    private val api: RickMortyApi? = null) {
    private val mutex = Mutex()
    private var generation = 0L
    val name = prefs.name
    suspend fun isReady() = mutex.withLock {
        !prefs.name.first().isNullOrBlank() && prefs.synchronized.first()
    }
    suspend fun login(name: String) = withContext(Dispatchers.IO) {
        val token = mutex.withLock {
            if (prefs.synchronized.first() && !prefs.name.first().isNullOrBlank()) return@withContext
            if (prefs.name.first().isNullOrBlank()) {
                db.withTransaction { db.characters().deleteAll(); db.locations().deleteAll() }
                prefs.setSynchronized(false)
            }
            prefs.saveName(name)
            generation
        }
        synchronizationDelay()
        val characters = api?.fetchCharacters() ?: CharacterDb().getAllCharacters()
        val locations = api?.fetchLocations() ?: LocationDb().getAllLocations()
        mutex.withLock {
            if (generation != token) throw CancellationException("Sesión cerrada")
            if (prefs.synchronized.first()) return@withLock
            db.withTransaction {
                db.characters().insertAll(characters.map { it.toEntity() })
                db.locations().insertAll(locations.map { it.toEntity() })
            }
            // If this write fails, retrying inserts is safe and preserves favorites.
            prefs.setSynchronized(true)
        }
    }
    suspend fun logout() = withContext(Dispatchers.IO) {
        mutex.withLock {
            generation++
            // Persist the invalidated session first, so process death cannot restore access.
            prefs.clearName()
            withContext(NonCancellable) {
                db.withTransaction { db.characters().deleteAll(); db.locations().deleteAll() }
            }
        }
    }
}
class AppContainer private constructor(context: Context) {
    val database = Room.databaseBuilder(context, AppDatabase::class.java, "lab10.db")
        .addMigrations(MIGRATION_1_2).build()
    val characters = CharacterRepository(database)
    val locations = LocationRepository(database)
    val session = SessionRepository(
        database,
        UserPreferences(context),
        synchronizationDelay = { delay(4_000) },
        api = RickMortyApi()
    )
    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE locations ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
            }
        }
        @Volatile private var instance: AppContainer? = null
        fun get(context: Context): AppContainer = instance ?: synchronized(this) {
            instance ?: AppContainer(context.applicationContext).also { instance = it }
        }
    }
}
