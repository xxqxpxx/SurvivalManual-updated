package org.ligi.survivalmanual.refactor.domain.use_case

import org.ligi.survivalmanual.refactor.domain.error.DomainException
import org.ligi.survivalmanual.refactor.domain.SearchResult
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository

class SearchSurvivalContentUseCase(
    private val repository: SurvivalGuideRepository
) {
    suspend operator fun invoke(query: String): Result<List<SearchResult>> {
        return try {
            val results = repository.searchSurvivalContent(query)
            // Handling empty results as a success state with an empty list
            Result.success(results)
        } catch (e: Exception) {
            // Catching potential exceptions from the repository
            // Consider more specific exception handling if repository throws specific exceptions
            Result.failure(DomainException.UnknownErrorException("Failed to perform search: ${e.message}"))
        }
    }
}