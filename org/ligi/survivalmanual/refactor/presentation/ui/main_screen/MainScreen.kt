package org.ligi.survivalmanual.refactor.presentation.ui.main_screen

// Removed android.content.Intent import if it was added accidentally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Ensure correct items import
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
import org.ligi.survivalmanual.refactor.domain.SearchResult
import org.ligi.survivalmanual.refactor.domain.SurvivalContent
import org.ligi.survivalmanual.refactor.presentation.ui.highlightTextAsAnnotatedString
// Ensure MainScreenViewModel is imported if not already
// import org.ligi.survivalmanual.refactor.presentation.ui.main_screen.MainScreenViewModel

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
                    // Use the correct ViewModel Intent class
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
                // Use let for safe handling of nullable searchResults
                state.searchResults != null -> {
                    state.searchResults?.let { results ->
                        if (results.isNotEmpty()) {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(results) { result -> // results is non-null here
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(text = result.title)
                                        Text(
                                            text = highlightTextAsAnnotatedString(
                                                text = result.snippet,
                                                query = searchQuery
                                            ),
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            // Display message when search results are empty
                            Text("No search results found.", modifier = Modifier.padding(8.dp))
                        }
                    }
                }
                // Use let for safe handling of nullable survivalContent
                state.survivalContent != null -> {
                     state.survivalContent?.let { content ->
                        ContentDisplay(content = content, viewModel = viewModel) // content is non-null here
                    }
                }
                else -> Text(
                    text = "Initializing...", // Or "No content loaded"
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun ContentDisplay(content: SurvivalContent, viewModel: MainScreenViewModel) {
    LazyColumn {
        content.sections.forEach { section ->
            item {
                Text(
                    text = section.title,
                    modifier = Modifier.padding(8.dp)
                )
            }

            section.articles.forEach { article ->
                item {
                    Text(
                        text = article.title,
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 4.dp)
                    )

                    // Iterate through article content
                    Column(modifier = Modifier.padding(start = 16.dp, end = 8.dp)) {
                        article.content.forEach { contentItem ->
                            when (contentItem) {
                                is ArticleContent.Text -> {
                                    Text(
                                        text = contentItem.text,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                                is ArticleContent.Image -> {
                                    ImageComposable(imageId = contentItem.imageUrl, viewModel = viewModel)
                                }
                                // Add handling for other ArticleContent types here
                            }
                        }
                    }
                }
            }
        }
    }
}
 }
                }
            }
        }
    }
}

@Composable
fun ImageComposable(imageId: String, viewModel: MainScreenViewModel) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        model = imageId,
        contentDescription = "Survival Guide Image"
    )
}