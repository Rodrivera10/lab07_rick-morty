package com.rodrigorivera.lab07_moviles
import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.Room
import com.rodrigorivera.lab07_moviles.data.*
import com.rodrigorivera.lab07_moviles.data.local.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.*
import org.junit.Assert.*
class RoomSessionTest {
    private lateinit var db: AppDatabase
    private lateinit var prefs: UserPreferences
    @Before fun setup() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        prefs = UserPreferences(context)
        prefs.clearName()
    }
    @After fun cleanup() = runBlocking { prefs.clearName(); db.close() }
    @Test fun syncIsIdempotentAndLogoutClearsBothTables() = runBlocking {
        var waits = 0
        val session = SessionRepository(db, prefs) { waits++ }
        session.login("Rodrigo")
        assertTrue(session.isReady())
        assertEquals(20, db.characters().observeAll().first().size)
        assertEquals(20, db.locations().observeAll().first().size)
        db.characters().toggleFavorite(1)
        session.login("Rodrigo")
        assertEquals(1, waits)
        assertTrue(db.characters().observeById(1).first()!!.isFavorite)
        session.logout()
        assertNull(prefs.name.first())
        assertFalse(prefs.synchronized.first())
        assertTrue(db.characters().observeAll().first().isEmpty())
        assertTrue(db.locations().observeAll().first().isEmpty())
        session.login("Nueva sesión")
        assertEquals(2, waits)
        assertFalse(db.characters().observeById(1).first()!!.isFavorite)
    }
    @Test fun logoutInvalidatesPendingSynchronization() = runBlocking {
        val entered = CompletableDeferred<Unit>()
        val resume = CompletableDeferred<Unit>()
        val session = SessionRepository(db, prefs) { entered.complete(Unit); resume.await() }
        val login = launch { session.login("Rodrigo") }
        entered.await()
        session.logout()
        resume.complete(Unit)
        login.join()
        assertTrue(login.isCancelled)
        assertFalse(session.isReady())
        assertTrue(db.characters().observeAll().first().isEmpty())
        assertTrue(db.locations().observeAll().first().isEmpty())
    }
    @Test fun failedSynchronizationCanRetry() = runBlocking {
        var shouldFail = true
        val session = SessionRepository(db, prefs) { if (shouldFail) error("Simulated failure") }
        try { session.login("Rodrigo"); fail("Expected failure") } catch (expected: IllegalStateException) { }
        assertFalse(prefs.synchronized.first())
        assertTrue(db.characters().observeAll().first().isEmpty())
        shouldFail = false
        session.login("Rodrigo")
        assertTrue(session.isReady())
    }
    @Test fun daoInsertUpdateDeleteAndFavoriteById() = runBlocking {
        val character = CharacterEntity(1, "Rick", "Alive", "Human", "Male", "image")
        db.characters().insertAll(listOf(character, character))
        assertEquals(1, db.characters().observeAll().first().size)
        db.characters().setFavorite(1, true)
        assertTrue(db.characters().observeById(1).first()!!.isFavorite)
        db.characters().update(character.copy(name = "Updated", isFavorite = true))
        assertEquals("Updated", db.characters().observeById(1).first()!!.name)
        db.characters().deleteById(1)
        assertNull(db.characters().observeById(1).first())
        val location = LocationEntity(7, "Earth", "Planet", "C-137")
        db.locations().insertAll(listOf(location, location))
        assertEquals(1, db.locations().observeAll().first().size)
        db.locations().update(location.copy(name = "Updated"))
        assertEquals("Updated", db.locations().observeById(7).first()!!.name)
        db.locations().deleteById(7)
        assertNull(db.locations().observeById(7).first())
    }
}
