package org.ligi.survivalmanual.refactor.data.local

import domain.error.DomainException
import org.ligi.survivalmanual.refactor.domain.Article
import org.ligi.survivalmanual.refactor.domain.ArticleContent
import org.ligi.survivalmanual.refactor.domain.SearchResult
import org.ligi.survivalmanual.refactor.domain.Section
import org.ligi.survivalmanual.refactor.domain.SurvivalContent

fun linkImagesInMarkDown(markdown: String): String {
    return markdown.replace(Regex("!\\[([^\\]]*)\\]\\(([^)]+)\\)")) { matchResult: MatchResult ->
        "[" + matchResult.value + "](" + matchResult.groupValues[2] + ")"
    }
}

class LocalSurvivalGuideDataSource {

    fun getSurvivalContent(): SurvivalContent {
        try {
            // Return dummy or hardcoded data for now
            val dummyArticle1 = Article(
                id = "article1",
                title = "Finding Water",
                content = linkImagesInMarkDown("Look for dew on grass in the morning..."),
                images = listOf(ArticleContent.Image("water_source.jpg", "A clear stream"))
            )

            val dummyArticle2 = Article(
                id = "article2",
                title = "Building Shelter",
                content = linkImagesInMarkDown("Find a natural windbreak..."),
                images = listOf(
                    ArticleContent.Image(
                        "shelter_construction.png",
                        "Building a lean-to"
                    )
                )
            )

            val dummySection1 = Section(
                id = "section1",
                title = "Basic Needs",
                articles = listOf(dummyArticle1, dummyArticle2)
            )

            val dummyArticle3 = Article(
                id = "article3",
                title = "Navigating with Stars",
                content = linkImagesInMarkDown("Use the North Star to find direction..."),
                images = emptyList()
            )

            val dummySection2 = Section(
                id = "section2",
                title = "Navigation",
                articles = listOf(dummyArticle3)
            )

            return SurvivalContent(
                sections = listOf(dummySection1, dummySection2)
            )
        } catch (e: Exception) {
            throw DomainException.UnknownErrorException("Error loading survival content: ${e.message}")
        }
    }

    suspend fun searchSurvivalContent(query: String): List<SearchResult> {
        val results = mutableListOf<SearchResult>()
        val lowerCaseQuery = query.lowercase()

        getSurvivalContent().sections.forEach { section ->
            try {
                section.articles.forEach { article ->
                    if (article.title.lowercase()
                            .contains(lowerCaseQuery) || article.content.lowercase()
                            .contains(lowerCaseQuery)
                    ) {
                        results.add(
                            SearchResult(
                                title = article.title,
                                snippet = article.content, // Or a relevant snippet
                                articleId = article.id // Assuming articleId is sufficient for navigation
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                throw DomainException.UnknownErrorException("Error searching survival content: ${e.message}")
            }
        }
        return results
    }
}