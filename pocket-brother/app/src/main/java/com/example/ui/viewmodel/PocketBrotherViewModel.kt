package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

sealed class Screen(val route: String, val title: String) {
    data object Splash : Screen("splash", "Pocket Brother")
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
    data object Memories : Screen("memories", "Gallery & Memories")
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
    val activePreset: String = "Focus"
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
    private val prefs: PreferencesManager

    init {
        val db = PocketBrotherDatabase.getDatabase(application)
        prefs = PreferencesManager(application)
        val llmService = com.example.ai.AndroidLocalLLMService(application)
        repository = PocketBrotherRepository(db, prefs, llmService)
    }

    // Navigation Stack
    private val backStack = ArrayDeque<Screen>()
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: Screen, addToBackStack: Boolean = true) {
        if (screen == _currentScreen.value) return
        if (addToBackStack && _currentScreen.value !is Screen.Splash && _currentScreen.value !is Screen.Onboarding) {
            backStack.addLast(_currentScreen.value)
        }
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (backStack.isNotEmpty()) {
            _currentScreen.value = backStack.removeLast()
            return true
        } else if (_currentScreen.value != Screen.Home && _currentScreen.value != Screen.Splash && _currentScreen.value != Screen.Onboarding) {
            _currentScreen.value = Screen.Home
            return true
        }
        return false
    }

    fun completeSplash() {
        val dest = if (repository.isOnboarded.value) Screen.Home else Screen.Onboarding
        _currentScreen.value = dest
    }

    // User Profile
    val sisterName: StateFlow<String> = repository.sisterName
    val relationship: StateFlow<RelationshipType> = repository.relationship
    val currentMode: StateFlow<PersonalityMode> = repository.currentMode
    val isOnboarded: StateFlow<Boolean> = repository.isOnboarded
    val themeMode: StateFlow<String> = prefs.themeMode
    val sisterAvatar: StateFlow<String> = prefs.sisterAvatar
    val preferredGreeting: StateFlow<String> = prefs.preferredGreeting
    val preferredLanguage: StateFlow<String> = prefs.preferredLanguage
    val weatherCity: StateFlow<String> = prefs.weatherCity
    val recentEmojis: StateFlow<List<String>> = prefs.recentEmojis

    fun updateSisterName(name: String) = repository.updateSisterName(name)
    fun updateRelationship(rel: RelationshipType) = repository.updateRelationship(rel)
    fun updateMode(mode: PersonalityMode) = repository.updateMode(mode)
    fun setThemeMode(mode: String) = prefs.setThemeMode(mode)
    fun setSisterAvatar(avatar: String) = prefs.setSisterAvatar(avatar)
    fun setPreferredGreeting(greeting: String) = prefs.setPreferredGreeting(greeting)
    fun setPreferredLanguage(lang: String) = prefs.setPreferredLanguage(lang)
    fun addRecentEmoji(emoji: String) = prefs.addRecentEmoji(emoji)

    fun completeOnboarding(name: String, rel: RelationshipType) {
        repository.completeOnboarding(name, rel)
        _currentScreen.value = Screen.Home
    }

    // Chat
    val chatMessages: StateFlow<List<ChatMessage>> = repository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isBrotherTyping = MutableStateFlow(false)
    val isBrotherTyping: StateFlow<Boolean> = _isBrotherTyping.asStateFlow()

    private var isSendingMessage = false

    fun sendSisterMessage(
        text: String,
        attachmentUri: String? = null,
        attachmentType: String? = null,
        attachmentName: String? = null,
        attachmentSize: Long = 0L
    ) {
        if (text.isBlank() && attachmentUri == null) return
        if (_isBrotherTyping.value || isSendingMessage) return

        isSendingMessage = true
        viewModelScope.launch {
            try {
                _isBrotherTyping.value = true
                delay(700)
                val resp = repository.sendSisterMessage(
                    text = text,
                    attachmentUri = attachmentUri,
                    attachmentType = attachmentType,
                    attachmentName = attachmentName,
                    attachmentSize = attachmentSize
                )
                if (resp.toolAction is com.example.ai.BrotherToolAction.StartTimer) {
                    setTimerPreset("Focus", (resp.toolAction as com.example.ai.BrotherToolAction.StartTimer).minutes * 60)
                    startTimer()
                }
            } catch (_: Exception) {
            } finally {
                _isBrotherTyping.value = false
                isSendingMessage = false
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

    // Memories & Gallery
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

    fun addVoiceNote(title: String, seconds: Int, filePath: String = "") {
        viewModelScope.launch {
            repository.insertVoiceNote(
                VoiceNoteItem(
                    title = title,
                    durationSeconds = seconds,
                    filePath = filePath
                )
            )
        }
    }

    fun updateVoiceNoteTitle(note: VoiceNoteItem, newTitle: String) {
        viewModelScope.launch {
            repository.deleteVoiceNote(note)
            repository.insertVoiceNote(note.copy(title = newTitle))
        }
    }

    fun deleteVoiceNote(note: VoiceNoteItem) {
        viewModelScope.launch {
            if (note.filePath.isNotBlank()) {
                try { File(note.filePath).delete() } catch (_: Exception) {}
            }
            repository.deleteVoiceNote(note)
        }
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
    private val _weather = MutableStateFlow(buildWeatherForCity(prefs.weatherCity.value))
    val weather: StateFlow<WeatherInfo> = _weather.asStateFlow()

    fun updateWeatherCity(city: String) {
        prefs.setWeatherCity(city)
        _weather.value = buildWeatherForCity(city)
    }

    private fun buildWeatherForCity(city: String): WeatherInfo {
        val trimmed = city.trim().ifEmpty { "Bengaluru" }
        val (temp, condition, greeting) = when (trimmed.lowercase()) {
            "bengaluru", "bangalore" -> Triple("24°C", "Partly Cloudy", "Pleasant evening for a walk with brother ♡")
            "mumbai" -> Triple("29°C", "Humid Breeze", "Stay hydrated! Grab a cold drink or juice $trimmed ♡")
            "delhi", "new delhi" -> Triple("28°C", "Clear Sky", "Pleasant sunny afternoon for college!")
            "hyderabad" -> Triple("27°C", "Sunny", "Perfect weather to grab some biryani with brother 🍚")
            "chennai" -> Triple("31°C", "Warm & Sunny", "Stay cool indoors $trimmed, don't walk in hot sun!")
            "kolkata" -> Triple("28°C", "Light Mist", "Cozy evening for hot tea & snacks ☕")
            "london" -> Triple("16°C", "Overcast & Drizzle", "Take an umbrella with you $trimmed! ☔")
            "new york" -> Triple("19°C", "Breezy", "Crisp morning air, bundle up warmly!")
            else -> Triple("25°C", "Pleasant Day", "Hope you're having a lovely day $trimmed! Always here for you ♡")
        }
        return WeatherInfo(
            city = trimmed,
            temperature = temp,
            condition = condition,
            greeting = greeting,
            isOfflineCached = true
        )
    }

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

    fun activateModelTier(tier: com.example.ai.ModelRamTier) {
        repository.selectTier(tier)
        repository.setEngineType(com.example.ai.EngineType.GEMMA_LOCAL_LLM)
        refreshAIStatus()
    }

    fun downloadGemmaModel(tier: com.example.ai.ModelRamTier) {
        viewModelScope.launch {
            repository.selectTier(tier)
            repository.startGemmaDownload(
                tier = tier,
                onProgress = { _, _, _ -> refreshAIStatus() },
                onComplete = { refreshAIStatus() },
                onError = { refreshAIStatus() }
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

    // Privacy & Storage Controls
    fun getStorageSummary(): StorageSummary {
        val app = getApplication<Application>()
        val dbFile = app.getDatabasePath("pocket_brother_db")
        val dbBytes = if (dbFile.exists()) dbFile.length() else 0L

        val attachDir = File(app.filesDir, "attachments")
        val attachBytes = attachDir.walkTopDown().filter { it.isFile }.map { it.length() }.sum()

        val audioDir = File(app.filesDir, "voice_notes")
        val audioBytes = audioDir.walkTopDown().filter { it.isFile }.map { it.length() }.sum()

        return StorageSummary(
            databaseBytes = dbBytes,
            attachmentsBytes = attachBytes,
            voiceNotesBytes = audioBytes,
            totalBytes = dbBytes + attachBytes + audioBytes
        )
    }

    suspend fun exportUserData(): String {
        val msgs = repository.allMessages.first()
        val nts = repository.allNotes.first()
        val rems = repository.allReminders.first()
        val tsks = repository.allTasks.first()

        val sb = StringBuilder()
        sb.append("=== POCKET BROTHER BACKUP ===\n")
        sb.append("Sister: ${sisterName.value}\n")
        sb.append("Relationship: ${relationship.value.label}\n")
        sb.append("Exported: ${java.util.Date()}\n\n")

        sb.append("-- CHAT MESSAGES (${msgs.size}) --\n")
        msgs.forEach { m ->
            val sender = if (m.isFromSister) sisterName.value else "Brother"
            sb.append("[$sender]: ${m.text}\n")
        }

        sb.append("\n-- NOTES (${nts.size}) --\n")
        nts.forEach { n ->
            sb.append("# ${n.title} [${n.category}]\n${n.content}\n\n")
        }

        sb.append("\n-- TASKS (${tsks.size}) --\n")
        tsks.forEach { t ->
            sb.append("[${if (t.isCompleted) "X" else " "}] ${t.title}\n")
        }

        return sb.toString()
    }

    fun resetAllData() {
        repository.resetAllLocalData()
        _currentScreen.value = Screen.Onboarding
    }
}

data class StorageSummary(
    val databaseBytes: Long = 0L,
    val attachmentsBytes: Long = 0L,
    val voiceNotesBytes: Long = 0L,
    val totalBytes: Long = 0L
) {
    fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 -> "%.1f MB".format(bytes.toFloat() / (1024 * 1024))
            bytes >= 1024 -> "%.1f KB".format(bytes.toFloat() / 1024)
            else -> "$bytes B"
        }
    }
}
