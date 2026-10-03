package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.VioletSecondary
import com.example.ui.theme.AmberAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToEditor: (String?, String?) -> Unit,
    onNavigateToExamples: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToSaved: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("PyLab", fontWeight = FontWeight.Bold)
                        Text("Python Practice on Mobile", style = MaterialTheme.typography.labelSmall, color = IndigoPrimary)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showAboutDialog = true },
                        modifier = Modifier.testTag("about_button")
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "About PyLab", tint = MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton(
                        onClick = onNavigateToSaved,
                        modifier = Modifier.testTag("saved_programs_button")
                    ) {
                        Icon(Icons.Default.Folder, contentDescription = "My Programs", tint = IndigoPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Banner Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Learn • Write • Run Python",
                        style = MaterialTheme.typography.titleMedium,
                        color = IndigoPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Python Practice Made Easy",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Write real Python code, test snippets, solve 15+ practice questions, and explore interactive examples offline.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Main Action Cards
            HomeFeatureCard(
                title = "Start Coding",
                subtitle = "Open Python editor, write code, run & see console output",
                icon = Icons.Default.Code,
                iconTint = IndigoPrimary,
                testTag = "start_coding_card",
                onClick = { onNavigateToEditor(null, null) }
            )

            HomeFeatureCard(
                title = "Python Examples",
                subtitle = "Browse curated beginner examples across basic, loops, lists & functions",
                icon = Icons.Default.MenuBook,
                iconTint = VioletSecondary,
                testTag = "examples_card",
                onClick = onNavigateToExamples
            )

            HomeFeatureCard(
                title = "Practice Questions",
                subtitle = "Test your skills with 15+ guided practice problems with hints & solutions",
                icon = Icons.Default.Quiz,
                iconTint = AmberAccent,
                testTag = "practice_card",
                onClick = onNavigateToPractice
            )

            HomeFeatureCard(
                title = "My Programs",
                subtitle = "Open, edit, run, and manage your saved Python scripts",
                icon = Icons.Default.FolderSpecial,
                iconTint = IndigoPrimary,
                testTag = "my_programs_card",
                onClick = onNavigateToSaved
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Developer Footer Credit
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Developed by",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Muhammad Azeem",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = IndigoPrimary
                )
            }
        }
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Column {
                    Text("PyLab", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = IndigoPrimary)
                    Text("Python Practice on Mobile", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "PyLab is designed to help students learn and practice Python programming directly on their Android devices, especially students who do not have regular access to a computer or laptop.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Divider()
                    Column {
                        Text("Developed by", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text("Muhammad Azeem", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = IndigoPrimary)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showAboutDialog = false },
                    modifier = Modifier.testTag("close_about_button")
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun HomeFeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
