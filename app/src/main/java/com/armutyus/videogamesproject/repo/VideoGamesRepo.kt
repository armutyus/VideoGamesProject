package com.armutyus.videogamesproject.repo

import com.armutyus.videogamesproject.api.VideoGamesAPI
import com.armutyus.videogamesproject.model.GameDetails
import com.armutyus.videogamesproject.model.VideoGamesResponse
import com.armutyus.videogamesproject.roomdb.Games
import com.armutyus.videogamesproject.roomdb.GamesDao
import com.armutyus.videogamesproject.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VideoGamesRepo @Inject constructor(
    private val gamesDao: GamesDao,
    private val videoGamesAPI: VideoGamesAPI
) : VideoGamesRepoInterface {

    override suspend fun insertGames(games: Games) {
        gamesDao.insert(games)
    }

    override suspend fun updateGames(games: Games) {
        gamesDao.update(games)
    }

    override fun getGamesList(): Flow<List<Games>> {
        return gamesDao.getGamesList()
    }

    override fun getFavoriteGamesList(): Flow<List<Games>> {
        return gamesDao.getFavoriteGamesList()
    }

    override fun searchGames(searchString: String): Flow<List<Games>> {
        return gamesDao.searchGames(searchString)
    }

    override fun getGamesByIdRoom(id: Int): Games {
        return gamesDao.getGamesByIdRoom(id)
    }

    override suspend fun gamesFromApi(): Resource<VideoGamesResponse> {
        return try {
            val response = videoGamesAPI.getGamesResponse()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error", null)
            } else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            Resource.error("No data!", null)
        }
    }

    override suspend fun getGamesById(id: Int): Resource<GameDetails> {
        return try {
            val response = videoGamesAPI.getGamesDetails(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error", null)
            } else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            Resource.error("No data!", null)
        }
    }
}