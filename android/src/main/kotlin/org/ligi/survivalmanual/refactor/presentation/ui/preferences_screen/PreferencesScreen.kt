package org.ligi.survivalmanual.refactor.presentation.ui.preferences_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ligi.survivalmanual.refactor.domain.ArticleContent
import org.ligi.survivalmanual.refactor.presentation.ui.preferences_screen.PreferencesViewModel.Intent
import org.ligi.survivalmanual.refactor.presentation.ui.preferences_screen.PreferencesViewModel.State


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    viewModel: PreferencesViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { ArticleContent.Text("Preferences") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when (state) {
                is State.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is State.DisplayPreferences -> {
                    val preferences = (state as State.DisplayPreferences).preferences
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ArticleContent.Text("Night Mode")
                        Switch(
                            checked = preferences.isNightModeEnabled,
                            onCheckedChange = { isChecked ->
                                viewModel.handleIntent(Intent.ToggleNightMode(isChecked))
                            }
                        )
                    }
                    // Add other preference controls here based on UserPreferences data class
                }

                is State.Error -> {
                    ArticleContent.Text("Error loading preferences: ${(state as State.Error).message}")
                }

                else -> {}
            }
        }
    }
}
