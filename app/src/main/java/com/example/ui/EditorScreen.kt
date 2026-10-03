package com.example.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.python.*
import com.example.ui.theme.DarkConsole
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.IndigoPrimary
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    initialCode: String? = null,
    initialTitle: String? = null,
    onBack: () -> Unit,
    onSaveProgram: suspend (String, String) -> Unit
) {
    var codeValue by remember { mutableStateOf(TextFieldValue(initialCode ?: "print(\"Hello, World!\")\n")) }
    var programTitle by remember { mutableStateOf(initialTitle ?: "My Python Script") }
    var executionState by remember { mutableStateOf<ExecutionState>(ExecutionState.Idle) }
    var consoleOutput by remember { mutableStateOf("Ready to run Python code...") }
    var userInputText by remember { mutableStateOf("") }
    var editorFontSize by remember { mutableStateOf(14.sp) }

    // Tkinter Simulator States
    var isTkinterMode by remember { mutableStateOf(false) }
    var tkinterPreviewData by remember { mutableStateOf<TkinterPreviewData?>(null) }
    var tkinterWidgetStates by remember { mutableStateOf<List<TkinterWidgetState>>(emptyList()) }
    var entryValues by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val engine = remember { PythonExecutionEngine(context) }
    val verticalEditorScroll = rememberScrollState()
    val horizontalEditorScroll = rememberScrollState()
    val consoleScrollState = rememberScrollState()

    var showSaveDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    fun insertSnippet(snippet: String) {
        val text = codeValue.text
        val cursor = codeValue.selection.start.coerceIn(0, text.length)
        val newText = text.substring(0, cursor) + snippet + text.substring(cursor)
        val newCursor = cursor + snippet.length
        codeValue = TextFieldValue(newText, TextRange(newCursor))
    }

    BackHandler {
        engine.cancel()
        onBack()
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Terminal, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(programTitle, fontWeight = FontWeight.Bold, maxLines = 1)
                        if (isTkinterMode) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = IndigoPrimary.copy(alpha = 0.2f)
                            ) {
                                Text("Tkinter GUI Mode", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = IndigoPrimary)
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        engine.cancel()
                        onBack()
                    }, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val formatted = PythonFormatter.format(codeValue.text)
                        codeValue = TextFieldValue(formatted, TextRange(formatted.length))
                        snackbarMessage = "Code auto-formatted!"
                    }, modifier = Modifier.testTag("format_button")) {
                        Icon(Icons.Default.AutoFixHigh, contentDescription = "Auto-Format Code", tint = IndigoPrimary)
                    }
                    IconButton(onClick = { showSettingsDialog = true }, modifier = Modifier.testTag("settings_button")) {
                        Icon(Icons.Default.Settings, contentDescription = "Editor Settings", tint = MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton(onClick = { showSaveDialog = true }, modifier = Modifier.testTag("save_program_button")) {
                        Icon(Icons.Default.Save, contentDescription = "Save Program", tint = IndigoPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Quick Snippets Menu Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚡ Snippets:",
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    val snippets = mapOf(
                        "for loop" to "for i in range(5):\n    print(i)\n",
                        "def function" to "def greet(name):\n    return f\"Hello, {name}!\"\n\nprint(greet(\"Student\"))\n",
                        "tkinter win" to "import tkinter as tk\n\nroot = tk.Tk()\nroot.title(\"My App\")\nroot.geometry(\"300x200\")\n\nlabel = tk.Label(root, text=\"Hello Students\")\nlabel.pack()\n\nroot.mainloop()\n",
                        "tkinter btn" to "import tkinter as tk\n\nroot = tk.Tk()\ndef click():\n    print(\"Clicked\")\n\nbtn = tk.Button(root, text=\"Click Me\", command=click)\nbtn.pack()\nroot.mainloop()\n"
                    )
                    snippets.forEach { (label, template) ->
                        AssistChip(
                            onClick = { insertSnippet(template) },
                            label = { Text(label, fontSize = 12.sp) },
                            modifier = Modifier.testTag("snippet_${label.replace(" ", "_")}")
                        )
                    }
                }
            }

            // Code Editor Area with Line Numbers, Syntax Highlighting & Auto-Indentation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(DarkSurface)
                    .padding(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    val lineCount = codeValue.text.lines().size.coerceAtLeast(1)
                    val lineNumbersStr = (1..lineCount).joinToString("\n") { it.toString() }
                    
                    Text(
                        text = lineNumbersStr,
                        fontFamily = FontFamily.Monospace,
                        fontSize = editorFontSize,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(end = 12.dp, top = 4.dp)
                            .widthIn(min = 28.dp),
                        textAlign = TextAlign.End
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .horizontalScroll(horizontalEditorScroll)
                            .verticalScroll(verticalEditorScroll)
                    ) {
                        BasicTextField(
                            value = codeValue,
                            onValueChange = { newValue ->
                                val oldText = codeValue.text
                                val newText = newValue.text
                                if (newText.length > oldText.length && newValue.selection.collapsed) {
                                    val cursor = newValue.selection.start
                                    if (cursor > 0 && newText[cursor - 1] == '\n') {
                                        val beforeCursor = newText.substring(0, cursor)
                                        val lines = beforeCursor.split("\n")
                                        if (lines.size >= 2) {
                                            val prevLine = lines[lines.size - 2]
                                            val indent = prevLine.takeWhile { it.isWhitespace() }
                                            val extraIndent = if (prevLine.trimEnd().endsWith(":")) "    " else ""
                                            val totalIndent = indent + extraIndent
                                            if (totalIndent.isNotEmpty()) {
                                                val updatedText = newText.substring(0, cursor) + totalIndent + newText.substring(cursor)
                                                val newCursor = cursor + totalIndent.length
                                                codeValue = TextFieldValue(updatedText, TextRange(newCursor))
                                                return@BasicTextField
                                            }
                                        }
                                    }
                                }
                                codeValue = newValue
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("code_editor_input"),
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = editorFontSize,
                                color = Color.White,
                                lineHeight = (editorFontSize.value * 1.4f).sp
                            ),
                            visualTransformation = VisualTransformation { text ->
                                TransformedText(PythonSyntaxHighlighter.highlight(text.text), OffsetMapping.Identity)
                            },
                            cursorBrush = SolidColor(IndigoPrimary)
                        )
                    }
                }
            }

            // Output Console Panel or Tkinter GUI Preview Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(DarkConsole)
                    .padding(12.dp)
            ) {
                if (isTkinterMode && tkinterPreviewData != null) {
                    TkinterPreview(
                        previewData = tkinterPreviewData!!,
                        codeText = codeValue.text,
                        onWidgetStateChanged = { updated -> tkinterWidgetStates = updated }
                    )
                } else {
                    // NORMAL OUTPUT CONSOLE
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(consoleScrollState)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "## OUTPUT CONSOLE",
                                style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary)
                            )
                            if (executionState is ExecutionState.Running) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = IndigoPrimary, strokeWidth = 2.dp)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = consoleOutput,
                            style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = Color.LightGray),
                            modifier = Modifier.testTag("console_output_text")
                        )

                        if (executionState is ExecutionState.WaitingForInput) {
                            val waitingState = executionState as ExecutionState.WaitingForInput
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = userInputText,
                                    onValueChange = { userInputText = it },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_prompt_field"),
                                    textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = Color.White),
                                    placeholder = { Text("Enter input value...", color = Color.Gray) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(onDone = {
                                        val inputVal = userInputText
                                        userInputText = ""
                                        consoleOutput += "\n$inputVal\n"
                                        executionState = ExecutionState.Idle
                                        waitingState.callback(inputVal)
                                    })
                                )
                                Button(
                                    onClick = {
                                        val inputVal = userInputText
                                        userInputText = ""
                                        consoleOutput += "\n$inputVal\n"
                                        executionState = ExecutionState.Idle
                                        waitingState.callback(inputVal)
                                    },
                                    modifier = Modifier.testTag("submit_input_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                                ) {
                                    Text("Submit")
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Action Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val code = codeValue.text
                            val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                            // Strict Tkinter Detection via TkinterSimulator object
                            val isTkinter = TkinterSimulator.isTkinterCode(code)

                            if (isTkinter) {
                                Log.d("PyLab", "[TkinterSimulator] Routing code to TkinterSimulator")
                                isTkinterMode = true
                                val previewData = TkinterSimulator.parse(code)
                                tkinterPreviewData = previewData
                                tkinterWidgetStates = previewData.widgets
                                consoleOutput = "[$timeStr] [TkinterSimulator] Initialized successfully. Window '${previewData.title}' rendered."
                                executionState = ExecutionState.Success("Tkinter Preview Active")
                            } else {
                                Log.d("PyLab", "[PythonExecutionEngine] Routing code to PythonExecutionEngine")
                                isTkinterMode = false
                                tkinterPreviewData = null
                                engine.cancel()
                                executionState = ExecutionState.Running
                                consoleOutput = if (consoleOutput == "Ready to run Python code..." || consoleOutput == "Console cleared.") {
                                    "[$timeStr] Running Python code...\n"
                                } else {
                                    "$consoleOutput\n\n[$timeStr] Running Python code...\n"
                                }

                                coroutineScope.launch {
                                    try {
                                        val result = engine.execute(code) { prompt ->
                                            consoleOutput += prompt
                                            kotlinx.coroutines.suspendCancellableCoroutine { cont ->
                                                executionState = ExecutionState.WaitingForInput(prompt) { inputStr ->
                                                    cont.resume(inputStr)
                                                }
                                            }
                                        }
                                        consoleOutput += "\n$result\n--- Program finished successfully ---"
                                        executionState = ExecutionState.Success(result)
                                    } catch (e: Exception) {
                                        val parsed = PythonErrorHandler.analyze(e, code.lines())
                                        var errText = "\n\nERROR: ${parsed.errorType}"
                                        if (parsed.lineNumber != null) {
                                            errText += " (Line ${parsed.lineNumber})"
                                        }
                                        errText += "\nDetails: ${parsed.message}"
                                        errText += "\n\n💡 Beginner Explanation:\n${parsed.explanation}"
                                        consoleOutput += errText
                                        executionState = ExecutionState.Error(parsed.errorType, parsed.message, parsed.explanation, parsed.lineNumber)
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("run_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isTkinterMode) "RUN / PREVIEW" else "RUN")
                    }

                    Button(
                        onClick = {
                            engine.cancel()
                            executionState = ExecutionState.Idle
                            isTkinterMode = false
                            tkinterPreviewData = null
                            consoleOutput = "Execution stopped by user."
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("stop_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("STOP")
                    }

                    OutlinedButton(
                        onClick = {
                            codeValue = TextFieldValue("")
                            consoleOutput = "Console cleared."
                            isTkinterMode = false
                            tkinterPreviewData = null
                            executionState = ExecutionState.Idle
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("clear_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("CLEAR")
                    }
                }
            }
        }
    }

    // Save Dialog
    if (showSaveDialog) {
        var titleInput by remember { mutableStateOf(programTitle) }
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Python Program") },
            text = {
                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = { Text("Program Title") },
                    singleLine = true,
                    modifier = Modifier.testTag("save_title_input")
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSaveDialog = false
                        if (titleInput.isNotBlank()) {
                            programTitle = titleInput
                            coroutineScope.launch {
                                onSaveProgram(programTitle, codeValue.text)
                                snackbarMessage = "Program saved successfully!"
                            }
                        }
                    },
                    modifier = Modifier.testTag("confirm_save_button")
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Settings Dialog
    if (showSettingsDialog) {
        var sliderValue by remember { mutableStateOf(editorFontSize.value) }
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Editor Settings") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Font Size: ${sliderValue.toInt()} sp", fontWeight = FontWeight.Bold)
                    Slider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        valueRange = 10f..24f,
                        steps = 14,
                        modifier = Modifier.testTag("font_size_slider")
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        editorFontSize = sliderValue.sp
                        showSettingsDialog = false
                    },
                    modifier = Modifier.testTag("confirm_settings_button")
                ) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
