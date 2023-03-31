package com.example.movieapp.domain.repository

import com.example.movieapp.data.entities.MovieDetails
import com.example.movieapp.data.entities.MoviePopular
import retrofit2.Response

interface MovieRepository {
    suspend fun getMovieDetails(id: Int):Response<MovieDetails>
    suspend fun getPopularMovies(page: Int):Response<MoviePopular>
}