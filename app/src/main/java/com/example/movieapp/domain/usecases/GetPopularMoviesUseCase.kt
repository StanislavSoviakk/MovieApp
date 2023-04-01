package com.example.movieapp.domain.usecases

import com.example.movieapp.data.repository.MovieRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(private val movieApiRepository: MovieRepository) {
    suspend fun invoke(page: Int) = movieApiRepository.getPopularMovies(page)
}