package presentation.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

fun highlightTextAsAnnotatedString(text: String, query: String): AnnotatedString {
    if (query.isBlank()) {
        return AnnotatedString(text)
    }

    val lowerCaseText = text.toLowerCase()
    val lowerCaseQuery = query.toLowerCase()
    val result = buildAnnotatedString {
        var currentPosition = 0
        var nextMatch = lowerCaseText.indexOf(lowerCaseQuery, currentPosition)

        while (nextMatch != -1) {
            // Append text before the match
            append(text.substring(currentPosition, nextMatch))

            // Append the highlighted text
            withStyle(style = SpanStyle(color = Color.Red)) { // You can customize the highlight color
                append(text.substring(nextMatch, nextMatch + query.length))
            }

            // Move to the position after the match
            currentPosition = nextMatch + query.length
            nextMatch = lowerCaseText.indexOf(lowerCaseQuery, currentPosition)
        }

        // Append any remaining text after the last match
        append(text.substring(currentPosition))
    }
    return result
}