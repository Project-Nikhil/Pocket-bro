package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoteItem
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.BackgroundWarmDark
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextCream
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotesScreen(
    notes: List<NoteItem>,
    onAddNote: (String, String, String, Boolean) -> Unit,
    onDeleteNote: (NoteItem) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedNoteForViewing by remember { mutableStateOf<NoteItem?>(null) }

    val filteredNotes = notes.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.content.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWarmDark)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Header matching mockup
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("notes_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextCream
                )
            }
            Text(
                text = "Notes & Diary",
                color = TextCream,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SurfaceCardElevated)
                    .testTag("notes_add_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note",
                    tint = TextCream
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search notes or diary...", color = TextSubtle) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextMuted
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceCardDark,
                unfocusedContainerColor = SurfaceCardDark,
                focusedBorderColor = AccentAmber,
                unfocusedBorderColor = SurfaceBorder,
                focusedTextColor = TextCream,
                unfocusedTextColor = TextCream
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Notes List matching mockup
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredNotes, key = { it.id }) { note ->
                val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                val dateStr = dateFormat.format(Date(note.updatedAt))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceCardDark)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                        .clickable { selectedNoteForViewing = note }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (note.isDiary) Color(0xFF3B1E28) else Color(0xFF1E2E42)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (note.isDiary) Icons.Default.Favorite else Icons.Default.Description,
                            contentDescription = note.title,
                            tint = if (note.isDiary) Color(0xFFF472B6) else Color(0xFF60A5FA),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = note.title,
                                color = TextCream,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (note.isPinned) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.PushPin,
                                    contentDescription = "Pinned",
                                    tint = AccentAmber,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = dateStr,
                            color = TextMuted,
                            fontSize = 11.5.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Open",
                        tint = TextSubtle,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }

    // Add Note Dialog
    if (showDialog) {
        var newTitle by remember { mutableStateOf("") }
        var newContent by remember { mutableStateOf("") }
        var newCategory by remember { mutableStateOf("Thoughts") }
        var isDiary by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("New Note / Diary Entry", color = TextCream) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Title") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextCream,
                            unfocusedTextColor = TextCream
                        )
                    )
                    OutlinedTextField(
                        value = newContent,
                        onValueChange = { newContent = it },
                        label = { Text("Content") },
                        minLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextCream,
                            unfocusedTextColor = TextCream
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isDiary,
                            onCheckedChange = { isDiary = it },
                            colors = CheckboxDefaults.colors(checkedColor = AccentAmber)
                        )
                        Text("Mark as Private Diary Entry", color = TextCream, fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newContent.isNotBlank() || newTitle.isNotBlank()) {
                            onAddNote(newTitle, newContent, newCategory, isDiary)
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmber)
                ) {
                    Text("Save", color = Color(0xFF261405))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = SurfaceCardElevated
        )
    }

    // View Note Dialog
    selectedNoteForViewing?.let { note ->
        AlertDialog(
            onDismissRequest = { selectedNoteForViewing = null },
            title = { Text(note.title, color = AccentAmber, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = note.content,
                        color = TextCream,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedNoteForViewing = null }) {
                    Text("Close", color = AccentAmber)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDeleteNote(note)
                    selectedNoteForViewing = null
                }) {
                    Text("Delete", color = Color(0xFFF87171))
                }
            },
            containerColor = SurfaceCardElevated
        )
    }
}
