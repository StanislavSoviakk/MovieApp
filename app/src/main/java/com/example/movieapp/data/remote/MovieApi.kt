package com.example.movieapp.data.remote

import com.example.movieapp.data.entities.MovieDetails
import com.example.movieapp.data.entities.MoviePopular
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("/movie/{id}")
    suspend fun getMovieDetails(
        @Query("api_key") api_key: String,
        @Path("id") id: Int
    ): Response<MovieDetails>

    @GET("movie/popular?")
    suspend fun getPopularMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response<MoviePopular>
}