package com.example.python

object PythonErrorHandler {
    data class ParsedError(val errorType: String, val message: String, val explanation: String, val lineNumber: Int?)

    fun analyze(exception: Exception, codeLines: List<String>): ParsedError {
        val msg = exception.message ?: "Unknown error"
        val stackTrace = exception.stackTrace
        var lineNum: Int? = null

        // Try to find line number from message or stack trace
        val lineMatch = Regex("line (\\d+)").find(msg)
        if (lineMatch != null) {
            lineNum = lineMatch.groupValues[1].toIntOrNull()
        }

        val errorTypeName = exception.javaClass.simpleName
        val friendlyExplanation = when {
            exception is NameError || msg.contains("NameError") || msg.contains("not defined") ->
                "You used a variable or function name before defining or spelling it correctly."
            exception is SyntaxError || msg.contains("SyntaxError") || msg.contains("invalid syntax") ->
                "There is a typo or missing punctuation (like a parenthesis, colon, or quote) in your code."
            exception is TypeError || msg.contains("TypeError") ->
                "You tried to perform an operation on incompatible data types (e.g. adding text and a number directly)."
            exception is ZeroDivisionError || msg.contains("division by zero") ->
                "You tried to divide a number by zero, which is not allowed in mathematics."
            exception is IndentationError || msg.contains("IndentationError") || msg.contains("indentation") ->
                "Your code indentation (spaces/tabs) is incorrect. Python relies on consistent indentation for blocks."
            exception is IndexError || msg.contains("IndexError") || msg.contains("list index out of range") ->
                "You tried to access an item in a list using an index position that does not exist."
            exception is ValueError || msg.contains("ValueError") ->
                "A function received an argument of the correct type but an inappropriate value (e.g. int('hello'))."
            else ->
                "Something went wrong while running your Python code. Check your syntax and logic."
        }

        return ParsedError(
            errorType = if (errorTypeName == "Exception" || errorTypeName == "RuntimeException") "PythonError" else errorTypeName,
            message = msg,
            explanation = friendlyExplanation,
            lineNumber = lineNum
        )
    }
}

class NameError(message: String) : Exception(message)
class SyntaxError(message: String) : Exception(message)
class TypeError(message: String) : Exception(message)
class ZeroDivisionError(message: String) : Exception(message)
class IndentationError(message: String) : Exception(message)
class IndexError(message: String) : Exception(message)
class ValueError(message: String) : Exception(message)
