package com.armutyus.videogamesproject.model

data class VideoGamesResponse(
    val count: Int,
    val description: String,
    val next: String,
    val previous: Any,
    val results: List<VideoGames>,
)