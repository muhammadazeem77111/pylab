package com.example.python

sealed class ExecutionState {
    object Idle : ExecutionState()
    object Running : ExecutionState()
    data class WaitingForInput(val prompt: String, val callback: (String) -> Unit) : ExecutionState()
    data class Success(val output: String) : ExecutionState()
    data class Error(val errorType: String, val message: String, val explanation: String, val lineNumber: Int?) : ExecutionState()
}
