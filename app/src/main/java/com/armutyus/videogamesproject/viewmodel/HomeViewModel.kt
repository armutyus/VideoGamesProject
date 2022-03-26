package com.armutyus.videogamesproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armutyus.videogamesproject.model.VideoGamesResponse
import com.armutyus.videogamesproject.repo.VideoGamesRepoInterface
import com.armutyus.videogamesproject.roomdb.Games
import com.armutyus.videogamesproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repoInterface: VideoGamesRepoInterface
) : ViewModel() {

    private val gamesResponse = MutableLiveData<Resource<VideoGamesResponse>>()
    val gamesResponseList: LiveData<Resource<VideoGamesResponse>>
        get() = gamesResponse

    private val videoGames = MutableLiveData<List<Games>>()
    val videoGamesList: LiveData<List<Games>>
        get() = videoGames

    private val searchVideoGames = MutableLiveData<List<Games>>()
    val searchGamesFromRoomList: LiveData<List<Games>>
        get() = searchVideoGames

    fun makeGamesResponse() {
        viewModelScope.launch {
            val response = repoInterface.gamesFromApi()
            gamesResponse.value = response
        }
    }

    fun insertGames(games: Games) =
        CoroutineScope(Dispatchers.IO).launch { repoInterface.insertGames(games) }

    fun getGamesList() {
        CoroutineScope(Dispatchers.IO).launch {
            repoInterface.getGamesList().collectLatest {
                videoGames.postValue(it)
            }
        }
    }

    fun searchGamesList(searchString: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repoInterface.searchGames(searchString).collectLatest {
                searchVideoGames.postValue(it)
            }
        }
    }

    /*fun setChosenCharacter(result: Result) {
        mutableChosenChar.value = result
    }*/
}