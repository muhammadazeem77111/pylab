package com.example.python

object PythonFormatter {
    fun format(code: String): String {
        if (code.isBlank()) return code

        val lines = code.lines()
        val formattedLines = mutableListOf<String>()
        var indentLevel = 0
        val indentSize = 4

        for (rawLine in lines) {
            val trimmed = rawLine.trim()
            if (trimmed.isEmpty()) {
                formattedLines.add("")
                continue
            }

            // Check if this line should reduce indent before adding
            if (trimmed.startsWith("else:") || trimmed.startsWith("elif ") || 
                trimmed.startsWith("except:") || trimmed.startsWith("finally:")) {
                indentLevel = (indentLevel - 1).coerceAtLeast(0)
            }

            val currentIndent = " ".repeat(indentLevel * indentSize)
            formattedLines.add(currentIndent + trimmed)

            // Increase indent for next line if current line ends with a colon
            if (trimmed.endsWith(":")) {
                indentLevel++
            }
        }

        return formattedLines.joinToString("\n") + "\n"
    }
}
