package com.example.movieapp.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.entities.Movie
import com.example.movieapp.domain.usecases.GetPopularMoviesUseCase
import com.example.movieapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PAGE_SIZE = 20

@HiltViewModel
class PopularMovieViewModel @Inject constructor(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) :
    ViewModel() {
    private val _popularMoviesLiveData = MutableLiveData<MutableList<Movie>?>()
    val popularMoviesLiveData: LiveData<MutableList<Movie>?>
        get() = _popularMoviesLiveData

    val loading = mutableStateOf(false)
    val errorDialog = mutableStateOf(false)

    private var errorDialogIsShown = false

    val page = mutableStateOf(1)
    var moviesScrollPosition = 0

    fun loadMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            loading.value = true
            //Simulation of complex request
            delay(2000)
            getPopularMoviesUseCase.invoke(page = page.value).let { networkResult ->
                when (networkResult) {
                    is NetworkResult.Success -> {
                        networkResult.data?.results?.let {
                            val currentMovieList = _popularMoviesLiveData.value ?: mutableListOf()
                            currentMovieList.addAll(it)
                            _popularMoviesLiveData.postValue(currentMovieList)
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

    fun loadNextMoviesPage() {
        viewModelScope.launch {
            //prevent duplicate requests
            if ((moviesScrollPosition + 1) >= (page.value * PAGE_SIZE)) {
                incrementPage()
                //prevent calling on init
                if (page.value > 1) {
                    loadMovies()
                }
            }
        }
    }

    private fun incrementPage() {
        page.value = page.value + 1
    }
}