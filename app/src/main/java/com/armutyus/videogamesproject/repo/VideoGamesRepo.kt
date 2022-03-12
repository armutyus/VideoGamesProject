package com.armutyus.videogamesproject.repo

import androidx.lifecycle.LiveData
import com.armutyus.videogamesproject.api.VideoGamesAPI
import com.armutyus.videogamesproject.model.GameDetails
import com.armutyus.videogamesproject.model.VideoGamesResponse
import com.armutyus.videogamesproject.roomdb.Games
import com.armutyus.videogamesproject.roomdb.GamesDao
import com.armutyus.videogamesproject.util.Resource
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

    override suspend fun getGamesById(id: Int): GameDetails {
        return videoGamesAPI.getGamesDetails(id)
    }

    override fun getGamesList(): LiveData<List<Games>> {
        return gamesDao.getGamesList()
    }

    override fun getFavoriteGamesList(): LiveData<List<Games>> {
        return gamesDao.getFavoriteGamesList()
    }

    override fun searchGames(searchString: String): LiveData<List<Games>> {
        return gamesDao.searchGames(searchString)
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
}