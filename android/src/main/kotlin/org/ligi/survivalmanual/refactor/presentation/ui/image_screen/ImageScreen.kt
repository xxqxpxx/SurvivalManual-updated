package org.ligi.survivalmanual.refactor.presentation.ui.image_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
  import coil3.compose.AsyncImage
import org.ligi.survivalmanual.refactor.domain.ArticleContent

import androidx.compose.material3.Scaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen(
    imageId: String,
    onBack: () -> Unit,
    viewModel: ImageViewModel = hiltViewModel<ImageViewModel>()
) {
    val context = LocalContext.current
 val state by viewModel.state.collectAsState()

    LaunchedEffect(imageId) {
        viewModel.loadImage(imageId)
    }

    Scaffold(
 topBar = {
 TopAppBar(
 title = { Text("Image") }, // You might want a more descriptive title
 navigationIcon = {
 IconButton(onClick = onBack) {
 Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
 }
 }
 )
 }
    ) { paddingValues ->
 Box(
 modifier = Modifier
 .fillMaxSize()
 .padding(paddingValues),
 contentAlignment = Alignment.Center
 ) {
 when {
 state.isLoading -> CircularProgressIndicator()
 state.error != null -> Text(
 text = "Error loading image: ${state.error}",
 color = MaterialTheme.colorScheme.error
 )

 state.imageData != null -> {
 AsyncImage(
 model = state.imageData, // Assuming Coil can handle ByteArray
 contentDescription = "Survival Guide Image", // Add a meaningful content description
                        modifier = Modifier.fillMaxSize()
                }
                        // Example placeholders for zoom/pan modifiers:
                        // .graphicsLayer {
                        //     scaleX = scale
                        //     scaleY = scale
                        //     translationX = offsetX
                        //     translationY = offsetY
                        // }
                        // .pointerInput(Unit) {
                        //     detectTransformGestures { centroid, pan, zoom, rotation ->
                        //         // Update scale and offset state here
                        //     }
                        // }
                    )
                }
 else -> {} // Handle initial state or no data
            }
        }
    }
}