package com.duoc.traductor.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationDao {
    @Query("SELECT * FROM translations ORDER BY timestamp DESC")
    fun getAll(): Flow<List<TranslationEntity>>

    @Insert
    suspend fun insert(t: TranslationEntity)

    @Delete
    suspend fun delete(t: TranslationEntity)

    @Query("UPDATE translations SET isFavorite = :fav WHERE id = :id")
    suspend fun setFavorite(id: Int, fav: Boolean)
}
