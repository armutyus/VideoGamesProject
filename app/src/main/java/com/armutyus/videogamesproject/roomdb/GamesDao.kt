package com.armutyus.videogamesproject.roomdb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(games: Games)

    @Update
    suspend fun update(games: Games)

    @Query("SELECT * FROM Games WHERE id = :id")
    fun getGamesByIdRoom(id: Int): Games

    @Query("SELECT * FROM Games")
    fun getGamesList(): Flow<List<Games>>

    @Query("SELECT * FROM Games WHERE favorite = 1")
    fun getFavoriteGamesList(): Flow<List<Games>>

    @Query("SELECT * FROM Games WHERE name LIKE :searchString")
    fun searchGames(searchString: String): Flow<List<Games>>
}