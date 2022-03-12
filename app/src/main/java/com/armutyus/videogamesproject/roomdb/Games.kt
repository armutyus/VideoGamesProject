package com.armutyus.videogamesproject.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Games")
data class Games(
    val background_image: String,
    val description: String,
    val metacritic: Int,
    val name: String,
    val released: String,
    val rating: Double,
    val favorite: Boolean,
    @PrimaryKey
    val id: Int?
    )
