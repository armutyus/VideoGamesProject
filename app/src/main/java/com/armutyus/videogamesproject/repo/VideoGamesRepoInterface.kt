package com.armutyus.videogamesproject.repo

import com.armutyus.videogamesproject.model.GameDetails
import com.armutyus.videogamesproject.model.VideoGamesResponse
import com.armutyus.videogamesproject.roomdb.Games
import com.armutyus.videogamesproject.util.Resource
import kotlinx.coroutines.flow.Flow

interface VideoGamesRepoInterface {

    suspend fun insertGames(games: Games)

    suspend fun updateGames(games: Games)

    suspend fun getGamesById(id: Int): Resource<GameDetails>

    fun getGamesList(): Flow<List<Games>>

    fun getFavoriteGamesList(): Flow<List<Games>>

    fun searchGames(searchString: String): Flow<List<Games>>

    fun getGamesByIdRoom(id: Int): Games

    suspend fun gamesFromApi(): Resource<VideoGamesResponse>
}