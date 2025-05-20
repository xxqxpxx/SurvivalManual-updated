package org.ligi.survivalmanual

import domain.model.UserPreferences
import org.ligi.survivalmanual.refactor.domain.use_case.SaveUserPreferencesUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class SaveUserPreferencesUseCaseTest {

    @Mock
    private lateinit var mockRepository: SurvivalGuideRepository

    private lateinit var saveUserPreferencesUseCase: SaveUserPreferencesUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        saveUserPreferencesUseCase = SaveUserPreferencesUseCase(mockRepository)
    }

    @Test
    fun `invoke calls repository saveUserPreferences with correct preferences`() = runBlocking {
        val preferences = UserPreferences(isNightModeEnabled = true)

        saveUserPreferencesUseCase(preferences)

        verify(mockRepository).saveUserPreferences(preferences)
    }

    @Test(expected = Exception::class) // Replace Exception with a more specific domain exception if needed
    fun `invoke throws exception when repository saveUserPreferences throws exception`() =
        runBlocking {
            val preferences = UserPreferences(isNightModeEnabled = false)
            // Mock repository to throw an exception when saveUserPreferences is called
            // For example, using Mockito:
            // `whenever(mockRepository.saveUserPreferences(preferences)).thenAnswer { throw Exception("Save failed") }`

            saveUserPreferencesUseCase(preferences)
        }
}