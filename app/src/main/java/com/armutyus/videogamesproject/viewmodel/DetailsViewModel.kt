package com.armutyus.videogamesproject.viewmodel

import androidx.lifecycle.ViewModel
import com.armutyus.videogamesproject.repo.VideoGamesRepoInterface
import com.armutyus.videogamesproject.roomdb.Games
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repoInterface: VideoGamesRepoInterface
) : ViewModel() {

    fun updateGames(games: Games) =
        CoroutineScope(Dispatchers.IO).launch { repoInterface.updateGames(games) }
}