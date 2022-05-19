package com.armutyus.videogamesproject.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Games::class], version = 1, exportSchema = false)
abstract class GamesDB : RoomDatabase() {
    abstract fun gamesDao(): GamesDao
}