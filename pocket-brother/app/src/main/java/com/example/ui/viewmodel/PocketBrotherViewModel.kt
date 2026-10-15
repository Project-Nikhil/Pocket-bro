package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AIModelStatus
import com.example.data.local.PocketBrotherDatabase
import com.example.data.local.PreferencesManager
import com.example.data.model.CalendarEvent
import com.example.data.model.ChatMessage
import com.example.data.model.MemoryItem
import com.example.data.model.NoteItem
import com.example.data.model.PersonalityMode
import com.example.data.model.RelationshipType
import com.example.data.model.ReminderItem
import com.example.data.model.StudyExamItem
import com.example.data.model.TodoTask
import com.example.data.model.VoiceNoteItem
import com.example.data.repository.PocketBrotherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String) {
    data object Onboarding : Screen("onboarding", "Welcome")
    data object Home : Screen("home", "Home")
    data object Chat : Screen("chat", "Brother Chat")
    data object Calendar : Screen("calendar", "Calendar")
    data object Reminders : Screen("reminders", "Reminders")
    data object Timer : Screen("timer", "Timer")
    data object Notes : Screen("notes", "Notes")
    data object Tasks : Screen("tasks", "To-Do List")
    data object StudyPlanner : Screen("study_planner", "Study Planner")
    data object Calculator : Screen("calculator", "Calculator")
    data object Memories : Screen("memories", "Memories")
    data object VoiceNotes : Screen("voice_notes", "Voice Notes")
    data object Clock : Screen("clock", "Clock & Alarms")
    data object Weather : Screen("weather", "Weather")
    data object Settings : Screen("settings", "Settings")
    data object ToolsMenu : Screen("tools_menu", "All Tools")
}

data class TimerState(
    val totalSeconds: Int = 25 * 60,
    val secondsLeft: Int = 25 * 60,
    val isRunning: Boolean = false,
    val activePreset: String = "Focus" // Focus (25m), Short Break (5m), Long Break (15m), Custom
)

data class WeatherInfo(
    val city: String = "Bengaluru",
    val temperature: String = "24°C",
    val condition: String = "Partly Cloudy",
    val greeting: String = "Pleasant evening for a walk with brother ♡",
    val isOfflineCached: Boolean = true
)

class PocketBrotherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PocketBrotherRepository

    init {
        val db = PocketBrotherDatabase.getDatabase(application)
        val prefs = PreferencesManager(application)
        val llmService = com.example.ai.AndroidLocalLLMService(application)
        repository = PocketBrotherRepository(db, prefs, llmService)
    }

    // Navigation
    private val _currentScreen = MutableStateFlow<Screen>(
        if (repository.isOnboarded.value) Screen.Home else Screen.Onboarding
    )
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    // User Profile
    val sisterName: StateFlow<String> = repository.sisterName
    val relationship: StateFlow<RelationshipType> = repository.relationship
    val currentMode: StateFlow<PersonalityMode> = repository.currentMode
    val isOnboarded: StateFlow<Boolean> = repository.isOnboarded

    fun updateSisterName(name: String) = repository.updateSisterName(name)
    fun updateRelationship(rel: RelationshipType) = repository.updateRelationship(rel)
    fun updateMode(mode: PersonalityMode) = repository.updateMode(mode)
    fun completeOnboarding(name: String, rel: RelationshipType) {
        repository.completeOnboarding(name, rel)
        _currentScreen.value = Screen.Home
    }

    // Chat
    val chatMessages: StateFlow<List<ChatMessage>> = repository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isBrotherTyping = MutableStateFlow(false)
    val isBrotherTyping: StateFlow<Boolean> = _isBrotherTyping.asStateFlow()

    fun sendSisterMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _isBrotherTyping.value = true
            delay(800) // Realistic typing delay
            val resp = repository.sendSisterMessage(text)
            _isBrotherTyping.value = false

            // Auto switch timer if triggered by AI
            if (resp.toolAction is com.example.ai.BrotherToolAction.StartTimer) {
                setTimerPreset("Focus", (resp.toolAction as com.example.ai.BrotherToolAction.StartTimer).minutes * 60)
                startTimer()
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch { repository.clearChat() }
    }

    // Calendar
    val calendarEvents: StateFlow<List<CalendarEvent>> = repository.allEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCalendarEvent(event: CalendarEvent) {
        viewModelScope.launch { repository.insertEvent(event) }
    }

    fun deleteCalendarEvent(event: CalendarEvent) {
        viewModelScope.launch { repository.deleteEvent(event) }
    }

    // Reminders
    val reminders: StateFlow<List<ReminderItem>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleReminder(reminder: ReminderItem) {
        viewModelScope.launch {
            repository.updateReminder(reminder.copy(isEnabled = !reminder.isEnabled))
        }
    }

    fun addReminder(reminder: ReminderItem) {
        viewModelScope.launch { repository.insertReminder(reminder) }
    }

    fun deleteReminder(reminder: ReminderItem) {
        viewModelScope.launch { repository.deleteReminder(reminder) }
    }

    // Notes
    val notes: StateFlow<List<NoteItem>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addNote(title: String, content: String, category: String, isDiary: Boolean) {
        viewModelScope.launch {
            repository.insertNote(
                NoteItem(
                    title = title.ifEmpty { "Untitled" },
                    content = content,
                    category = category,
                    isDiary = isDiary
                )
            )
        }
    }

    fun deleteNote(note: NoteItem) {
        viewModelScope.launch { repository.deleteNote(note) }
    }

    // Tasks
    val tasks: StateFlow<List<TodoTask>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleTask(task: TodoTask) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun addTask(title: String, priority: String = "Medium") {
        viewModelScope.launch {
            repository.insertTask(
                TodoTask(title = title, priority = priority)
            )
        }
    }

    fun deleteTask(task: TodoTask) {
        viewModelScope.launch { repository.deleteTask(task) }
    }

    // Exams
    val exams: StateFlow<List<StudyExamItem>> = repository.allExams
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addExam(exam: StudyExamItem) {
        viewModelScope.launch { repository.insertExam(exam) }
    }

    fun deleteExam(exam: StudyExamItem) {
        viewModelScope.launch { repository.deleteExam(exam) }
    }

    // Memories
    val memories: StateFlow<List<MemoryItem>> = repository.allMemories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addMemory(title: String, desc: String, quote: String, isRealBrother: Boolean) {
        viewModelScope.launch {
            repository.insertMemory(
                MemoryItem(
                    title = title,
                    description = desc,
                    quote = quote,
                    isFromRealBrother = isRealBrother
                )
            )
        }
    }

    fun deleteMemory(memory: MemoryItem) {
        viewModelScope.launch {
            repository.deleteMemory(memory)
        }
    }

    // Voice Notes
    val voiceNotes: StateFlow<List<VoiceNoteItem>> = repository.allVoiceNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addVoiceNote(title: String, seconds: Int) {
        viewModelScope.launch {
            repository.insertVoiceNote(
                VoiceNoteItem(
                    title = title,
                    durationSeconds = seconds
                )
            )
        }
    }

    fun deleteVoiceNote(note: VoiceNoteItem) {
        viewModelScope.launch { repository.deleteVoiceNote(note) }
    }

    // Timer Logic
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null

    fun setTimerPreset(preset: String, seconds: Int) {
        pauseTimer()
        _timerState.value = TimerState(
            totalSeconds = seconds,
            secondsLeft = seconds,
            isRunning = false,
            activePreset = preset
        )
    }

    fun startTimer() {
        if (_timerState.value.isRunning) return
        _timerState.value = _timerState.value.copy(isRunning = true)
        timerJob = viewModelScope.launch {
            while (_timerState.value.secondsLeft > 0 && _timerState.value.isRunning) {
                delay(1000)
                _timerState.value = _timerState.value.copy(
                    secondsLeft = _timerState.value.secondsLeft - 1
                )
            }
            if (_timerState.value.secondsLeft <= 0) {
                _timerState.value = _timerState.value.copy(isRunning = false)
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _timerState.value = _timerState.value.copy(isRunning = false)
    }

    fun resetTimer() {
        pauseTimer()
        _timerState.value = _timerState.value.copy(
            secondsLeft = _timerState.value.totalSeconds
        )
    }

    // Weather
    private val _weather = MutableStateFlow(WeatherInfo())
    val weather: StateFlow<WeatherInfo> = _weather.asStateFlow()

    // AI Status & Gemma Model Management
    private val _aiStatus = MutableStateFlow(repository.getAIStatus())
    val aiStatus: StateFlow<AIModelStatus> = _aiStatus.asStateFlow()

    fun refreshAIStatus() {
        _aiStatus.value = repository.getAIStatus()
    }

    fun setEngineType(engine: com.example.ai.EngineType) {
        repository.setEngineType(engine)
        refreshAIStatus()
    }

    fun selectModelTier(tier: com.example.ai.ModelRamTier) {
        repository.selectTier(tier)
        refreshAIStatus()
    }

    fun downloadGemmaModel(tier: com.example.ai.ModelRamTier) {
        viewModelScope.launch {
            repository.selectTier(tier)
            repository.startGemmaDownload(
                tier = tier,
                onProgress = { _, _, _ ->
                    refreshAIStatus()
                },
                onComplete = {
                    refreshAIStatus()
                },
                onError = {
                    refreshAIStatus()
                }
            )
        }
    }

    fun deleteGemmaModel(tier: com.example.ai.ModelRamTier) {
        repository.deleteGemmaModel(tier)
        refreshAIStatus()
    }

    fun importGemmaModel(tier: com.example.ai.ModelRamTier, path: String) {
        viewModelScope.launch {
            repository.importGemmaModel(tier, path)
            refreshAIStatus()
        }
    }

    fun resetAllData() {
        repository.resetAllLocalData()
        _currentScreen.value = Screen.Onboarding
    }
}
