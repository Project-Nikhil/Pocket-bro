package com.example.data.repository

import com.example.ai.AIService
import com.example.ai.BrotherResponse
import com.example.ai.BrotherToolAction
import com.example.ai.OfflineBrotherEngine
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

class PocketBrotherRepository(
    private val database: PocketBrotherDatabase,
    private val preferencesManager: PreferencesManager,
    private val aiService: AIService = OfflineBrotherEngine()
) {
    // Preferences
    val sisterName: StateFlow<String> = preferencesManager.sisterName
    val relationship: StateFlow<RelationshipType> = preferencesManager.relationship
    val currentMode: StateFlow<PersonalityMode> = preferencesManager.currentMode
    val isOnboarded: StateFlow<Boolean> = preferencesManager.isOnboarded

    fun updateSisterName(name: String) = preferencesManager.setSisterName(name)
    fun updateRelationship(rel: RelationshipType) = preferencesManager.setRelationship(rel)
    fun updateMode(mode: PersonalityMode) = preferencesManager.setCurrentMode(mode)
    fun completeOnboarding(name: String, rel: RelationshipType) {
        preferencesManager.setSisterName(name)
        preferencesManager.setRelationship(rel)
        preferencesManager.setOnboarded(true)
    }

    // Chat
    val allMessages: Flow<List<ChatMessage>> = database.chatDao().getAllMessages()

    suspend fun sendSisterMessage(text: String): BrotherResponse {
        val now = System.currentTimeMillis()
        val currentRel = relationship.value
        val mode = currentMode.value
        val name = sisterName.value

        // Insert sister message
        database.chatDao().insertMessage(
            ChatMessage(
                text = text,
                isFromSister = true,
                timestamp = now,
                mode = mode.name
            )
        )

        // Generate Brother response
        val history = database.chatDao().getAllMessages().first().takeLast(6).map { it.text }
        val response = aiService.generateResponse(
            sisterMessage = text,
            relationship = currentRel,
            currentMode = mode,
            sisterName = name,
            recentHistory = history
        )

        // Handle tool action if present
        response.toolAction?.let { executeToolAction(it) }

        // Insert Brother replies with small timestamps staggering
        var replyTime = now + 1000
        for (reply in response.replyMessages) {
            database.chatDao().insertMessage(
                ChatMessage(
                    text = reply,
                    isFromSister = false,
                    timestamp = replyTime,
                    mode = response.effectiveMode.name
                )
            )
            replyTime += 1200
        }

        return response
    }

    private suspend fun executeToolAction(toolAction: BrotherToolAction) {
        when (toolAction) {
            is BrotherToolAction.CreateReminder -> {
                database.reminderDao().insertReminder(
                    ReminderItem(
                        title = toolAction.title,
                        timeText = toolAction.timeText,
                        isEnabled = true,
                        category = "Daily"
                    )
                )
            }
            is BrotherToolAction.SaveNote -> {
                database.noteDao().insertNote(
                    NoteItem(
                        title = toolAction.title,
                        content = toolAction.content,
                        category = "Diary",
                        isPinned = true,
                        isDiary = true
                    )
                )
            }
            is BrotherToolAction.CreateCalendarEvent -> {
                database.calendarDao().insertEvent(
                    CalendarEvent(
                        title = toolAction.title,
                        dateMillis = System.currentTimeMillis(),
                        startTime = toolAction.time,
                        endTime = "Later",
                        category = "Personal"
                    )
                )
            }
            is BrotherToolAction.CreateTask -> {
                database.taskDao().insertTask(
                    TodoTask(
                        title = toolAction.title,
                        isCompleted = false,
                        dueDate = "Today"
                    )
                )
            }
            is BrotherToolAction.StartTimer -> {
                // Timer is triggered directly or recorded
            }
            is BrotherToolAction.QuerySchedule -> {
                // Handled in message
            }
        }
    }

    suspend fun clearChat() = database.chatDao().clearChat()

    // Calendar
    val allEvents: Flow<List<CalendarEvent>> = database.calendarDao().getAllEvents()
    suspend fun insertEvent(event: CalendarEvent) = database.calendarDao().insertEvent(event)
    suspend fun deleteEvent(event: CalendarEvent) = database.calendarDao().deleteEvent(event)

    // Reminders
    val allReminders: Flow<List<ReminderItem>> = database.reminderDao().getAllReminders()
    suspend fun insertReminder(reminder: ReminderItem) = database.reminderDao().insertReminder(reminder)
    suspend fun updateReminder(reminder: ReminderItem) = database.reminderDao().updateReminder(reminder)
    suspend fun deleteReminder(reminder: ReminderItem) = database.reminderDao().deleteReminder(reminder)

    // Notes
    val allNotes: Flow<List<NoteItem>> = database.noteDao().getAllNotes()
    suspend fun insertNote(note: NoteItem) = database.noteDao().insertNote(note)
    suspend fun updateNote(note: NoteItem) = database.noteDao().updateNote(note)
    suspend fun deleteNote(note: NoteItem) = database.noteDao().deleteNote(note)

    // Tasks
    val allTasks: Flow<List<TodoTask>> = database.taskDao().getAllTasks()
    suspend fun insertTask(task: TodoTask) = database.taskDao().insertTask(task)
    suspend fun updateTask(task: TodoTask) = database.taskDao().updateTask(task)
    suspend fun deleteTask(task: TodoTask) = database.taskDao().deleteTask(task)

    // Exams
    val allExams: Flow<List<StudyExamItem>> = database.studyExamDao().getAllExams()
    suspend fun insertExam(exam: StudyExamItem) = database.studyExamDao().insertExam(exam)
    suspend fun updateExam(exam: StudyExamItem) = database.studyExamDao().updateExam(exam)
    suspend fun deleteExam(exam: StudyExamItem) = database.studyExamDao().deleteExam(exam)

    // Memories
    val allMemories: Flow<List<MemoryItem>> = database.memoryDao().getAllMemories()
    suspend fun insertMemory(memory: MemoryItem) = database.memoryDao().insertMemory(memory)
    suspend fun deleteMemory(memory: MemoryItem) = database.memoryDao().deleteMemory(memory)

    // Voice Notes
    val allVoiceNotes: Flow<List<VoiceNoteItem>> = database.voiceNoteDao().getAllVoiceNotes()
    suspend fun insertVoiceNote(note: VoiceNoteItem) = database.voiceNoteDao().insertVoiceNote(note)
    suspend fun deleteVoiceNote(note: VoiceNoteItem) = database.voiceNoteDao().deleteVoiceNote(note)

    fun getAIStatus() = aiService.getStatus()

    fun selectTier(tier: com.example.ai.ModelRamTier) {
        aiService.selectTier(tier)
    }

    fun setEngineType(engine: com.example.ai.EngineType) {
        aiService.setEngineType(engine)
    }

    suspend fun startGemmaDownload(
        tier: com.example.ai.ModelRamTier,
        onProgress: (Float, Float, Float) -> Unit,
        onComplete: () -> Unit,
        onError: (String) -> Unit
    ) {
        aiService.startGemmaDownload(tier, onProgress, onComplete, onError)
    }

    suspend fun importGemmaModel(tier: com.example.ai.ModelRamTier, path: String) =
        aiService.importGemmaModel(tier, path)

    fun deleteGemmaModel(tier: com.example.ai.ModelRamTier) =
        aiService.deleteGemmaModel(tier)

    fun resetAllLocalData() {
        preferencesManager.clearAllData()
    }
}
