package presentation.ui.image_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import org.ligi.survivalmanual.refactor.domain.ArticleContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen(
    imageId: String,
    onBack: () -> Unit,
    viewModel: ImageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val imageUrl by viewModel.imageUrl.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(imageId) {
        viewModel.loadImage(imageId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { ArticleContent.Text("Image") }, // You might want a more descriptive title
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
                isLoading -> CircularProgressIndicator()
                error != null -> Text(
                    "Error loading image: ${error.message}",
                    color = MaterialTheme.colors.error
                )

                imageUrl != null -> {
                    // TODO: Implement zoom and pan functionality for the image
                    // This can be done by using modifiers like graphicsLayer and pointerInput
                    // to handle touch gestures (pinch for zoom, drag for pan).
                    // Alternatively, integrate a library that provides zoomable image functionality.

                    AsyncImage(
                        model = imageUrl, // Assuming ViewModel provides a suitable model for Coil
                        contentDescription = "Image", // Provide a meaningful description
                        modifier = Modifier.fillMaxSize()
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

                else -> ArticleContent.Text("Image not available") // Handle case where imageUrl is null after loading
            }
        }
    }
}