package com.armutyus.videogamesproject.model

data class VideoGamesResponse(
    val count: Int,
    val description: String,
    val next: String,
    val nofollow: Boolean,
    val nofollow_collections: List<String>,
    val noindex: Boolean,
    val previous: Any,
    val results: List<VideoGames>,
    val seo_description: String,
    val seo_h1: String,
    val seo_keywords: String,
    val seo_title: String
)