package com.example.movieapp.data.repository

import com.example.movieapp.data.entities.MoviePopular
import com.example.movieapp.data.remote.MovieApi
import com.example.movieapp.utils.BaseApiResponse
import com.example.movieapp.utils.NetworkResult
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieApi: MovieApi) : BaseApiResponse() {

    suspend fun getPopularMovies(page: Int): NetworkResult<MoviePopular> {
        return safeApiCall { movieApi.getPopularMovies(api_key, page) }
    }

    companion object {
        const val api_key = "b144c8edb49202499c748f4052e75a30"
    }
}