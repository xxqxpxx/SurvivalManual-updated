package org.ligi.survivalmanual.refactor.presentation.navigation

import androidx.compose.runtime.Composable
 import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.ligi.survivalmanual.refactor.presentation.ui.main_screen.MainScreen
import org.ligi.survivalmanual.refactor.presentation.ui.main_screen.MainScreenViewModel
import org.ligi.survivalmanual.refactor.presentation.ui.preferences_screen.PreferencesScreen
import org.ligi.survivalmanual.refactor.presentation.ui.preferences_screen.PreferencesViewModel
import org.ligi.survivalmanual.refactor.presentation.ui.image_screen.ImageScreen

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object PreferencesScreen : Screen("preferences_screen")
    object ImageScreen : Screen("image_screen/{imageId}") {
        fun createRoute(imageId: String) = "image_screen/$imageId"
    }
    // Add other screens here
}


@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(Screen.MainScreen.route) {
            val viewModel = hiltViewModel<MainScreenViewModel>()
            MainScreen(
                viewModel = viewModel,
                onNavigateToPreferences = { navController.navigate(Screen.PreferencesScreen.route) },
                onNavigateToImage = { imageId ->
                    navController.navigate(
                        Screen.ImageScreen.route.replace(
                            "{imageId}",
                            imageId
                        )
                    )
                }
            )
        }
        composable(Screen.PreferencesScreen.route) {
            val viewModel = hiltViewModel<PreferencesViewModel>()
            PreferencesScreen(
                viewModel = viewModel
                /* Add navigation lambdas for preferences screen if needed */
            )
        }

        // Add other composables for other screens here
        composable(
            route = Screen.ImageScreen.route,
            arguments = listOf(navArgument("imageId") { type = NavType.StringType })
        ) { backStackEntry ->
            val imageId = backStackEntry.arguments?.getString("imageId")
                ?: "" // Handle case where imageId is null
            // Ensure imageId is not empty or handle the error
            ImageScreen(
                imageId = imageId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}