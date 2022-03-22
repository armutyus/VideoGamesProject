package com.armutyus.videogamesproject.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(games: Games)

    @Update
    fun update(games: Games)

    /*@Query("SELECT * FROM Games WHERE id = :id")
    fun getGamesById(id: Int): Games?*/

    @Query("SELECT * FROM Games")
    fun getGamesList(): MutableList<Games>

    @Query("SELECT * FROM Games WHERE favorite = 1")
    fun getFavoriteGamesList(): LiveData<List<Games>>

    @Query("SELECT * FROM Games WHERE name LIKE :searchString")
    fun searchGames(searchString: String): MutableList<Games>
}