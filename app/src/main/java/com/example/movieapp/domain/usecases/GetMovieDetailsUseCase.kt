package com.example.movieapp.domain.usecases

import com.example.movieapp.data.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val movieApiRepository: MovieRepository) {
    suspend fun invoke(id: Int) = movieApiRepository.getMovieDetails(id)
}