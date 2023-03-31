package com.example.movieapp.domain.usecases

import com.example.movieapp.data.repository.MovieRepositoryImpl
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val movieApiRepository: MovieRepositoryImpl) {
    suspend fun invoke(id: Int) = movieApiRepository.getMovieDetails(id)
}