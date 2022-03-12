package com.armutyus.videogamesproject.api

import com.armutyus.videogamesproject.model.GameDetails
import com.armutyus.videogamesproject.model.VideoGamesResponse
import com.armutyus.videogamesproject.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoGamesAPI {

    @GET("games")
    suspend fun getGamesResponse(
        @Query("key") apiKey: String = API_KEY
    ): Response<VideoGamesResponse>

    @GET("games/{id}")
    suspend fun getGamesDetails(
        @Path("id") id: Int,
        @Query("key") apiKey: String = API_KEY
    ): GameDetails

}