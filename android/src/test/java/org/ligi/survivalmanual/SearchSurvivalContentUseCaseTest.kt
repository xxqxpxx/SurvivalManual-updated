package org.ligi.survivalmanual

import domain.model.SearchResult
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository
import org.ligi.survivalmanual.refactor.domain.use_case.SearchSurvivalContentUseCase
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class SearchSurvivalContentUseCaseTest {

    @Mock
    private lateinit var survivalGuideRepository: SurvivalGuideRepository

    private lateinit var searchSurvivalContentUseCase: SearchSurvivalContentUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        searchSurvivalContentUseCase = SearchSurvivalContentUseCase(survivalGuideRepository)
    }

    @Test
    fun `invoke calls repository searchSurvivalContent with correct query and returns results`() =
        runBlocking {
            val query = "test"
            val expectedResults = listOf(
                SearchResult("Title 1", "Snippet 1", "id1"),
                SearchResult("Title 2", "Snippet 2", "id2")
            )
            `when`(survivalGuideRepository.searchSurvivalContent(query)).thenReturn(expectedResults)

            val actualResults = searchSurvivalContentUseCase(query)

            assertEquals(expectedResults, actualResults)
        }

    @Test
    fun `invoke calls repository searchSurvivalContent and returns empty list for no results`() =
        runBlocking {
            val query = "nonexistent"
            val expectedResults = emptyList<SearchResult>()
            `when`(survivalGuideRepository.searchSurvivalContent(query)).thenReturn(expectedResults)

            val actualResults = searchSurvivalContentUseCase(query)

            assertEquals(expectedResults, actualResults)
        }

    // Add tests for error scenarios if the use case handles specific exceptions
}