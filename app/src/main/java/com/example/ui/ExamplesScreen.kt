package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.ExampleItem
import com.example.ui.model.PythonData
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.IndigoPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamplesScreen(
    onBack: () -> Unit,
    onRunExample: (String, String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedExample by remember { mutableStateOf<ExampleItem?>(null) }

    val categories = listOf(
        "All",
        "Basic Python",
        "Conditions",
        "Loops",
        "Lists",
        "Functions",
        "Object-Oriented Programming",
        "Exception Handling",
        "Tkinter GUI Programming"
    )

    val filteredExamples = if (selectedCategory == "All") {
        PythonData.examples
    } else {
        PythonData.examples.filter { it.category == selectedCategory }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Python Examples", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("examples_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Category Filter Row
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory),
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.background,
                indicator = {}
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        modifier = Modifier.padding(end = 8.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IndigoPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Examples List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredExamples) { example ->
                    ExampleCard(example = example, onClick = { selectedExample = example })
                }
            }
        }
    }

    // Example Detail Dialog
    if (selectedExample != null) {
        val ex = selectedExample!!
        AlertDialog(
            onDismissRequest = { selectedExample = null },
            title = {
                Column {
                    Text(ex.title, fontWeight = FontWeight.Bold)
                    Text(ex.category, style = MaterialTheme.typography.bodySmall, color = IndigoPrimary)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(ex.explanation, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))

                    if (ex.category == "Tkinter GUI Programming") {
                        TkinterVisualPreview(code = ex.code)
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text("Code:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkSurface, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = ex.code,
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val title = ex.title
                        val code = ex.code
                        selectedExample = null
                        onRunExample(title, code)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier.testTag("run_example_button")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (ex.category == "Tkinter GUI Programming") "Run / Preview" else "Run Example")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedExample = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun TkinterVisualPreview(code: String) {
    val titleMatch = Regex("""title\s*\(\s*["']([^"']+)["']\s*\)""").find(code)
    val windowTitle = titleMatch?.groupValues?.get(1) ?: "Tkinter Window"

    val labels = Regex("""Label\s*\([^)]*text\s*=\s*["']([^"']+)["']""").findAll(code).map { it.groupValues[1] }.toList()
    val buttons = Regex("""Button\s*\([^)]*text\s*=\s*["']([^"']+)["']""").findAll(code).map { it.groupValues[1] }.toList()
    val hasEntry = code.contains("Entry")
    val hasCheck = code.contains("Checkbutton")
    val hasRadio = code.contains("Radiobutton")
    val hasList = code.contains("Listbox")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📱 Tkinter Android Preview", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = IndigoPrimary)
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = IndigoPrimary.copy(alpha = 0.2f)
                ) {
                    Text("GUI Simulator", modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = IndigoPrimary)
                }
            }

            Divider()

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.background,
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = IndigoPrimary,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = windowTitle,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    if (labels.isEmpty() && buttons.isEmpty() && !hasEntry && !hasCheck && !hasRadio && !hasList) {
                        Text("Empty Tkinter Window", style = MaterialTheme.typography.bodySmall, color = Color.Gray, fontSize = 11.sp)
                    }

                    labels.forEach { lblText ->
                        Text(lblText, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, fontSize = 12.sp)
                    }

                    if (hasEntry) {
                        OutlinedTextField(
                            value = "Sample input...",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth().height(40.dp),
                            readOnly = true,
                            textStyle = TextStyle(fontSize = 11.sp)
                        )
                    }

                    if (hasCheck) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Checkbox(checked = true, onCheckedChange = {})
                            Text("Checkbox Option", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp)
                        }
                    }

                    if (hasRadio) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = true, onClick = {})
                                Text("Option 1", style = MaterialTheme.typography.bodySmall, fontSize = 10.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = false, onClick = {})
                                Text("Option 2", style = MaterialTheme.typography.bodySmall, fontSize = 10.sp)
                            }
                        }
                    }

                    if (hasList) {
                        Surface(
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(4.dp)) {
                                Text("• List Item 1", style = MaterialTheme.typography.bodySmall, fontSize = 10.sp)
                                Text("• List Item 2", style = MaterialTheme.typography.bodySmall, fontSize = 10.sp)
                            }
                        }
                    }

                    buttons.forEach { btnText ->
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(btnText, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExampleCard(example: ExampleItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = example.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = IndigoPrimary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = example.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = IndigoPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = example.explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
