package com.armutyus.videogamesproject.model

data class GameDetails(
    val background_image: String,
    val description: String,
    val id: Int,
    val metacritic: Int,
    val name: String,
    val released: String,
)