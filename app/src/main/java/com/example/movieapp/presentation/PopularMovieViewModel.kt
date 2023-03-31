package com.example.movieapp.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.entities.MoviePopular
import com.example.movieapp.domain.usecases.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMovieViewModel @Inject constructor(private val useCase: GetPopularMoviesUseCase) :
    ViewModel() {
    private val _popularMoviesLiveData = MutableLiveData<MoviePopular>()
    val popularMoviesLiveData: LiveData<MoviePopular>
        get() = _popularMoviesLiveData

    fun getPopularMovies() {
        viewModelScope.launch {
            useCase.invoke(page = 1).let {
                if (it.isSuccessful) {
                    _popularMoviesLiveData.postValue(it.body())
                    Log.d("Get movies", it.body()?.results.toString())
                } else
                    Log.d("Get movies", "Failed to load popular movies: ${it.code()}")
            }
        }
    }
}