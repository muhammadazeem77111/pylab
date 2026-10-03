package com.example.python

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

object PythonSyntaxHighlighter {
    private val keywords = setOf(
        "def", "return", "if", "elif", "else", "for", "in", "while",
        "import", "from", "class", "True", "False", "None", "and", "or",
        "not", "is", "break", "continue", "pass", "try", "except", "with", "as", "global", "nonlocal"
    )

    private val builtins = setOf(
        "print", "len", "range", "int", "str", "float", "list", "dict", "set", "input",
        "abs", "all", "any", "sum", "min", "max", "sorted", "enumerate", "zip", "map", "filter", "open", "type"
    )

    private val keywordColor = Color(0xFFC792EA) // Violet
    private val builtinColor = Color(0xFF82AAFF) // Blue
    private val stringColor = Color(0xFFC3E88D)  // Green
    private val commentColor = Color(0xFF63778D) // Comment Gray
    private val numberColor = Color(0xFFF78C6C)  // Orange
    private val textColor = Color.White

    fun highlight(text: String): AnnotatedString {
        if (text.isEmpty()) return AnnotatedString("")

        return buildAnnotatedString {
            append(text)

            // Default color
            addStyle(SpanStyle(color = textColor), 0, text.length)

            // Numbers
            val numberRegex = Regex("\\b\\d+\\.?\\d*\\b")
            numberRegex.findAll(text).forEach { match ->
                addStyle(SpanStyle(color = numberColor), match.range.first, match.range.last + 1)
            }

            // Words (Keywords & Builtins)
            val wordRegex = Regex("\\b[a-zA-Z_]\\w*\\b")
            wordRegex.findAll(text).forEach { match ->
                val word = match.value
                when {
                    word in keywords -> {
                        addStyle(SpanStyle(color = keywordColor, fontWeight = FontWeight.Bold), match.range.first, match.range.last + 1)
                    }
                    word in builtins -> {
                        addStyle(SpanStyle(color = builtinColor), match.range.first, match.range.last + 1)
                    }
                }
            }

            // Strings (double and single quotes)
            val stringRegex = Regex("\".*?\"|'.*?'")
            stringRegex.findAll(text).forEach { match ->
                addStyle(SpanStyle(color = stringColor), match.range.first, match.range.last + 1)
            }

            // Comments (starting with #)
            val commentRegex = Regex("#.*")
            commentRegex.findAll(text).forEach { match ->
                addStyle(SpanStyle(color = commentColor), match.range.first, match.range.last + 1)
            }
        }
    }
}
