package com.example.movieapp.data.entities

data class MoviePopular(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)