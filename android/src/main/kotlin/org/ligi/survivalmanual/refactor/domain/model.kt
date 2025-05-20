package org.ligi.survivalmanual.refactor.domain

data class SurvivalContent(
    val title: String,
    val sections: List<Section>
)

data class Section(
    val title: String,
    val articles: List<Article>
)

data class Article(
    val title: String,
    val content: List<ArticleContent>,

    )

sealed class ArticleContent {
    data class Text(val text: String) : ArticleContent()
    data class Image(val imageUrl: String, val altText: String? = null) : ArticleContent()
    // Add other types of content as needed, e.g., lists, code blocks
}

data class SearchResult(
    val title: String,
    val snippet: String,
    val articleId: String // Or whatever identifier is used to navigate to the article
)

data class UserPreferences(
    val isNightModeEnabled: Boolean
)

