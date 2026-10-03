package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.python.TkinterPreviewData
import com.example.python.TkinterWidgetState
import com.example.python.TkinterSimulator
import com.example.ui.theme.IndigoPrimary

@Composable
fun TkinterPreview(
    previewData: TkinterPreviewData,
    codeText: String,
    onWidgetStateChanged: (List<TkinterWidgetState>) -> Unit
) {
    var widgetStates by remember(previewData) { mutableStateOf(previewData.widgets) }
    var entryValues by remember(previewData) { mutableStateOf<Map<String, String>>(emptyMap()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "## TKINTER GUI PREVIEW",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndigoPrimary)
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = IndigoPrimary.copy(alpha = 0.2f)
            ) {
                Text("Simulator Active", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = IndigoPrimary)
            }
        }

        // Simulated Window Container using Box, Column, Row, Button
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.4f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Window Title Bar
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = IndigoPrimary,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = previewData.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            if (previewData.geometry != null) {
                                Text(
                                    text = "Size: ${previewData.geometry}",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Dynamic Widget Mapping using Box, Column, Row, Button
                    widgetStates.forEach { widget ->
                        when (widget.type) {
                            "Label" -> {
                                Text(
                                    text = widget.text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            "Button" -> {
                                Button(
                                    onClick = {
                                        val updated = TkinterSimulator.executeCommand(widget.command, codeText, widgetStates, entryValues)
                                        widgetStates = updated
                                        onWidgetStateChanged(updated)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                    modifier = Modifier.height(38.dp)
                                ) {
                                    Text(widget.text, fontSize = 12.sp)
                                }
                            }
                            "Entry" -> {
                                val currentVal = entryValues[widget.id] ?: ""
                                OutlinedTextField(
                                    value = currentVal,
                                    onValueChange = { newVal ->
                                        entryValues = entryValues + (widget.id to newVal)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    placeholder = { Text("Enter text here...", fontSize = 12.sp) },
                                    textStyle = TextStyle(fontSize = 12.sp),
                                    singleLine = true
                                )
                            }
                            "Checkbutton" -> {
                                var checked by remember { mutableStateOf(false) }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Checkbox(checked = checked, onCheckedChange = { checked = it })
                                    Text(widget.text, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            "Radiobutton" -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    RadioButton(selected = true, onClick = {})
                                    Text(widget.text, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            "Listbox" -> {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(6.dp)) {
                                        Text("• Python Programming", style = MaterialTheme.typography.bodySmall)
                                        Text("• Tkinter GUI Design", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
