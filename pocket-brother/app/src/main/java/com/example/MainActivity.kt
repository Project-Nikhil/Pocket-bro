package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.PocketBottomBar
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.ClockScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MemoriesScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StudyPlannerScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.TimerScreen
import com.example.ui.screens.ToolsMenuScreen
import com.example.ui.screens.VoiceNotesScreen
import com.example.ui.screens.WeatherScreen
import com.example.ui.theme.BackgroundWarmDark
import com.example.ui.theme.PocketBrotherTheme
import com.example.ui.viewmodel.PocketBrotherViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    private val viewModel: PocketBrotherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PocketBrotherTheme {
                PocketBrotherApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun PocketBrotherApp(viewModel: PocketBrotherViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val sisterName by viewModel.sisterName.collectAsState()
    val relationship by viewModel.relationship.collectAsState()
    val currentMode by viewModel.currentMode.collectAsState()
    val aiStatus by viewModel.aiStatus.collectAsState()

    val chatMessages by viewModel.chatMessages.collectAsState()
    val isTyping by viewModel.isBrotherTyping.collectAsState()
    val calendarEvents by viewModel.calendarEvents.collectAsState()
    val reminders by viewModel.reminders.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val exams by viewModel.exams.collectAsState()
    val memories by viewModel.memories.collectAsState()
    val voiceNotes by viewModel.voiceNotes.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val weather by viewModel.weather.collectAsState()

    val showBottomBar = currentScreen in listOf(
        Screen.Home,
        Screen.ToolsMenu,
        Screen.Settings
    )

    // Handle back button on sub-screens
    if (currentScreen != Screen.Home && currentScreen != Screen.Onboarding) {
        BackHandler {
            viewModel.navigateTo(Screen.Home)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundWarmDark,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                PocketBottomBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp)
        ) {
            when (currentScreen) {
                is Screen.Onboarding -> {
                    OnboardingScreen(
                        initialName = sisterName,
                        initialRelationship = relationship,
                        onComplete = { name, rel ->
                            viewModel.completeOnboarding(name, rel)
                        }
                    )
                }

                is Screen.Home -> {
                    HomeScreen(
                        sisterName = sisterName,
                        relationship = relationship,
                        onNavigate = { screen -> viewModel.navigateTo(screen) }
                    )
                }

                is Screen.Chat -> {
                    ChatScreen(
                        messages = chatMessages,
                        currentMode = currentMode,
                        isTyping = isTyping,
                        relationship = relationship,
                        sisterName = sisterName,
                        onSendMessage = { text -> viewModel.sendSisterMessage(text) },
                        onModeChange = { mode -> viewModel.updateMode(mode) },
                        onRoleChange = { rel -> viewModel.updateRelationship(rel) },
                        onClearChat = { viewModel.clearChat() },
                        onBack = { viewModel.navigateTo(Screen.Home) },
                        onNavigateToSettings = { viewModel.navigateTo(Screen.Settings) }
                    )
                }

                is Screen.Calendar -> {
                    CalendarScreen(
                        events = calendarEvents,
                        onAddEvent = { event -> viewModel.addCalendarEvent(event) },
                        onDeleteEvent = { event -> viewModel.deleteCalendarEvent(event) },
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.Reminders -> {
                    RemindersScreen(
                        reminders = reminders,
                        onToggle = { reminder -> viewModel.toggleReminder(reminder) },
                        onAdd = { reminder -> viewModel.addReminder(reminder) },
                        onDelete = { reminder -> viewModel.deleteReminder(reminder) },
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.Timer -> {
                    TimerScreen(
                        timerState = timerState,
                        onStart = { viewModel.startTimer() },
                        onPause = { viewModel.pauseTimer() },
                        onReset = { viewModel.resetTimer() },
                        onSelectPreset = { preset, secs -> viewModel.setTimerPreset(preset, secs) },
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.Notes -> {
                    NotesScreen(
                        notes = notes,
                        onAddNote = { title, content, cat, isDiary ->
                            viewModel.addNote(title, content, cat, isDiary)
                        },
                        onDeleteNote = { note -> viewModel.deleteNote(note) },
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.Tasks -> {
                    TasksScreen(
                        tasks = tasks,
                        onToggle = { task -> viewModel.toggleTask(task) },
                        onAdd = { title, pri -> viewModel.addTask(title, pri) },
                        onDelete = { task -> viewModel.deleteTask(task) },
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.StudyPlanner -> {
                    StudyPlannerScreen(
                        exams = exams,
                        onAddExam = { exam -> viewModel.addExam(exam) },
                        onDeleteExam = { exam -> viewModel.deleteExam(exam) },
                        onNavigateToTimer = { viewModel.navigateTo(Screen.Timer) },
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.Calculator -> {
                    com.example.ui.screens.CalculatorScreen(
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.Memories -> {
                    MemoriesScreen(
                        memories = memories,
                        sisterName = sisterName,
                        relationship = relationship,
                        onAddMemory = { title, desc, quote, isReal ->
                            viewModel.addMemory(title, desc, quote, isReal)
                        },
                        onDeleteMemory = { memory -> viewModel.deleteMemory(memory) },
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.VoiceNotes -> {
                    VoiceNotesScreen(
                        voiceNotes = voiceNotes,
                        onAddVoiceNote = { title, secs -> viewModel.addVoiceNote(title, secs) },
                        onDeleteVoiceNote = { note -> viewModel.deleteVoiceNote(note) },
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.Clock -> {
                    ClockScreen(
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.Weather -> {
                    WeatherScreen(
                        weather = weather,
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.Settings -> {
                    SettingsScreen(
                        sisterName = sisterName,
                        relationship = relationship,
                        currentMode = currentMode,
                        aiStatus = aiStatus,
                        onUpdateName = { name -> viewModel.updateSisterName(name) },
                        onUpdateRelationship = { rel -> viewModel.updateRelationship(rel) },
                        onSelectTier = { tier -> viewModel.selectModelTier(tier) },
                        onDownloadTier = { tier -> viewModel.downloadGemmaModel(tier) },
                        onDeleteTier = { tier -> viewModel.deleteGemmaModel(tier) },
                        onSelectEngine = { engine -> viewModel.setEngineType(engine) },
                        onImportTier = { tier, path -> viewModel.importGemmaModel(tier, path) },
                        onResetAll = { viewModel.resetAllData() },
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.ToolsMenu -> {
                    ToolsMenuScreen(
                        onNavigate = { screen -> viewModel.navigateTo(screen) },
                        onBack = { viewModel.navigateTo(Screen.Home) }
                    )
                }
            }
        }
    }
}
