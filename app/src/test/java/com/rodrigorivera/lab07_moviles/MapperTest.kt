package com.rodrigorivera.lab07_moviles
import com.rodrigorivera.lab07_moviles.data.Character
import com.rodrigorivera.lab07_moviles.locations.Location
import com.rodrigorivera.lab07_moviles.data.local.*
import org.junit.Assert.*
import org.junit.Test
class MapperTest {
    @Test fun characterRoundTripPreservesEveryFieldAndFavorite() {
        val model = Character(42, "Rick", "Alive", "Human", "Male", "https://image", true)
        assertEquals(model, model.toEntity().toModel())
    }
    @Test fun locationRoundTripPreservesEveryField() {
        val model = Location(9, "Earth", "Planet", "C-137")
        assertEquals(model, model.toEntity().toModel())
    }
}
