package org.ligi.survivalmanual.refactor.data.repository

import org.ligi.survivalmanual.refactor.domain.error.DomainException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ligi.survivalmanual.refactor.data.local.ImageDataSource
import org.ligi.survivalmanual.refactor.data.local.LocalSurvivalGuideDataSource
import org.ligi.survivalmanual.refactor.data.local.PreferencesDataSource
import org.ligi.survivalmanual.refactor.domain.SearchResult
import org.ligi.survivalmanual.refactor.domain.SurvivalContent
import org.ligi.survivalmanual.refactor.domain.UserPreferences
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository

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
                throw DomainException.UnknownErrorException("Error getting survival content: ${e.message}")
            }
        }
    }

    // Implement the searchSurvivalContent method
    override suspend fun searchSurvivalContent(query: String): List<SearchResult> {
        return withContext(Dispatchers.IO) { // Use Dispatchers.IO for potential blocking operations
            try {
                localDataSource.searchSurvivalContent(query)
            } catch (e: Exception) {
                throw DomainException.UnknownErrorException("Error searching survival content: ${e.message}")
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
    override suspend fun getImageData(imageId: String): ByteArray {
        return imageDataSource.getImageData(imageId)
    }
}