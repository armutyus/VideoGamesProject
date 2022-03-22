package com.armutyus.videogamesproject.repo

import androidx.lifecycle.LiveData
import com.armutyus.videogamesproject.model.GameDetails
import com.armutyus.videogamesproject.model.VideoGamesResponse
import com.armutyus.videogamesproject.roomdb.Games
import com.armutyus.videogamesproject.util.Resource

interface VideoGamesRepoInterface {

    suspend fun insertGames(games: Games)

    suspend fun updateGames(games: Games)

    suspend fun getGamesById(id: Int): GameDetails

    fun getGamesList(): MutableList<Games>

    fun getFavoriteGamesList(): LiveData<List<Games>>

    fun searchGames(searchString: String): MutableList<Games>

    suspend fun gamesFromApi(): Resource<VideoGamesResponse>
}