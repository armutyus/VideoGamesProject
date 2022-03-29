package com.armutyus.videogamesproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armutyus.videogamesproject.model.GameDetails
import com.armutyus.videogamesproject.repo.VideoGamesRepoInterface
import com.armutyus.videogamesproject.roomdb.Games
import com.armutyus.videogamesproject.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val repoInterface: VideoGamesRepoInterface
) : ViewModel() {
    private val gamesDetailsResponse = MutableLiveData<Resource<GameDetails>>()
    val gamesDetails: LiveData<Resource<GameDetails>>
        get() = gamesDetailsResponse

    private val videoGamesDetails = MutableLiveData<Games>()
    val videoGamesDetailsById: LiveData<Games>
        get() = videoGamesDetails

    fun gameDetailResponse(id: Int) {
        viewModelScope.launch {
            val response = repoInterface.getGamesById(id)
            gamesDetailsResponse.value = response
        }
    }

    fun updateGames(games: Games) =
        CoroutineScope(Dispatchers.IO).launch { repoInterface.updateGames(games) }

    fun getGamesDetailsById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repoInterface.getGamesByIdRoom(id).apply {
                videoGamesDetails.postValue(this)
            }
        }
    }
}