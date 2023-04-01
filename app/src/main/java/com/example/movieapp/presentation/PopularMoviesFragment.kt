package com.example.movieapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieapp.data.entities.Movie
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PopularMoviesFragment : Fragment() {

    private val viewModel: PopularMovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadMovies()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val popularMovies = viewModel.popularMoviesLiveData.observeAsState().value
                MovieList(popularMovies?.results ?: emptyList())
            }
        }
    }

    @Composable
    fun MovieList(movies: List<Movie>) {
        val loading = viewModel.loading.value
        val errorDialog = viewModel.errorDialog.value

        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(movies.take(movies.size)) { item ->
                    MovieItem(movie = item)
                }
            }
            ProgressBar(isDisplayed = loading)
            ShowErrorDialog(isDisplayed = errorDialog)
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun MovieItem(movie: Movie) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Card(elevation = 4.dp, modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    val action = PopularMoviesFragmentDirections.showMovieDetails(movie)
                    findNavController().navigate(action)
                }
            ) {
                GlideImage(
                    model = BASE_URL + movie.poster_path,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3 / 4f)
                        .clip(RoundedCornerShape(4.dp)),
                    contentDescription = "Movie poster",
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            val padding = Modifier.padding(horizontal = 8.dp)
            Text(text = movie.title ?: "", maxLines = 2, modifier = padding)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = movie.release_date ?: "", modifier = padding)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    @Composable
    fun ShowErrorDialog(isDisplayed: Boolean) {
        if (isDisplayed) {
            AlertDialog(
                onDismissRequest = { viewModel.errorDialog.value = false }, title = {
                    Text(text = "Error")
                },
                text = {
                    Text(
                        text = "Oops, it looks like our movie API is having some trouble right now." +
                                " Please check your internet connection and try again."
                    )
                },
                buttons = {
                    Button(
                        onClick = { viewModel.errorDialog.value = false },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Text(text = "OK")
                    }
                }
            )
        }
    }

    @Composable
    fun ProgressBar(isDisplayed: Boolean) {
        if (isDisplayed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(50.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    companion object {
        const val BASE_URL = "https://image.tmdb.org/t/p/w500"
    }
}