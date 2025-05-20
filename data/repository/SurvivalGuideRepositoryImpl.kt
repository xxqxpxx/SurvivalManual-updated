package data.repository

import data.local.LocalSurvivalGuideDataSource
import domain.model.SurvivalContent
import domain.model.SearchResult
import domain.model.UserPreferences
import domain.repository.SurvivalGuideRepository
import domain.error.DomainException
import domain.error.UnknownErrorException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import data.local.PreferencesDataSource

import data.local.ImageDataSource

class SurvivalGuideRepositoryImpl(
    private val localDataSource: LocalSurvivalGuideDataSource,
    private val preferencesDataSource: PreferencesDataSource,
    private val imageDataSource: ImageDataSource // Add ImageDataSource as a dependency
) : SurvivalGuideRepository {
    override suspend fun getSurvivalContent(): SurvivalContent {
        return withContext(Dispatchers.IO) {
            try {
                localDataSource.getSurvivalContent()
            } catch (e: Exception) {
                throw UnknownErrorException("Error getting survival content: ${e.message}")
            }
        }
    }

    // Implement the searchSurvivalContent method
    override suspend fun searchSurvivalContent(query: String): List<SearchResult> {
        return withContext(Dispatchers.IO) { // Use Dispatchers.IO for potential blocking operations
            try {
                localDataSource.searchSurvivalContent(query)
            } catch (e: Exception) {
                throw UnknownErrorException("Error searching survival content: ${e.message}")
            }
        }
    }

    // Implement getUserPreferences method
    override suspend fun getUserPreferences(): UserPreferences {
        return withContext(Dispatchers.IO) {
            preferencesDataSource.getUserPreferences()
        }
    }

    // Implement saveUserPreferences method
    override suspend fun saveUserPreferences(preferences: UserPreferences) {
        withContext(Dispatchers.IO) {
            preferencesDataSource.saveUserPreferences(preferences)
        }
    }

    // Implement getImageData method
    override suspend fun getImageData(imageId: String): String {
        return imageDataSource.getImageData(imageId)
    }
}