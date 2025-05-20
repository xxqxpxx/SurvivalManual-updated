package domain.repository

import domain.model.SurvivalContent
import domain.model.SearchResult // Assuming SearchResult is in domain.model
import domain.model.UserPreferences

interface SurvivalGuideRepository {

    suspend fun getSurvivalContent(): SurvivalContent
    suspend fun searchSurvivalContent(query: String): List<SearchResult>
    suspend fun getImageData(imageId: String): String
    // suspend fun getSection(sectionId: String): Section?
    // suspend fun getArticle(articleId: String): Article?
}