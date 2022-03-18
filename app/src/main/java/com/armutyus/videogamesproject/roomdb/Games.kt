package com.armutyus.videogamesproject.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Games")
data class Games(
    val background_image: String,
    val name: String,
    val rating: Double,
    val released: String,
    val metacritic: Int,
    val favorite: Boolean,
    @PrimaryKey
    val id: Int?
    )
