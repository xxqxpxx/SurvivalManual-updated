package org.ligi.survivalmanual

import org.ligi.survivalmanual.refactor.domain.error.DomainException
import domain.model.UserPreferences
import org.ligi.survivalmanual.refactor.domain.use_case.GetUserPreferencesUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

class GetUserPreferencesUseCaseTest {

    private lateinit var getUserPreferencesUseCase: GetUserPreferencesUseCase
    private lateinit var mockRepository: SurvivalGuideRepository

    @Before
    fun setUp() {
        mockRepository = mock(SurvivalGuideRepository::class.java)
        getUserPreferencesUseCase = GetUserPreferencesUseCase(mockRepository)
    }

    @Test
    fun `invoke calls repository getUserPreferences and returns preferences`() = runBlocking {
        val expectedPreferences = UserPreferences(isNightMode = true)
        `when`(mockRepository.getUserPreferences()).thenReturn(expectedPreferences)

        val result = getUserPreferencesUseCase()

        verify(mockRepository).getUserPreferences()
        assertEquals(expectedPreferences, result)
    }

    @Test(expected = DomainException.UnknownErrorException::class)
    fun `invoke throws DomainException when repository throws exception`() = runBlocking {
        val exception = RuntimeException("Something went wrong")
        `when`(mockRepository.getUserPreferences()).thenThrow(exception)

        getUserPreferencesUseCase()

        verify(mockRepository).getUserPreferences()
    }
}