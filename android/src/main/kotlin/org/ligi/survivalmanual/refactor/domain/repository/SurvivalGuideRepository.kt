package org.ligi.survivalmanual.refactor.domain.repository

import org.ligi.survivalmanual.refactor.domain.Article
import org.ligi.survivalmanual.refactor.domain.SearchResult
import org.ligi.survivalmanual.refactor.domain.Section
import org.ligi.survivalmanual.refactor.domain.SurvivalContent
import org.ligi.survivalmanual.refactor.domain.UserPreferences

interface SurvivalGuideRepository {

    suspend fun getSurvivalContent(): SurvivalContent
    suspend fun searchSurvivalContent(query: String): List<SearchResult>
    suspend fun getImageData(imageId: String): String

    // suspend fun getSection(sectionId: String): Section?
  //   suspend fun getArticle(articleId: String): Article?
    suspend fun getUserPreferences(): UserPreferences
    suspend fun saveUserPreferences(preferences: UserPreferences)
}