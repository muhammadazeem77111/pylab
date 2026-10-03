package com.example.python

import android.util.Log

data class TkinterWidgetState(
    val id: String,
    val type: String, // Label, Button, Entry, Checkbutton, Radiobutton, Listbox
    var text: String,
    val command: String? = null
)

data class TkinterPreviewData(
    val title: String,
    val geometry: String?,
    val widgets: List<TkinterWidgetState>
)

object TkinterSimulator {
    fun isTkinterCode(code: String): Boolean {
        val trimmed = code.trim()
        return trimmed.contains("tkinter") ||
               trimmed.contains("tk.") ||
               trimmed.contains("Label(") ||
               trimmed.contains("Button(") ||
               trimmed.contains("Entry(") ||
               trimmed.contains("messagebox") ||
               trimmed.contains("mainloop")
    }

    fun parse(code: String): TkinterPreviewData {
        Log.d("PyLab", "[TkinterSimulator] Parsing Tkinter code...")
        val titleMatch = Regex("""title\s*\(\s*["']([^"']+)["']\s*\)""").find(code)
        val title = titleMatch?.groupValues?.get(1) ?: "Tkinter Window"

        val geomMatch = Regex("""geometry\s*\(\s*["']([^"']+)["']\s*\)""").find(code)
        val geometry = geomMatch?.groupValues?.get(1)

        val widgets = mutableListOf<TkinterWidgetState>()
        val lines = code.lines()
        for ((index, line) in lines.withIndex()) {
            val trimmed = line.trim()
            if (trimmed.contains("Label(")) {
                val textMatch = Regex("""text\s*=\s*["']([^"']*)["']""").find(trimmed)
                val lblText = textMatch?.groupValues?.get(1) ?: "Label"
                val varName = trimmed.split("=").firstOrNull()?.trim() ?: "label_$index"
                widgets.add(TkinterWidgetState(varName, "Label", lblText))
            } else if (trimmed.contains("Button(")) {
                val textMatch = Regex("""text\s*=\s*["']([^"']*)["']""").find(trimmed)
                val btnText = textMatch?.groupValues?.get(1) ?: "Button"
                val cmdMatch = Regex("""command\s*=\s*([a-zA-Z0-9_]+)""").find(trimmed)
                val cmdName = cmdMatch?.groupValues?.get(1)
                val varName = trimmed.split("=").firstOrNull()?.trim() ?: "btn_$index"
                widgets.add(TkinterWidgetState(varName, "Button", btnText, cmdName))
            } else if (trimmed.contains("Entry(")) {
                val varName = trimmed.split("=").firstOrNull()?.trim() ?: "entry_$index"
                widgets.add(TkinterWidgetState(varName, "Entry", ""))
            } else if (trimmed.contains("Checkbutton(")) {
                val textMatch = Regex("""text\s*=\s*["']([^"']*)["']""").find(trimmed)
                val cbText = textMatch?.groupValues?.get(1) ?: "Checkbutton"
                val varName = trimmed.split("=").firstOrNull()?.trim() ?: "cb_$index"
                widgets.add(TkinterWidgetState(varName, "Checkbutton", cbText))
            } else if (trimmed.contains("Radiobutton(")) {
                val textMatch = Regex("""text\s*=\s*["']([^"']*)["']""").find(trimmed)
                val rbText = textMatch?.groupValues?.get(1) ?: "Radiobutton"
                val varName = trimmed.split("=").firstOrNull()?.trim() ?: "rb_$index"
                widgets.add(TkinterWidgetState(varName, "Radiobutton", rbText))
            } else if (trimmed.contains("Listbox(")) {
                val varName = trimmed.split("=").firstOrNull()?.trim() ?: "list_$index"
                widgets.add(TkinterWidgetState(varName, "Listbox", "List Item"))
            }
        }

        if (widgets.isEmpty()) {
            widgets.add(TkinterWidgetState("default_lbl", "Label", "Tkinter Application Running"))
        }

        return TkinterPreviewData(title, geometry, widgets)
    }

    fun executeCommand(cmdName: String?, code: String, currentWidgets: List<TkinterWidgetState>, entryValues: Map<String, String>): List<TkinterWidgetState> {
        if (cmdName == null) return currentWidgets
        Log.d("PyLab", "[TkinterSimulator] Executing command: $cmdName")
        val lines = code.lines()
        var inFunc = false
        val updatedWidgets = currentWidgets.toMutableList()
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("def $cmdName(")) {
                inFunc = true
                continue
            }
            if (inFunc && trimmed.startsWith("def ")) {
                inFunc = false
            }
            if (inFunc) {
                if (trimmed.contains(".config(text=") || trimmed.contains(".configure(text=")) {
                    val valMatch = Regex("""text\s*=\s*["']([^"']*)["']""").find(trimmed)
                    if (valMatch != null) {
                        val newText = valMatch.groupValues[1]
                        for (i in updatedWidgets.indices) {
                            if (updatedWidgets[i].type == "Label") {
                                updatedWidgets[i] = updatedWidgets[i].copy(text = newText)
                                break
                            }
                        }
                    } else if (trimmed.contains("entry.get()") || trimmed.contains("e.get()") || trimmed.contains("get()")) {
                        val entryVal = entryValues.values.firstOrNull() ?: "Input Value"
                        for (i in updatedWidgets.indices) {
                            if (updatedWidgets[i].type == "Label") {
                                updatedWidgets[i] = updatedWidgets[i].copy(text = entryVal)
                                break
                            }
                        }
                    }
                }
            }
        }
        return updatedWidgets
    }
}
