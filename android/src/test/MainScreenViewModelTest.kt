package presentation.ui.main_screen.test

import com.google.common.truth.Truth.assertThat
import domain.error.DomainException
import domain.model.SearchResult
import domain.model.SurvivalContent
import domain.use_case.GetImageUseCase
import domain.use_case.GetSurvivalContentUseCase
import domain.use_case.SearchSurvivalContentUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import presentation.ui.main_screen.MainScreenViewModel
import presentation.ui.main_screen.MainScreenViewModel.State

class MainScreenViewModelTest {

    @Before
    fun setUp() {
        // Set up your test environment here
    }
 
    @get:Rule
    val mockitoRule = MockitoJUnit.rule()
 
    @Mock
    lateinit var getSurvivalContentUseCase: GetSurvivalContentUseCase
 
    @Mock
    lateinit var searchSurvivalContentUseCase: SearchSurvivalContentUseCase
 
    @Mock
    lateinit var getImageUseCase: GetImageUseCase
 
    private lateinit var viewModel: MainScreenViewModel
 
    @Before
    fun setup() {
        viewModel = MainScreenViewModel(
            getSurvivalContentUseCase,
            searchSurvivalContentUseCase,
            getImageUseCase
        )
    }
 
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleIntent LoadContent updates state to Success on success`() = runTest {
        val content = SurvivalContent("Test Title", "Test Content")
        `when`(getSurvivalContentUseCase()).thenReturn(content)
 
        viewModel.handleIntent(MainScreenViewModel.Intent.LoadContent)
 
        assertThat(viewModel.state.value).isEqualTo(State.Success(content))
        verify(getSurvivalContentUseCase).invoke()
    }
 
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleIntent LoadContent updates state to Error on failure`() = runTest {
        val errorMessage = "Failed to load content"
        `when`(getSurvivalContentUseCase()).thenAnswer { throw DomainException.ContentNotFoundException(errorMessage) }
 
        viewModel.handleIntent(MainScreenViewModel.Intent.LoadContent)
 
        assertThat(viewModel.state.value).isEqualTo(State.Error(errorMessage))
        verify(getSurvivalContentUseCase).invoke()
    }
 
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleIntent PerformSearch updates state to SearchResults on success`() = runTest {
        val query = "test"
        val searchResults = listOf(SearchResult("Title 1", "Snippet 1", "id1"), SearchResult("Title 2", "Snippet 2", "id2"))
        `when`(searchSurvivalContentUseCase(query)).thenReturn(searchResults)
 
        viewModel.handleIntent(MainScreenViewModel.Intent.PerformSearch(query))
 
        assertThat(viewModel.state.value).isEqualTo(State.SearchResults(searchResults))
        verify(searchSurvivalContentUseCase).invoke(query)
    }
 
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleIntent PerformSearch updates state to Error on failure`() = runTest {
        val query = "test"
        val errorMessage = "Search failed"
        `when`(searchSurvivalContentUseCase(query)).thenAnswer { throw DomainException.NetworkException(errorMessage) }
 
        viewModel.handleIntent(MainScreenViewModel.Intent.PerformSearch(query))
 
        assertThat(viewModel.state.value).isEqualTo(State.Error(errorMessage))
        verify(searchSurvivalContentUseCase).invoke(query)
    }
 
    // Add more tests for other intents and edge cases as needed
 
    // Note: Testing GetImageUseCase interaction might be better done in UI tests
    // or by observing side effects if the ViewModel triggers image loading for display.
    // For simple use case calls, the pattern would be similar to the tests above.
}