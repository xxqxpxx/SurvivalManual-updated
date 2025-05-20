package presentation.ui.preferences_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import presentation.ui.preferences_screen.PreferencesViewModel.Intent
import presentation.ui.preferences_screen.PreferencesViewModel.State
import presentation.ui.theme.SurvivalManualTheme

@Composable
fun PreferencesScreen(
    viewModel: PreferencesViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Preferences") })
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
                }
                is State.Error -> {
                    Text("Error loading preferences: ${(state as State.Error).message}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPreferencesScreen() {
    SurvivalManualTheme {
        // For preview, you might want to mock the ViewModel or use a dummy state
        // As ViewModel requires Hilt setup, providing a simple Text for preview
        Text("Preferences Screen Preview")
    }
}
package presentation.ui.preferences_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import presentation.ui.preferences_screen.PreferencesViewModel.Intent
import presentation.ui.preferences_screen.PreferencesViewModel.State
import presentation.ui.theme.SurvivalManualTheme

@Composable
fun PreferencesScreen(
    viewModel: PreferencesViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Preferences") })
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
                        Text("Night Mode")
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
                    Text("Error loading preferences: ${(state as State.Error).message}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPreferencesScreen() {
    SurvivalManualTheme {
        // For preview, you might want to mock the ViewModel or use a dummy state
        // As ViewModel requires Hilt setup, providing a simple Text for preview
        Text("Preferences Screen Preview")
    }
}
package presentation.ui.preferences_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import presentation.ui.preferences_screen.PreferencesViewModel.Intent
import presentation.ui.preferences_screen.PreferencesViewModel.State
import presentation.ui.theme.SurvivalManualTheme

@Composable
fun PreferencesScreen(
    viewModel: PreferencesViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Preferences") })
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
                        Text("Night Mode")
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
                    Text("Error loading preferences: ${(state as State.Error).message}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPreferencesScreen() {
    SurvivalManualTheme {
        // For preview, you might want to mock the ViewModel or use a dummy state
        // As ViewModel requires Hilt setup, providing a simple Text for preview
        Text("Preferences Screen Preview")
    }
}