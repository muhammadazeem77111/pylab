package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.data.AppDatabase
import com.example.data.ProgramEntity
import com.example.data.ProgramRepository
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

sealed class Screen {
    object Welcome : Screen()
    object Home : Screen()
    data class Editor(val code: String? = null, val title: String? = null) : Screen()
    object Examples : Screen()
    object Practice : Screen()
    object Saved : Screen()
}

class MainActivity : ComponentActivity() {
    private lateinit var repository: ProgramRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getInstance(applicationContext)
        repository = ProgramRepository(db.programDao())

        val prefs = getSharedPreferences("python_ide_prefs", Context.MODE_PRIVATE)
        val hasSeenWelcome = prefs.getBoolean("has_seen_welcome", false)

        setContent {
            MyApplicationTheme {
                var currentScreen by remember {
                    mutableStateOf<Screen>(if (hasSeenWelcome) Screen.Home else Screen.Welcome)
                }

                var editorCode by remember { mutableStateOf<String?>(null) }
                var editorTitle by remember { mutableStateOf<String?>(null) }

                val savedPrograms by repository.allPrograms.collectAsState(initial = emptyList())
                val coroutineScope = rememberCoroutineScope()

                when (val screen = currentScreen) {
                    is Screen.Welcome -> {
                        WelcomeScreen(
                            onStartClicked = {
                                prefs.edit().putBoolean("has_seen_welcome", true).apply()
                                currentScreen = Screen.Home
                            }
                        )
                    }
                    is Screen.Home -> {
                        HomeScreen(
                            onNavigateToEditor = { code, title ->
                                editorCode = code
                                editorTitle = title
                                currentScreen = Screen.Editor(code, title)
                            },
                            onNavigateToExamples = { currentScreen = Screen.Examples },
                            onNavigateToPractice = { currentScreen = Screen.Practice },
                            onNavigateToSaved = { currentScreen = Screen.Saved }
                        )
                    }
                    is Screen.Editor -> {
                        EditorScreen(
                            initialCode = screen.code,
                            initialTitle = screen.title,
                            onBack = { currentScreen = Screen.Home },
                            onSaveProgram = { title, code ->
                                repository.save(ProgramEntity(title = title, code = code))
                            }
                        )
                    }
                    is Screen.Examples -> {
                        ExamplesScreen(
                            onBack = { currentScreen = Screen.Home },
                            onRunExample = { title, code ->
                                currentScreen = Screen.Editor(code = code, title = title)
                            }
                        )
                    }
                    is Screen.Practice -> {
                        PracticeScreen(
                            onBack = { currentScreen = Screen.Home },
                            onWriteCode = { title, code ->
                                currentScreen = Screen.Editor(code = code, title = title)
                            }
                        )
                    }
                    is Screen.Saved -> {
                        SavedProgramsScreen(
                            programs = savedPrograms,
                            onBack = { currentScreen = Screen.Home },
                            onOpenProgram = { title, code ->
                                currentScreen = Screen.Editor(code = code, title = title)
                            },
                            onDeleteProgram = { id ->
                                coroutineScope.launch {
                                    repository.delete(id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
