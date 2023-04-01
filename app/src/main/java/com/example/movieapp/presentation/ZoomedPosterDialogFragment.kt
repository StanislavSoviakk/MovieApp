package com.example.movieapp.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieapp.utils.Constants

class ZoomedPosterDialogFragment : DialogFragment() {

    private val args: ZoomedPosterDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setBackgroundEffect()

        return ComposeView(requireContext()).apply {
            setContent {
                ZoomedInPosterDialog()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    private fun setBackgroundEffect() {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            dialog?.window?.attributes?.blurBehindRadius = 8
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ZoomedInPosterDialog() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            GlideImage(
                model = Constants.BASE_POSTER_URL + args.poster,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}