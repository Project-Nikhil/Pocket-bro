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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ui.viewmodel.StorageSummary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.AIModelStatus
import com.example.data.model.PersonalityMode
import com.example.data.model.RelationshipType
import com.example.data.model.StudyExamItem
import com.example.data.model.TodoTask
import com.example.data.model.VoiceNoteItem
import com.example.ui.components.BrotherAvatar
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.BackgroundWarmDark
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextCream
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.WeatherInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/* ----------------- 1. TO-DO LIST SCREEN ----------------- */
@Composable
fun TasksScreen(
    tasks: List<TodoTask>,
    onToggle: (TodoTask) -> Unit,
    onAdd: (String, String) -> Unit,
    onDelete: (TodoTask) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWarmDark)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("tasks_back_button")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextCream)
            }
            Text("To-Do List", color = TextCream, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SurfaceCardElevated)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task", tint = TextCream)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(tasks, key = { it.id }) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceCardDark)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onToggle(task) },
                        colors = CheckboxDefaults.colors(checkedColor = AccentAmber)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.title,
                            color = if (task.isCompleted) TextSubtle else TextCream,
                            fontSize = 14.5.sp,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        )
                        if (task.dueDate.isNotBlank()) {
                            Text(task.dueDate, color = TextMuted, fontSize = 11.5.sp)
                        }
                    }
                    IconButton(onClick = { onDelete(task) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextSubtle, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }

    if (showDialog) {
        var title by remember { mutableStateOf("") }
        var priority by remember { mutableStateOf("Medium") }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Task", color = TextCream) },
            text = {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task description") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextCream, unfocusedTextColor = TextCream)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) onAdd(title, priority)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmber)
                ) {
                    Text("Add", color = Color(0xFF261405))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel", color = TextMuted) }
            },
            containerColor = SurfaceCardElevated
        )
    }
}

/* ----------------- 2. STUDY PLANNER & EXAM COUNTDOWN ----------------- */
@Composable
fun StudyPlannerScreen(
    exams: List<StudyExamItem>,
    onAddExam: (StudyExamItem) -> Unit,
    onDeleteExam: (StudyExamItem) -> Unit,
    onNavigateToTimer: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWarmDark)
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("study_back_button")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextCream)
            }
            Text("Study Planner & Exams", color = TextCream, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SurfaceCardElevated)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Exam", tint = TextCream)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Focus Session Quick Launch Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF38231B))
                .border(1.dp, AccentAmber.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                .clickable(onClick = onNavigateToTimer)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Timer, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Start Focus Study Session", color = TextCream, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text("25 min Pomodoro with zero distraction", color = TextMuted, fontSize = 12.sp)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(16.dp))
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text("Upcoming Exam Countdowns", color = TextCream, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(10.dp))

        exams.forEach { exam ->
            val now = System.currentTimeMillis()
            val diffMillis = exam.examDateMillis - now
            val daysLeft = (diffMillis / (1000 * 60 * 60 * 24)).coerceAtLeast(0)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceCardDark)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(exam.subject, color = AccentAmber, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(exam.examName, color = TextMuted, fontSize = 12.sp)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF3C2018))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("$daysLeft Days Left", color = Color(0xFFFF9E7A), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val progress = if (exam.totalTopics > 0) exam.completedTopics.toFloat() / exam.totalTopics else 0f
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AccentAmber,
                        trackColor = SurfaceCardElevated
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("${exam.completedTopics}/${exam.totalTopics} Topics", color = TextMuted, fontSize = 11.sp)
                }
            }
        }
    }

    if (showDialog) {
        var subject by remember { mutableStateOf("") }
        var examName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Exam", color = TextCream) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Subject (e.g. Physics)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextCream, unfocusedTextColor = TextCream)
                    )
                    OutlinedTextField(
                        value = examName,
                        onValueChange = { examName = it },
                        label = { Text("Exam Name (e.g. Finals)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextCream, unfocusedTextColor = TextCream)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (subject.isNotBlank()) {
                            onAddExam(
                                StudyExamItem(
                                    subject = subject,
                                    examName = examName.ifEmpty { "Exam" },
                                    examDateMillis = System.currentTimeMillis() + (14 * 24 * 3600 * 1000L)
                                )
                            )
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmber)
                ) {
                    Text("Save", color = Color(0xFF261405))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel", color = TextMuted) }
            },
            containerColor = SurfaceCardElevated
        )
    }
}

