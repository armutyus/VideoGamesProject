package com.armutyus.videogamesproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.armutyus.videogamesproject.repo.VideoGamesRepoInterface
import com.armutyus.videogamesproject.roomdb.Games
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repoInterface: VideoGamesRepoInterface
) : ViewModel() {

    private val videoGamesFavorite = MutableLiveData<List<Games>>()
    val videoGamesFavoritesList: LiveData<List<Games>>
        get() = videoGamesFavorite

    fun getFavoriteGamesList() {
        CoroutineScope(Dispatchers.IO).launch {
            repoInterface.getFavoriteGamesList().collectLatest {
                videoGamesFavorite.postValue(it)
            }
        }
    }
}