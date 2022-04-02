package com.armutyus.videogamesproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armutyus.videogamesproject.model.GameDetails
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
class DetailsViewModel @Inject constructor(
    private val repoInterface: VideoGamesRepoInterface
) : ViewModel() {

    private val videoGamesDetails = MutableLiveData<Games>()
    val videoGamesDetailsById: LiveData<Games>
        get() = videoGamesDetails

    fun updateGames(games: Games) =
        CoroutineScope(Dispatchers.IO).launch { repoInterface.updateGames(games) }

    fun getGamesDetailsById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repoInterface.getGamesByIdRoom(id).collectLatest {
                videoGamesDetails.postValue(it)
            }
        }
    }
}