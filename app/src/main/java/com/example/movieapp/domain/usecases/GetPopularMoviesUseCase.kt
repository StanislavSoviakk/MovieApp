package com.example.movieapp.domain.usecases

import com.example.movieapp.data.repository.MovieRepositoryImpl
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(private val movieApiRepository: MovieRepositoryImpl) {
    suspend fun invoke(page: Int) = movieApiRepository.getPopularMovies(page)
}