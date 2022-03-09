package com.armutyus.videogamesproject.api

import com.armutyus.videogamesproject.model.VideoGamesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoGamesAPI {

    @GET("games")
    suspend fun getGamesResponse(
        @Query("key") apiKey: String = "7c1d4651064340adb0bcdec2976315d4"
    ): Response<VideoGamesResponse>

}