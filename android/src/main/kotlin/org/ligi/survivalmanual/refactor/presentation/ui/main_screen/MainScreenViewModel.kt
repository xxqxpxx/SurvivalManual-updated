package org.ligi.survivalmanual.refactor.presentation.ui.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.ligi.survivalmanual.refactor.domain.SearchResult
import org.ligi.survivalmanual.refactor.domain.SurvivalContent
import org.ligi.survivalmanual.refactor.domain.error.DomainException
import org.ligi.survivalmanual.refactor.domain.use_case.GetSurvivalContentUseCase
import org.ligi.survivalmanual.refactor.domain.use_case.SearchSurvivalContentUseCase
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getSurvivalContentUseCase: GetSurvivalContentUseCase,
    private val searchSurvivalContentUseCase: SearchSurvivalContentUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        handleIntent(MainScreenIntent.LoadContent)
    }

    fun handleIntent(intent: MainScreenIntent) {
        when (intent) {
            MainScreenIntent.LoadContent -> loadContent()
            is MainScreenIntent.Search -> searchContent(intent.query)
        }
    }

    private fun loadContent() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val content = getSurvivalContentUseCase()
                _state.value = _state.value.copy(isLoading = false, survivalContent = content)
            } catch (e: DomainException) {
                _state.value =
                    _state.value.copy(isLoading = false, error = e.message ?: "An error occurred")
            } catch (e: Exception) { // Catch any other unexpected exceptions
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "An unexpected error occurred: ${e.message}"
                )
            }
        }
    }

    private fun searchContent(query: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, searchResults = null)
            try {
                val results = searchSurvivalContentUseCase(query)
                _state.value =
                    _state.value.copy(isLoading = false, searchResults = results.getOrNull())
            } catch (e: DomainException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "An error occurred during search"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "An unexpected error occurred during search: ${e.message}"
                )
            }
        }
    }
}


sealed class MainScreenIntent {
    object LoadContent : MainScreenIntent()
    data class Search(val query: String) : MainScreenIntent()

    // Add other intents here as needed
}

data class MainScreenState(
    val isLoading: Boolean = false,
    val survivalContent: SurvivalContent? = null,
    val error: String? = null,
    val searchResults: List<SearchResult>? = null
)