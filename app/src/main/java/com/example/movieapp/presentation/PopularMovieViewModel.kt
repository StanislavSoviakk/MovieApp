package com.example.movieapp.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.entities.MoviePopular
import com.example.movieapp.data.remote.NetworkResult
import com.example.movieapp.domain.usecases.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMovieViewModel @Inject constructor(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) :
    ViewModel() {
    private val _popularMoviesLiveData = MutableLiveData<MoviePopular>()
    val popularMoviesLiveData: LiveData<MoviePopular>
        get() = _popularMoviesLiveData

    val loading = mutableStateOf(false)
    val errorDialog = mutableStateOf(false)

    private var errorDialogIsShown = false

    fun loadMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            loading.value = true
            //Simulation of complex request
            delay(2000)
            getPopularMoviesUseCase.invoke(page = 1).let { networkResult ->
                when (networkResult) {
                    is NetworkResult.Success -> {
                        networkResult.data.let {
                            _popularMoviesLiveData.postValue(it)
                        }
                        loading.value = false
                        Log.d("Get movies", networkResult.data?.results.toString())
                    }
                    is NetworkResult.Error -> {
                        Log.d(
                            "Get movies",
                            "Failed to load popular movies: ${networkResult.message}"
                        )
                        launch(Dispatchers.Default) {
                            while (_popularMoviesLiveData.value == null) {
                                delay(5000)
                                loadMovies()
                            }
                        }
                        errorDialog.value = !errorDialogIsShown
                        errorDialogIsShown = true
                    }
                    is NetworkResult.Loading -> {}
                }
            }
        }
    }
}