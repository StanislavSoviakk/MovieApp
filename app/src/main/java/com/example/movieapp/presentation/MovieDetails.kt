package com.example.movieapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieapp.R
import com.example.movieapp.data.entities.Movie
import com.example.movieapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
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
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(modifier = Modifier.padding(top = 8.dp)) {
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
        var isPosterPressed by remember { mutableStateOf(false) }
        val posterScale by animateFloatAsState(
            if (isPosterPressed) 1.05f else 1f,
            animationSpec = tween(
                durationMillis = 200
            )
        )
        GlideImage(
            model = Constants.BASE_POSTER_URL + movie.poster_path,
            contentDescription = null,
            modifier = Modifier
                .width(180.dp)
                .height(250.dp)
                .scale(posterScale)
                .pointerInput(Unit) {
                    movie.poster_path?.let { poster ->
                        var job: Job?
                        var isDialogOpened = false
                        forEachGesture {
                            awaitPointerEventScope {
                                awaitFirstDown()
                                //ACTION_DOWN
                                job = lifecycleScope.launch(Dispatchers.Default) {
                                    isPosterPressed = true
                                    delay(200)
                                    withContext(Dispatchers.Main) {
                                        val action =
                                            MovieDetailsDirections.showZoomedPosterDialog(poster)
                                        findNavController().navigate(action)
                                        isDialogOpened = true
                                    }
                                }

                                while (awaitPointerEvent().changes.all { it.pressed }) {
                                    //Do nothing and wait for ACTION_UP
                                }

                                //ACTION_UP
                                job?.cancel()
                                isPosterPressed = false
                                if (isDialogOpened) {
                                    findNavController().popBackStack()
                                    isDialogOpened = false
                                }
                            }
                        }
                    }
                },
            contentScale = ContentScale.FillWidth
        )
    }
}