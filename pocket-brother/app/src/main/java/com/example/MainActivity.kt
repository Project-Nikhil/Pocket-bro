package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import com.example.ui.screens.SplashScreen
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
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: PocketBrotherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by viewModel.themeMode.collectAsState()
            val isDark = when (themeMode) {
                "LIGHT" -> false
                "DARK" -> true
                else -> isSystemInDarkTheme()
            }
            PocketBrotherTheme(darkTheme = isDark) {
                PocketBrotherApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun PocketBrotherApp(viewModel: PocketBrotherViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val currentScreen by viewModel.currentScreen.collectAsState()
    val sisterName by viewModel.sisterName.collectAsState()
    val relationship by viewModel.relationship.collectAsState()
    val currentMode by viewModel.currentMode.collectAsState()
    val aiStatus by viewModel.aiStatus.collectAsState()

    val themeMode by viewModel.themeMode.collectAsState()
    val sisterAvatar by viewModel.sisterAvatar.collectAsState()
    val preferredGreeting by viewModel.preferredGreeting.collectAsState()
    val preferredLanguage by viewModel.preferredLanguage.collectAsState()
    val recentEmojis by viewModel.recentEmojis.collectAsState()

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

    // Handle back button on sub-screens to return to the previous screen rather than hardcoding Home
    if (currentScreen != Screen.Home && currentScreen != Screen.Onboarding && currentScreen != Screen.Splash) {
        BackHandler {
            viewModel.navigateBack()
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
                is Screen.Splash -> {
                    SplashScreen(
                        onLoaded = { viewModel.completeSplash() }
                    )
                }

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
                        recentEmojis = recentEmojis,
                        aiStatus = aiStatus,
                        onSendMessage = { text -> viewModel.sendSisterMessage(text) },
                        onSendAttachmentMessage = { text, uri, type, name, size ->
                            viewModel.sendSisterMessage(
                                text = text,
                                attachmentUri = uri,
                                attachmentType = type,
                                attachmentName = name,
                                attachmentSize = size
                            )
                        },
                        onAddRecentEmoji = { emoji -> viewModel.addRecentEmoji(emoji) },
                        onModeChange = { mode -> viewModel.updateMode(mode) },
                        onRoleChange = { rel -> viewModel.updateRelationship(rel) },
                        onClearChat = { viewModel.clearChat() },
                        onBack = { viewModel.navigateBack() },
                        onNavigateToSettings = { viewModel.navigateTo(Screen.Settings) },
                        onSelectEngine = { engine -> viewModel.setEngineType(engine) },
                        onSelectTier = { tier -> viewModel.selectModelTier(tier) },
                        onActivateTier = { tier -> viewModel.activateModelTier(tier) },
                        onDownloadTier = { tier -> viewModel.downloadGemmaModel(tier) },
                        onImportTier = { tier, path -> viewModel.importGemmaModel(tier, path) }
                    )
                }

                is Screen.Calendar -> {
                    CalendarScreen(
                        events = calendarEvents,
                        onAddEvent = { event -> viewModel.addCalendarEvent(event) },
                        onDeleteEvent = { event -> viewModel.deleteCalendarEvent(event) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.Reminders -> {
                    RemindersScreen(
                        reminders = reminders,
                        onToggle = { reminder -> viewModel.toggleReminder(reminder) },
                        onAdd = { reminder -> viewModel.addReminder(reminder) },
                        onDelete = { reminder -> viewModel.deleteReminder(reminder) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.Timer -> {
                    TimerScreen(
                        timerState = timerState,
                        onStart = { viewModel.startTimer() },
                        onPause = { viewModel.pauseTimer() },
                        onReset = { viewModel.resetTimer() },
                        onSelectPreset = { preset, secs -> viewModel.setTimerPreset(preset, secs) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.Notes -> {
                    NotesScreen(
                        notes = notes,
                        onAddNote = { title, content, cat, isDiary ->
                            viewModel.addNote(title, content, cat, isDiary)
                        },
                        onDeleteNote = { note -> viewModel.deleteNote(note) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.Tasks -> {
                    TasksScreen(
                        tasks = tasks,
                        onToggle = { task -> viewModel.toggleTask(task) },
                        onAdd = { title, pri -> viewModel.addTask(title, pri) },
                        onDelete = { task -> viewModel.deleteTask(task) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.StudyPlanner -> {
                    StudyPlannerScreen(
                        exams = exams,
                        onAddExam = { exam -> viewModel.addExam(exam) },
                        onDeleteExam = { exam -> viewModel.deleteExam(exam) },
                        onNavigateToTimer = { viewModel.navigateTo(Screen.Timer) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.Calculator -> {
                    com.example.ui.screens.CalculatorScreen(
                        onBack = { viewModel.navigateBack() }
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
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.VoiceNotes -> {
                    VoiceNotesScreen(
                        voiceNotes = voiceNotes,
                        onAddVoiceNote = { title, secs -> viewModel.addVoiceNote(title, secs) },
                        onDeleteVoiceNote = { note -> viewModel.deleteVoiceNote(note) },
                        onRenameVoiceNote = { note, newTitle -> viewModel.updateVoiceNoteTitle(note, newTitle) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.Clock -> {
                    ClockScreen(
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.Weather -> {
                    WeatherScreen(
                        weather = weather,
                        onCityChanged = { city -> viewModel.updateWeatherCity(city) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.Settings -> {
                    SettingsScreen(
                        sisterName = sisterName,
                        relationship = relationship,
                        currentMode = currentMode,
                        aiStatus = aiStatus,
                        sisterAvatar = sisterAvatar,
                        preferredGreeting = preferredGreeting,
                        preferredLanguage = preferredLanguage,
                        themeMode = themeMode,
                        storageSummary = viewModel.getStorageSummary(),
                        onUpdateName = { name -> viewModel.updateSisterName(name) },
                        onUpdateRelationship = { rel -> viewModel.updateRelationship(rel) },
                        onUpdateAvatar = { av -> viewModel.setSisterAvatar(av) },
                        onUpdateGreeting = { gr -> viewModel.setPreferredGreeting(gr) },
                        onUpdateLanguage = { lg -> viewModel.setPreferredLanguage(lg) },
                        onUpdateThemeMode = { th -> viewModel.setThemeMode(th) },
                        onExportData = {
                            coroutineScope.launch {
                                try {
                                    val backup = viewModel.exportUserData()
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, backup)
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, "Pocket Brother Backup")
                                    context.startActivity(shareIntent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Export ready in local storage", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        onSelectTier = { tier -> viewModel.selectModelTier(tier) },
                        onDownloadTier = { tier -> viewModel.downloadGemmaModel(tier) },
                        onDeleteTier = { tier -> viewModel.deleteGemmaModel(tier) },
                        onSelectEngine = { engine -> viewModel.setEngineType(engine) },
                        onImportTier = { tier, path -> viewModel.importGemmaModel(tier, path) },
                        onResetAll = { viewModel.resetAllData() },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.ToolsMenu -> {
                    ToolsMenuScreen(
                        onNavigate = { screen -> viewModel.navigateTo(screen) },
                        onBack = { viewModel.navigateBack() }
                    )
                }
            }
        }
    }
}
