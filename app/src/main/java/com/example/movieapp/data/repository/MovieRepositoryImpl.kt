package com.example.movieapp.data.repository

import com.example.movieapp.data.entities.MovieDetails
import com.example.movieapp.data.entities.MoviePopular
import com.example.movieapp.data.remote.MovieApi
import com.example.movieapp.domain.repository.MovieRepository
import retrofit2.Response
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val movieApi: MovieApi): MovieRepository {

    override suspend fun getMovieDetails(id: Int): Response<MovieDetails> {
        return movieApi.getMovieDetails(api_key, id)
    }

    override suspend fun getPopularMovies(page: Int): Response<MoviePopular> {
        return movieApi.getPopularMovies(api_key, page)
    }

    companion object {
        const val api_key = "b144c8edb49202499c748f4052e75a30"
    }
}