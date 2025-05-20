package org.ligi.survivalmanual

import org.ligi.survivalmanual.refactor.domain.error.DomainException
import org.ligi.survivalmanual.refactor.domain.use_case.GetUserPreferencesUseCase
import org.ligi.survivalmanual.refactor.domain.use_case.SaveUserPreferencesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.ligi.survivalmanual.refactor.presentation.ui.preferences_screen.PreferencesViewModel
import org.ligi.survivalmanual.refactor.presentation.ui.preferences_screen.PreferencesViewModel.Intent
import org.ligi.survivalmanual.refactor.presentation.ui.preferences_screen.PreferencesViewModel.State

@ExperimentalCoroutinesApi
class PreferencesViewModelTest {

    private lateinit var viewModel: PreferencesViewModel
    private val mockGetUserPreferencesUseCase: GetUserPreferencesUseCase = mockk()
    private val mockSaveUserPreferencesUseCase: SaveUserPreferencesUseCase = mockk()

    @Before
    fun setUp() {
        viewModel =
            PreferencesViewModel(mockGetUserPreferencesUseCase, mockSaveUserPreferencesUseCase)
    }

    @Test
    fun `handleIntent LoadPreferences updates state to Success on success`() = runTest {
        val userPreferences = domain.model.UserPreferences(darkModeEnabled = true)
        coEvery { mockGetUserPreferencesUseCase() } returns userPreferences

        viewModel.handleIntent(Intent.LoadPreferences)

        val state = viewModel.state.first()
        assertEquals(State.Success(userPreferences), state)
    }

    @Test
    fun `handleIntent LoadPreferences updates state to Error on failure`() = runTest {
        val errorMessage = "Failed to load preferences"
        coEvery { mockGetUserPreferencesUseCase() } throws DomainException.UnknownErrorException(
            errorMessage
        )

        viewModel.handleIntent(Intent.LoadPreferences)

        val state = viewModel.state.first()
        assertEquals(State.Error(errorMessage), state)
    }

    @Test
    fun `handleIntent SavePreferences updates state to Success on success`() = runTest {
        val userPreferences = domain.model.UserPreferences(darkModeEnabled = false)
        coEvery { mockSaveUserPreferencesUseCase(userPreferences) } returns Unit

        viewModel.handleIntent(Intent.SavePreferences(userPreferences))

        val state = viewModel.state.first()
        assertEquals(
            State.Success(userPreferences),
            state
        ) // Assuming state is updated to the saved preferences
    }

    @Test
    fun `handleIntent SavePreferences updates state to Error on failure`() = runTest {
        val userPreferences = domain.model.UserPreferences(darkModeEnabled = false)
        val errorMessage = "Failed to save preferences"
        coEvery { mockSaveUserPreferencesUseCase(userPreferences) } throws DomainException.PreferencesSaveException(
            errorMessage
        )

        viewModel.handleIntent(Intent.SavePreferences(userPreferences))

        val state = viewModel.state.first()
        assertEquals(State.Error(errorMessage), state)
    }
}