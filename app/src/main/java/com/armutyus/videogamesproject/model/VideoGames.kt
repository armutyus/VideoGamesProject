package com.armutyus.videogamesproject.model

data class VideoGames(
    val background_image: String,
    val id: Int,
    val name: String,
    val rating: Double,
    val metacritic: Int,
    val released: String,
)