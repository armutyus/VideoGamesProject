package com.armutyus.videogamesproject.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Games")
data class Games(
    var background_image: String,
    var name: String,
    var rating: Double,
    var released: String,
    var metacritic: Int,
    var description: String,
    var favorite: Boolean,
    @PrimaryKey
    var id: Int?
)
