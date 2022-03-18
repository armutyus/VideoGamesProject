package com.armutyus.videogamesproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armutyus.videogamesproject.model.VideoGames
import com.armutyus.videogamesproject.model.VideoGamesResponse
import com.armutyus.videogamesproject.repo.VideoGamesRepoInterface
import com.armutyus.videogamesproject.roomdb.Games
import com.armutyus.videogamesproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repoInterface: VideoGamesRepoInterface
) : ViewModel() {

    fun insertGames(games: Games) =
        CoroutineScope(Dispatchers.IO).launch { repoInterface.insertGames(games) }

    private val gamesResponse = MutableLiveData<Resource<VideoGamesResponse>>()
    val gamesResponseList: LiveData<Resource<VideoGamesResponse>>
        get() = gamesResponse

    /*private val videoGames = MutableLiveData<VideoGames>()
    val videoGamesList: LiveData<VideoGames>
        get() = videoGames*/

    fun makeGamesResponse() {
        viewModelScope.launch {
            val response = repoInterface.gamesFromApi()
            gamesResponse.value = response
        }
    }

    /*fun setChosenCharacter(result: Result) {
        mutableChosenChar.value = result
    }*/
}