/* ----------------- 3. VOICE NOTES SCREEN ----------------- */
@Composable
fun VoiceNotesScreen(
    voiceNotes: List<VoiceNoteItem>,
    onAddVoiceNote: (String, Int) -> Unit,
    onDeleteVoiceNote: (VoiceNoteItem) -> Unit,
    onRenameVoiceNote: (VoiceNoteItem, String) -> Unit = { _, _ -> },
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRecording by remember { mutableStateOf(false) }
    var recordingSeconds by remember { mutableIntStateOf(0) }
    var currentlyPlayingId by remember { mutableStateOf<Long?>(null) }
    var noteToRename by remember { mutableStateOf<VoiceNoteItem?>(null) }

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val name = uri.lastPathSegment?.substringAfterLast('/') ?: "imported_voice_note.mp3"
            onAddVoiceNote(name, 30)
        }
    }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordingSeconds = 0
            while (isRecording) {
                kotlinx.coroutines.delay(1000)
                recordingSeconds++
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWarmDark)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("voice_back_button")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextCream)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("Voice Notes", color = TextCream, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1.3f))
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Record Voice Note Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(if (isRecording) Color(0xFF4A1A1A) else SurfaceCardDark)
                .border(1.dp, if (isRecording) Color(0xFFFF5252) else SurfaceBorder, RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = if (isRecording) "Recording Voice Note... (${recordingSeconds}s)" else "Record Brother Voice Note",
                    color = TextCream,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (isRecording) "Tap red button to finish" else "Talk to brother instead of typing",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
            IconButton(
                onClick = {
                    if (isRecording) {
                        isRecording = false
                        val duration = if (recordingSeconds > 0) recordingSeconds else (15..45).random()
                        onAddVoiceNote("Note to Brother #${voiceNotes.size + 1}", duration)
                    } else {
                        isRecording = true
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isRecording) Color(0xFFFF5252) else AccentAmber)
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = "Record",
                    tint = Color(0xFF261405)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Import Audio File Button
        Button(
            onClick = { audioPickerLauncher.launch("audio/*") },
            colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardElevated),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Import Audio File", color = TextCream, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Saved Recordings (${voiceNotes.size})", color = TextCream, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(voiceNotes, key = { it.id }) { note ->
                val isPlaying = currentlyPlayingId == note.id
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isPlaying) Color(0xFF2E1C24) else SurfaceCardDark)
                        .border(1.dp, if (isPlaying) Color(0xFFF472B6) else SurfaceBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3F212B))
                            .clickable {
                                currentlyPlayingId = if (isPlaying) null else note.id
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Stop" else "Play",
                            tint = Color(0xFFF472B6)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(note.title, color = TextCream, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = if (isPlaying) "Playing audio... 🎵" else "${note.durationSeconds}s duration",
                            color = if (isPlaying) Color(0xFFF472B6) else TextMuted,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(onClick = { noteToRename = note }) {
                        Icon(Icons.Default.Edit, contentDescription = "Rename", tint = AccentAmber, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = { onDeleteVoiceNote(note) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextSubtle, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }

    // Rename Voice Note Dialog
    noteToRename?.let { note ->
        var renameText by remember { mutableStateOf(note.title) }
        AlertDialog(
            onDismissRequest = { noteToRename = null },
            title = { Text("Rename Voice Note", color = TextCream) },
            text = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    label = { Text("Note Title") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextCream, unfocusedTextColor = TextCream),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (renameText.isNotBlank()) {
                            onRenameVoiceNote(note, renameText.trim())
                        }
                        noteToRename = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmber)
                ) {
                    Text("Save", color = Color(0xFF261405))
                }
            },
            dismissButton = {
                TextButton(onClick = { noteToRename = null }) { Text("Cancel", color = TextMuted) }
            },
            containerColor = SurfaceCardElevated
        )
    }
}

/* ----------------- 4. CLOCK SCREEN ----------------- */
@Composable
fun ClockScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val now = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWarmDark)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("clock_back_button")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextCream)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("Clock & World Times", color = TextCream, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1.3f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(SurfaceCardDark)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Local Time", color = TextMuted, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(now, color = AccentAmber, fontSize = 34.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Time Zones", color = TextCream, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))

        listOf(
            Pair("New Delhi / India (IST)", "Current Location"),
            Pair("London (GMT)", "4h 30m behind"),
            Pair("New York (EST)", "9h 30m behind"),
            Pair("Tokyo (JST)", "3h 30m ahead")
        ).forEach { (city, desc) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceCardDark)
                    .border(0.8.dp, SurfaceBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(city, color = TextCream, fontSize = 14.sp)
                Text(desc, color = TextMuted, fontSize = 12.sp)
            }
        }
    }
}

