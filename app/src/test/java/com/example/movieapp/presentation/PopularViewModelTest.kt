package com.example.movieapp.presentation

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movieapp.MainDispatcherRule
import com.example.movieapp.data.entities.Movie
import com.example.movieapp.data.entities.MoviePopular
import com.example.movieapp.domain.usecases.GetPopularMoviesUseCase
import com.example.movieapp.utils.NetworkResult
import io.mockk.every
import io.mockk.mockkStatic
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PopularViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase

    private lateinit var viewModel: PopularMovieViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = PopularMovieViewModel(getPopularMoviesUseCase)
    }

    @Test
    fun `loadMovies should update popularMoviesLiveData`() = runBlocking {
        val movieList = mutableListOf(
            Movie(
                adult = false,
                backdrop_path = "/example",
                id = 1,
                original_language = "en",
                original_title = "Example original title",
                overview = "Example overview",
                popularity = 1.0,
                poster_path = "/examplePosterPath",
                release_date = "01-01-2000",
                title = "Example title",
                video = false,
                vote_average = 1.0,
                vote_count = 100
            )
        )
        val networkResult = NetworkResult.Success(
            MoviePopular(
                page = 1,
                results = movieList,
                total_pages = 2,
                total_results = 40
            )
        )
        `when`(getPopularMoviesUseCase.invoke(page = 1)).thenReturn(networkResult)

        viewModel.loadMovies()

        delay(5000)

        assertEquals(movieList, viewModel.popularMoviesLiveData.value)
    }

    @Test
    fun `loadNextMoviePage should increment page and call loadMovies`() = runBlocking {
        viewModel.page.value = 1
        viewModel.moviesScrollPosition = 20

        viewModel.loadNextMoviesPage()

        assertEquals(2, viewModel.page.value)
    }

    @Test
    fun `loadNextMoviePage should not increment page and call loadMovies when movieScrollPosition is not and the page end`() =
        runBlocking {
            viewModel.page.value = 1
            viewModel.moviesScrollPosition = 15

            viewModel.loadNextMoviesPage()

            assertEquals(1, viewModel.page.value)
        }

    @Test
    fun `loadNextMoviesPage should set errorDialog to true when NetworkResult is Error`() =
        runBlocking {
            mockkStatic(Log::class)
            every { Log.d(any(), any()) } returns 0
            val networkResult = NetworkResult.Error<MoviePopular>(null, "Error message")
            `when`(getPopularMoviesUseCase.invoke(page = 1)).thenReturn(networkResult)

            viewModel.loadMovies()

            delay(5000)

            assertEquals(true, viewModel.errorDialog.value)
        }
}