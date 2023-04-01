package com.example.movieapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieapp.R
import com.example.movieapp.data.entities.Movie
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MovieDetails : Fragment() {

    private val args: MovieDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MovieDetail(args.movie)
            }
        }
    }

    @Composable
    fun MovieDetail(movie: Movie) {
        val scrollState = rememberScrollState()
        Scaffold(
            topBar = {
                TopAppBar(title = {}, modifier = Modifier.requiredHeight(70.dp), navigationIcon = {
                    IconButton(onClick = { findNavController().popBackStack() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                },
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 0.dp
                )
            }
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .scrollable(
                            state = scrollState,
                            orientation = Orientation.Vertical
                        )
                ) {
                    Row {
                        Poster(movie)

                        Column(modifier = Modifier.padding(start = 14.dp)) {
                            MovieData(key = "Rating", value = movie.vote_average.toString())

                            MovieData(
                                key = "Release date",
                                value = movie.release_date ?: "no information"
                            )

                            MovieData(
                                key = "Language", value = movie.original_language?.uppercase(
                                    Locale.getDefault()
                                ) ?: "no information"
                            )
                        }
                    }
                    Text(
                        text = movie.title.toString(),
                        modifier = Modifier.padding(
                            top = 10.dp,
                            bottom = 4.dp
                        ),
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = movie.overview.toString(),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun MovieData(key: String, value: String) {
        Column(modifier = Modifier) {
            Text(
                text = key,
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
            Text(text = value, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun Poster(movie: Movie) {
        Card {
            GlideImage(
                model = BASE_URL + movie.poster_path,
                contentDescription = null,
                modifier = Modifier
                    .width(180.dp)
                    .height(250.dp),
                contentScale = ContentScale.FillWidth
            )
        }
    }

    companion object {
        const val BASE_URL = "https://image.tmdb.org/t/p/w500"
    }
}