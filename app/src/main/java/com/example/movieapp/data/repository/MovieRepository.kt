package com.example.movieapp.data.repository

import com.example.movieapp.data.entities.MoviePopular
import com.example.movieapp.data.remote.MovieApi
import com.example.movieapp.utils.BaseApiResponse
import com.example.movieapp.utils.Constants
import com.example.movieapp.utils.NetworkResult
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieApi: MovieApi) : BaseApiResponse() {

    suspend fun getPopularMovies(page: Int): NetworkResult<MoviePopular> {
        return safeApiCall { movieApi.getPopularMovies(Constants.API_KEY, page) }
    }
}