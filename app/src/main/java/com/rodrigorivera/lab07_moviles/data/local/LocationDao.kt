package com.rodrigorivera.lab07_moviles.data.local
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao
interface LocationDao {
    @Query("SELECT * FROM locations ORDER BY id") fun observeAll(): Flow<List<LocationEntity>>
    @Query("SELECT * FROM locations WHERE id = :id") fun observeById(id: Int): Flow<LocationEntity?>
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertAll(items: List<LocationEntity>)
    @Update suspend fun update(item: LocationEntity)
    @Query("UPDATE locations SET isFavorite = NOT isFavorite WHERE id = :id") suspend fun toggleFavorite(id: Int)
    @Query("DELETE FROM locations WHERE id = :id") suspend fun deleteById(id: Int)
    @Query("DELETE FROM locations") suspend fun deleteAll()
}
