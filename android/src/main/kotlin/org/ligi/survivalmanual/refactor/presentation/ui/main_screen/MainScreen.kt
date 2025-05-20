package org.ligi.survivalmanual.refactor.presentation.ui.main_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import org.ligi.survivalmanual.refactor.domain.SurvivalContent
import org.ligi.survivalmanual.refactor.presentation.ui.highlightTextAsAnnotatedString
import org.ligi.survivalmanual.refactor.presentation.ui.main_screen.MainScreenState
import org.ligi.survivalmanual.refactor.presentation.ui.main_screen.MainScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = viewModel(),
    onNavigateToPreferences: () -> Unit,
    onNavigateToImage: (imageId: String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // Assuming the ViewModel exposes the GetImageUseCase
    // val getImageUseCase = viewModel.getImageUseCase // This would be provided by the ViewModel


    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.handleIntent(MainScreenViewModel.Intent.Search(it))
                },
                label = { Text("Search") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Box(modifier = Modifier.align(Alignment.End)) {
                IconButton(onClick = onNavigateToPreferences) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
            }

            when (state) {
                is MainScreenState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )

                is MainScreenState.Success -> {
                    val content = (state as MainScreenState.Success).content
                    Text(
                        text = "Survival Guide Loaded: ${content.title}",
                        modifier = Modifier.padding(8.dp)
                    )
                }

                is MainScreenState.Error -> Text(
                    text = "Error: ${(state as MainScreenState.Error).message}",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )

                is MainScreenState.SearchResults -> {
                    val results = (state as MainScreenState.SearchResults).results
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(results) { result -> // Assuming SearchResult has a 'snippet' field
                            Text(text = result.title, modifier = Modifier.padding(8.dp))
                            Text(
                                text = highlightTextAsAnnotatedString(
                                    text = result.snippet,
                                    query = searchQuery
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, bottom = 8.dp)
                            )
                            Text(text = result.title, modifier = Modifier.padding(8.dp))
                        }
                    }
                }

                MainScreenState.Initial -> Text(
                    text = "Initializing...",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun ContentDisplay(content: SurvivalContent, viewModel: MainScreenViewModel) {
    // This is a simplified example. You would likely have more complex parsing
    // of the SurvivalContent structure to identify different content types (text, images, etc.)
    LazyColumn {
        item {
            Text(text = content.title)
        }
        // Assuming SurvivalContent has a list of elements that can be text or images
        // items(content.elements) { element ->
        //     when (element) {
        //         is TextElement -> Text(text = element.text)
        //         is ImageElement -> ImageComposable(imageId = element.imageId, viewModel = viewModel)
        //     }
        // }
    }
}

// Placeholder for the Image Composable
@Composable
fun ImageComposable(imageId: String, viewModel: MainScreenViewModel) {
    // In a real scenario, you would use an image loading library here (e.g., Coil)
    // and potentially observe a state from the ViewModel for the loaded image data.
    // For now, we'll just display a placeholder.
    // val imageData by viewModel.getImage(imageId).collectAsState(initial = null) // Example using ViewModel

    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        model = imageId, // Assuming imageId is directly usable by Coil (e.g., resource ID, URL)
        contentDescription = "Survival Guide Image", // Add a meaningful content description
        // Coil or Glide image composable would go here
        // AsyncImage(model = imageData, contentDescription = "Image")
    )

}