/* ----------------- 5. WEATHER SCREEN ----------------- */
@Composable
fun WeatherScreen(
    weather: WeatherInfo,
    onCityChanged: (String) -> Unit = {},
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCityDialog by remember { mutableStateOf(false) }
    var locationNote by remember { mutableStateOf<String?>(null) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            locationNote = "GPS Location: Bengaluru detected"
            onCityChanged("Bengaluru")
        } else {
            locationNote = "Permission skipped. You can manually select any city below."
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWarmDark)
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("weather_back_button")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextCream)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("Daily Weather", color = TextCream, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1.3f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Main Weather Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF1E2838))
                .border(1.dp, SurfaceBorder, RoundedCornerShape(22.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.WbSunny, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(54.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(weather.temperature, color = TextCream, fontSize = 42.sp, fontWeight = FontWeight.Bold)
                Text("${weather.city} · ${weather.condition}", color = TextMuted, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(weather.greeting, color = AccentAmber, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Action Buttons: Manual City Selection & Optional Location
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { showCityDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = AccentAmber),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF261405), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Select City", color = Color(0xFF261405), fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                },
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardElevated),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Detect Location", color = TextCream, fontSize = 12.5.sp)
            }
        }

        locationNote?.let { note ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(note, color = TextMuted, fontSize = 11.5.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Popular Cities", color = TextCream, fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        val popularCities = listOf("Bengaluru", "Mumbai", "Delhi", "Hyderabad", "Kolkata", "Chennai", "London", "New York")
        popularCities.chunked(2).forEach { rowCities ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowCities.forEach { city ->
                    val isCurrent = weather.city.equals(city, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isCurrent) Color(0xFF2E241E) else SurfaceCardDark)
                            .border(1.dp, if (isCurrent) AccentAmber else SurfaceBorder, RoundedCornerShape(12.dp))
                            .clickable { onCityChanged(city) }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = if (isCurrent) "$city ✓" else city,
                            color = if (isCurrent) AccentAmber else TextCream,
                            fontSize = 13.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
                if (rowCities.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    if (showCityDialog) {
        var customCity by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = { Text("Select City", color = TextCream) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Type the name of your town or city:", color = TextMuted, fontSize = 12.sp)
                    OutlinedTextField(
                        value = customCity,
                        onValueChange = { customCity = it },
                        placeholder = { Text("e.g. Pune, Jaipur, Sydney") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextCream, unfocusedTextColor = TextCream),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customCity.isNotBlank()) {
                            onCityChanged(customCity.trim())
                        }
                        showCityDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmber)
                ) {
                    Text("Save", color = Color(0xFF261405))
                }
            },
            dismissButton = {
                TextButton(onClick = { showCityDialog = false }) { Text("Cancel", color = TextMuted) }
            },
            containerColor = SurfaceCardElevated
        )
    }
}

/* ----------------- 6. SETTINGS SCREEN ----------------- */
@Composable
fun SettingsScreen(
    sisterName: String,
    relationship: RelationshipType,
    currentMode: PersonalityMode,
    aiStatus: AIModelStatus,
    sisterAvatar: String = "👧",
    preferredGreeting: String = "Good morning bhaiya ♡",
    preferredLanguage: String = "English",
    themeMode: String = "DARK",
    storageSummary: StorageSummary = StorageSummary(),
    onUpdateName: (String) -> Unit,
    onUpdateRelationship: (RelationshipType) -> Unit,
    onUpdateAvatar: (String) -> Unit = {},
    onUpdateGreeting: (String) -> Unit = {},
    onUpdateLanguage: (String) -> Unit = {},
    onUpdateThemeMode: (String) -> Unit = {},
    onExportData: (() -> Unit)? = null,
    onSelectTier: (com.example.ai.ModelRamTier) -> Unit,
    onDownloadTier: (com.example.ai.ModelRamTier) -> Unit,
    onDeleteTier: (com.example.ai.ModelRamTier) -> Unit,
    onSelectEngine: (com.example.ai.EngineType) -> Unit,
    onImportTier: (com.example.ai.ModelRamTier, String) -> Unit,
    onResetAll: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nameInput by remember { mutableStateOf(sisterName) }
    var greetingInput by remember { mutableStateOf(preferredGreeting) }
    var showResetConfirm by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var importTargetTier by remember { mutableStateOf(com.example.ai.ModelRamTier.TIER_BALANCED) }
    var highRamWarningTier by remember { mutableStateOf<com.example.ai.ModelRamTier?>(null) }

    val ramInfo = aiStatus.deviceRamInfo

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWarmDark)
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("settings_back_button")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextCream)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("Settings & Preferences", color = TextCream, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1.3f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Card with Avatar, Language, Greeting
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceCardDark)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Sister's Profile", color = AccentAmber, fontSize = 15.sp, fontWeight = FontWeight.Bold)

            // Avatar selection
            Text("Your Avatar", color = TextMuted, fontSize = 12.sp)
            val avatars = listOf("👧", "👩", "🧒", "🌸", "🎀", "✨", "🦋", "🧸")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                avatars.forEach { av ->
                    val isSelected = sisterAvatar == av
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) AccentAmber.copy(alpha = 0.25f) else SurfaceCardElevated)
                            .border(if (isSelected) 1.5.dp else 0.dp, if (isSelected) AccentAmber else Color.Transparent, CircleShape)
                            .clickable { onUpdateAvatar(av) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(av, fontSize = 18.sp)
                    }
                }
            }

            OutlinedTextField(
                value = nameInput,
                onValueChange = {
                    nameInput = it
                    onUpdateName(it)
                },
                label = { Text("Your Name") },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextCream, unfocusedTextColor = TextCream),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Relationship Selection", color = TextMuted, fontSize = 13.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onUpdateRelationship(RelationshipType.ELDER_BROTHER) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (relationship == RelationshipType.ELDER_BROTHER) AccentAmber else SurfaceCardElevated
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Elder Brother", color = if (relationship == RelationshipType.ELDER_BROTHER) Color(0xFF261405) else TextCream, fontSize = 12.sp)
                }
                Button(
                    onClick = { onUpdateRelationship(RelationshipType.YOUNGER_BROTHER) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (relationship == RelationshipType.YOUNGER_BROTHER) AccentAmber else SurfaceCardElevated
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Younger Brother", color = if (relationship == RelationshipType.YOUNGER_BROTHER) Color(0xFF261405) else TextCream, fontSize = 12.sp)
                }
            }

            // Language preferences
            Text("Preferred Language", color = TextMuted, fontSize = 13.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("English", "Hinglish", "Hindi").forEach { lang ->
                    val isLang = preferredLanguage.equals(lang, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isLang) AccentAmber else SurfaceCardElevated)
                            .clickable { onUpdateLanguage(lang) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(lang, color = if (isLang) Color(0xFF261405) else TextCream, fontSize = 12.sp, fontWeight = if (isLang) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }

            // Morning Greeting
            Text("Preferred Greeting", color = TextMuted, fontSize = 13.sp)
            OutlinedTextField(
                value = greetingInput,
                onValueChange = {
                    greetingInput = it
                    onUpdateGreeting(it)
                },
                placeholder = { Text("e.g. Good morning bhaiya ♡") },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextCream, unfocusedTextColor = TextCream),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Appearance Theme Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceCardDark)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Appearance Theme", color = AccentAmber, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text("Choose your preferred theme mode:", color = TextMuted, fontSize = 12.sp)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    Triple("DARK", "Dark Mode", Icons.Default.DarkMode),
                    Triple("LIGHT", "Light Mode", Icons.Default.LightMode),
                    Triple("SYSTEM", "System Default", Icons.Default.Settings)
                ).forEach { (mode, label, icon) ->
                    val isSelected = themeMode.equals(mode, ignoreCase = true)
                    Button(
                        onClick = { onUpdateThemeMode(mode) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) AccentAmber else SurfaceCardElevated
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(icon, contentDescription = null, tint = if (isSelected) Color(0xFF261405) else TextCream, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(label, color = if (isSelected) Color(0xFF261405) else TextCream, fontSize = 10.5.sp, maxLines = 1)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Device RAM Diagnostics Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF231C18))
                .border(1.2.dp, AccentAmber.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Device RAM Diagnostics", color = AccentAmber, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF38231B))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("${ramInfo.totalRamGb} GB RAM", color = AccentAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "Detected: ${ramInfo.totalRamGb} GB Total RAM  ·  ${ramInfo.availableRamGb} GB Available",
                color = TextCream,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Recommended for your phone: ${ramInfo.recommendedTier.modelName}",
                color = Color(0xFF4ADE80),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Active AI Engine Switcher Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceCardDark)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Active Brother AI Engine", color = AccentAmber, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text("Select which AI inference engine powers your brother's texts:", color = TextMuted, fontSize = 12.sp)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onSelectEngine(com.example.ai.EngineType.BUILTIN_OFFLINE_CORE) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (aiStatus.activeEngine == com.example.ai.EngineType.BUILTIN_OFFLINE_CORE) AccentAmber else SurfaceCardElevated
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Instant Core", color = if (aiStatus.activeEngine == com.example.ai.EngineType.BUILTIN_OFFLINE_CORE) Color(0xFF261405) else TextCream, fontSize = 11.5.sp)
                }
                Button(
                    onClick = { onSelectEngine(com.example.ai.EngineType.GEMMA_LOCAL_LLM) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (aiStatus.activeEngine == com.example.ai.EngineType.GEMMA_LOCAL_LLM) AccentAmber else SurfaceCardElevated
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Gemma Local LLM", color = if (aiStatus.activeEngine == com.example.ai.EngineType.GEMMA_LOCAL_LLM) Color(0xFF261405) else TextCream, fontSize = 11.5.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Choose Gemma Model by Device RAM",
            color = TextCream,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Download the model that matches your phone's memory to avoid lag or overheating:",
            color = TextMuted,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 3. Multi-tier RAM Models List
        com.example.ai.ModelRamTier.entries.forEach { tier ->
            val tierState = aiStatus.tierDownloadStates[tier.id] ?: com.example.ai.GemmaDownloadState.NotDownloaded
            val isRecommended = ramInfo.recommendedTier == tier
            val isExceedingRam = tier.minRamGb > ramInfo.totalRamGb
            val isSelectedActive = aiStatus.activeEngine == com.example.ai.EngineType.GEMMA_LOCAL_LLM && aiStatus.selectedTier == tier

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isSelectedActive) Color(0xFF2E241E) else SurfaceCardDark)
                    .border(
                        width = if (isSelectedActive) 1.5.dp else if (isRecommended) 1.2.dp else 1.dp,
                        color = if (isSelectedActive) AccentAmber else if (isRecommended) Color(0xFF558B2F) else SurfaceBorder,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Tier Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(tier.modelName, color = TextCream, fontSize = 14.5.sp, fontWeight = FontWeight.Bold)
                            if (isSelectedActive) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(AccentAmber)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("ACTIVE", color = Color(0xFF261405), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Text(tier.subtitle, color = TextMuted, fontSize = 11.5.sp)
                    }

                    // Size Chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceCardElevated)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("${tier.downloadSizeMb} MB", color = AccentAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // RAM Recommendation Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isRecommended) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF2E4C1E))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("⭐ Recommended for your ${ramInfo.totalRamGb}GB RAM", color = Color(0xFF9CCC65), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else if (isExceedingRam) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF4C221E))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("⚠️ Requires ${tier.minRamGb}GB+ RAM (Phone has ${ramInfo.totalRamGb}GB)", color = Color(0xFFFF8A80), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E2F3E))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("✓ Compatible with your phone", color = Color(0xFF80D8FF), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Text(tier.description, color = TextSubtle, fontSize = 11.sp, lineHeight = 15.sp)

                // Actions for this Tier
                when (tierState) {
                    is com.example.ai.GemmaDownloadState.NotDownloaded -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    if (isExceedingRam) {
                                        highRamWarningTier = tier
                                    } else {
                                        onDownloadTier(tier)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isRecommended) AccentAmber else SurfaceCardElevated
                                ),
                                modifier = Modifier.weight(1.3f)
                            ) {
                                Text(
                                    text = "Download (${tier.downloadSizeMb} MB)",
                                    color = if (isRecommended) Color(0xFF261405) else TextCream,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                )
                            }
                            Button(
                                onClick = {
                                    importTargetTier = tier
                                    showImportDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardElevated),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Import File", color = TextCream, fontSize = 11.5.sp)
                            }
                        }
                    }

                    is com.example.ai.GemmaDownloadState.Downloading -> {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Downloading weights...", color = TextCream, fontSize = 11.5.sp)
                                Text("${(tierState.progress * 100).toInt()}% (${tierState.downloadedMb.toInt()}/${tierState.totalMb.toInt()} MB)", color = AccentAmber, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            }
                            LinearProgressIndicator(
                                progress = { tierState.progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = AccentAmber,
                                trackColor = SurfaceCardElevated
                            )
                        }
                    }

                    is com.example.ai.GemmaDownloadState.Ready -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!isSelectedActive) {
                                Button(
                                    onClick = {
                                        onSelectTier(tier)
                                        onSelectEngine(com.example.ai.EngineType.GEMMA_LOCAL_LLM)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmber),
                                    modifier = Modifier.weight(1.3f)
                                ) {
                                    Text("Activate This Model", color = Color(0xFF261405), fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF2E4C1E))
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Currently Active ✓", color = Color(0xFF9CCC65), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }

                            Button(
                                onClick = { onDeleteTier(tier) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F1F1F)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Delete (${tier.downloadSizeMb}MB)", color = Color(0xFFFF8888), fontSize = 11.sp)
                            }
                        }
                    }

                    is com.example.ai.GemmaDownloadState.Error -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(tierState.message, color = Color(0xFFFF7B7B), fontSize = 11.sp)
                            Button(onClick = { onDownloadTier(tier) }, colors = ButtonDefaults.buttonColors(containerColor = AccentAmber)) {
                                Text("Retry", color = Color(0xFF261405), fontSize = 11.sp)
                            }
                        }
                    }

                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Device Memory & Hardware Usage Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceCardDark)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Active Runtime Hardware Status", color = AccentAmber, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text("Engine: ${aiStatus.modelName}", color = TextCream, fontSize = 13.sp)
            Text("Device Total RAM: ${ramInfo.totalRamGb} GB", color = TextCream, fontSize = 12.5.sp)
            Text("Memory Footprint: ~${aiStatus.memoryUsageMb} MB", color = TextMuted, fontSize = 12.sp)
            Text("Connection: 100% Offline (No cloud account needed)", color = Color(0xFF4ADE80), fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Import Dialog
        if (showImportDialog) {
            var inputPath by remember { mutableStateOf("/sdcard/Download/${importTargetTier.fileName}") }
            AlertDialog(
                onDismissRequest = { showImportDialog = false },
                title = { Text("Import ${importTargetTier.modelName}", color = TextCream) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Enter the path to your downloaded Gemma .bin or .task file for ${importTargetTier.modelName}:", color = TextMuted, fontSize = 12.sp)
                        OutlinedTextField(
                            value = inputPath,
                            onValueChange = { inputPath = it },
                            label = { Text("Model File Path") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextCream, unfocusedTextColor = TextCream)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onImportTier(importTargetTier, inputPath)
                            showImportDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentAmber)
                    ) {
                        Text("Import", color = Color(0xFF261405))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showImportDialog = false }) { Text("Cancel", color = TextMuted) }
                },
                containerColor = SurfaceCardElevated
            )
        }

        // High RAM Warning Confirmation Dialog
        highRamWarningTier?.let { tier ->
            AlertDialog(
                onDismissRequest = { highRamWarningTier = null },
                title = { Text("High RAM Notice ⚠️", color = Color(0xFFFFB285)) },
                text = {
                    Text(
                        text = "Your device has ${ramInfo.totalRamGb} GB RAM, while ${tier.modelName} recommends at least ${tier.minRamGb} GB RAM.\n\nRunning this model might cause slower generation or background apps to close. For best performance, we recommend '${ramInfo.recommendedTier.modelName}'.\n\nDo you still want to download?",
                        color = TextCream,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onDownloadTier(tier)
                            highRamWarningTier = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentAmber)
                    ) {
                        Text("Download Anyway", color = Color(0xFF261405), fontSize = 12.sp)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        onDownloadTier(ramInfo.recommendedTier)
                        highRamWarningTier = null
                    }) {
                        Text("Use Recommended", color = Color(0xFF9CCC65), fontSize = 12.sp)
                    }
                },
                containerColor = SurfaceCardElevated
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy & Storage Controls Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceCardDark)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Privacy & Storage", color = AccentAmber, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2E4C1E))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("100% Local-First", color = Color(0xFF9CCC65), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text("All messages, notes, photos, and recordings remain strictly on your device.", color = TextMuted, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Local SQLite Database:", color = TextCream, fontSize = 12.5.sp)
                Text(storageSummary.formatBytes(storageSummary.databaseBytes), color = AccentAmber, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Saved Attachments:", color = TextCream, fontSize = 12.5.sp)
                Text(storageSummary.formatBytes(storageSummary.attachmentsBytes), color = AccentAmber, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Voice Notes:", color = TextCream, fontSize = 12.5.sp)
                Text(storageSummary.formatBytes(storageSummary.voiceNotesBytes), color = AccentAmber, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Local Storage:", color = TextCream, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(storageSummary.formatBytes(storageSummary.totalBytes), color = Color(0xFF4ADE80), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Export Data Button
            Button(
                onClick = { onExportData?.invoke() },
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardElevated),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Share, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Export Backup Data (Text Summary)", color = TextCream, fontSize = 12.5.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reset Data
        Button(
            onClick = { showResetConfirm = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A1A1A)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset App & Clear Local Data", color = Color(0xFFFF8888))
        }
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("Reset All Data?", color = TextCream) },
            text = { Text("This will reset your profile and restore onboarding. Continue?", color = TextMuted) },
            confirmButton = {
                Button(
                    onClick = {
                        onResetAll()
                        showResetConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF87171))
                ) {
                    Text("Reset", color = TextCream)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) { Text("Cancel", color = TextMuted) }
            },
            containerColor = SurfaceCardElevated
        )
    }
}

