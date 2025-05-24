package org.ligi.survivalmanual.refactor.presentation.ui.main_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import org.ligi.survivalmanual.refactor.domain.ArticleContent
import org.ligi.survivalmanual.refactor.domain.SurvivalContent
import org.ligi.survivalmanual.refactor.presentation.ui.highlightTextAsAnnotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = viewModel(),
    onNavigateToPreferences: () -> Unit,
    onNavigateToImage: (imageId: String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

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
                    viewModel.handleIntent(MainScreenIntent.Search(it))
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

            when {
                state.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )

                state.error != null -> Text(
                    text = "Error: ${state.error}",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )

            /*    state.searchResults != null -> {
                    val results = state.searchResults
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(results) { result ->
                            Text(
                                text = result.title,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Text(
                                text = highlightTextAsAnnotatedString(
                                    text = result.snippet,
                                    query = searchQuery
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }*/

                state.survivalContent != null -> {
                    ContentDisplay(
                        content = state.survivalContent!!,
                        onNavigateToImage = onNavigateToImage
                    )
                }

                else -> Text(
                    text = "Initializing...",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun ContentDisplay(
    content: SurvivalContent,
    onNavigateToImage: (String) -> Unit
) {
    LazyColumn {
        item {
            Text(
                text = content.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
        
        content.sections.forEach { section ->
            item {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            section.articles.forEach { article ->
                item {
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                
                article.content.forEach { contentItem ->
                    when (contentItem) {
                        is ArticleContent.Text -> {
                            item {
                                Text(
                                    text = contentItem.text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                        is ArticleContent.Image -> {
                            item {
                                AsyncImage(
                                    model = contentItem.imageUrl,
                                    contentDescription = contentItem.altText,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .clickable { onNavigateToImage(contentItem.imageUrl) }
                                )
                            }
                        }
                    }
                }
            }
        }
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