package com.example.python

import android.content.Context
import com.chaquo.python.Python
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class KotlinStdinReader(private val inputProvider: suspend (String) -> String) {
    fun readline(): String {
        val inputStr = runBlocking(Dispatchers.Main) {
            inputProvider("")
        }
        return inputStr + "\n"
    }
}

class PythonExecutionEngine(private val context: Context) {

    private var isCancelled = false

    fun cancel() {
        isCancelled = true
    }

    suspend fun execute(
        code: String,
        inputProvider: suspend (String) -> String
    ): String = withContext(Dispatchers.IO) {
        isCancelled = false

        // Python runtime is initialized exactly once in PyLabApplication onCreate
        val py = Python.getInstance()
        val sys = py.getModule("sys")
        val ioModule = py.getModule("io")
        val builtins = py.getModule("builtins")

        // Create fresh StringIO buffers for stdout/stderr for every run
        val stdoutBuf = ioModule.callAttr("StringIO")
        val stderrBuf = ioModule.callAttr("StringIO")

        val oldStdout = sys.get("stdout")
        val oldStderr = sys.get("stderr")
        val oldStdin = sys.get("stdin")

        sys.put("stdout", stdoutBuf)
        sys.put("stderr", stderrBuf)
        sys.put("stdin", KotlinStdinReader(inputProvider))

        // Force a fresh execution context (brand new globals dictionary) for every RUN trigger
        val globals = builtins.callAttr("dict")

        try {
            // Execute user code with fresh global and local dictionaries to prevent missing frame errors or stale state
            builtins.callAttr("exec", code, globals, globals)

            val outVal = stdoutBuf.callAttr("getvalue").toString()
            val errVal = stderrBuf.callAttr("getvalue").toString()

            val combined = buildString {
                if (outVal.isNotEmpty()) append(outVal)
                if (errVal.isNotEmpty()) {
                    if (isNotEmpty()) append("\n")
                    append(errVal)
                }
            }
            combined
        } catch (e: Exception) {
            val errVal = stderrBuf.callAttr("getvalue").toString()
            val message = e.message ?: "Execution Error"
            if (errVal.isNotEmpty()) {
                throw Exception(errVal)
            } else {
                throw Exception(message)
            }
        } finally {
            sys.put("stdout", oldStdout)
            sys.put("stderr", oldStderr)
            sys.put("stdin", oldStdin)
        }
    }
}