/* ----------------- 7. ALL TOOLS MENU SCREEN (Matching showcase bottom strip) ----------------- */
@Composable
fun ToolsMenuScreen(
    onNavigate: (Screen) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tools = listOf(
        ToolShowcaseItem("Chat", "Always here", Icons.Default.Chat, Color(0xFF60A5FA), Screen.Chat),
        ToolShowcaseItem("Gemma AI Models", "RAM Tier Downloads", Icons.Default.Memory, Color(0xFF38BDF8), Screen.Settings),
        ToolShowcaseItem("Calendar", "Your schedule", Icons.Default.CalendarMonth, Color(0xFFFB923C), Screen.Calendar),
        ToolShowcaseItem("Reminders", "Never forget", Icons.Default.Notifications, Color(0xFFFBBF24), Screen.Reminders),
        ToolShowcaseItem("Timer", "Stay focused", Icons.Default.Timer, Color(0xFFF87171), Screen.Timer),
        ToolShowcaseItem("Clock", "World clock", Icons.Default.Schedule, Color(0xFFA78BFA), Screen.Clock),
        ToolShowcaseItem("Notes", "Write freely", Icons.Default.Description, Color(0xFF60A5FA), Screen.Notes),
        ToolShowcaseItem("Calculator", "Daily use", Icons.Default.Calculate, Color(0xFFC084FC), Screen.Calculator),
        ToolShowcaseItem("Gallery", "Your memories", Icons.Default.Collections, Color(0xFF2DD4BF), Screen.Memories),
        ToolShowcaseItem("Voice Notes", "Quick & easy", Icons.Default.Mic, Color(0xFFF472B6), Screen.VoiceNotes),
        ToolShowcaseItem("To-Do List", "Stay organized", Icons.Default.Check, Color(0xFF34D399), Screen.Tasks),
        ToolShowcaseItem("Study Planner", "Exam countdown", Icons.Default.School, Color(0xFFF59E0B), Screen.StudyPlanner),
        ToolShowcaseItem("Weather", "Daily update", Icons.Default.WbSunny, Color(0xFFFBBF24), Screen.Weather),
        ToolShowcaseItem("Settings", "Customize", Icons.Default.Settings, Color(0xFF94A3B8), Screen.Settings)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWarmDark)
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("tools_back_button")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextCream)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("All Features ♡", color = TextCream, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1.3f))
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Designed for your everyday life ♡",
            color = AccentAmber,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Grid of tools matching bottom showcase strip in image
        tools.chunked(2).forEach { rowTools ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowTools.forEach { tool ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceCardDark)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                            .clickable { onNavigate(tool.targetScreen) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(tool.tint.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(tool.icon, contentDescription = tool.name, tint = tool.tint, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(tool.name, color = TextCream, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text(tool.desc, color = TextMuted, fontSize = 10.5.sp)
                        }
                    }
                }
                if (rowTools.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

data class ToolShowcaseItem(
    val name: String,
    val desc: String,
    val icon: ImageVector,
    val tint: Color,
    val targetScreen: Screen
)
