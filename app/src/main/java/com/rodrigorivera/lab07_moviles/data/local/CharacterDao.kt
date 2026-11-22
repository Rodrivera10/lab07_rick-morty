package com.rodrigorivera.lab07_moviles.data.local
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters ORDER BY id") fun observeAll(): Flow<List<CharacterEntity>>
    @Query("SELECT * FROM characters WHERE id = :id") fun observeById(id: Int): Flow<CharacterEntity?>
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertAll(items: List<CharacterEntity>)
    @Update suspend fun update(item: CharacterEntity)
    @Query("UPDATE characters SET isFavorite = :favorite WHERE id = :id") suspend fun setFavorite(id: Int, favorite: Boolean)
    @Query("UPDATE characters SET isFavorite = NOT isFavorite WHERE id = :id") suspend fun toggleFavorite(id: Int)
    @Query("DELETE FROM characters WHERE id = :id") suspend fun deleteById(id: Int)
    @Query("DELETE FROM characters") suspend fun deleteAll()
}
