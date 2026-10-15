package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RelationshipType(val label: String, val sisterDescription: String) {
    ELDER_BROTHER(
        label = "My Elder Brother",
        sisterDescription = "You're my younger sister — I'll call you Chelli, Behen, Bauni, Bhondu & Bro"
    ),
    YOUNGER_BROTHER(
        label = "My Younger Brother",
        sisterDescription = "You're my elder sister — I'll call you Akka, Didi, Behen, Bauni, Bhondu & Bro"
    )
}

enum class PersonalityMode(val title: String, val subtitle: String, val emoji: String) {
    COMFORT("Comfort", "always here for you ♡", "🥺"),
    IRRITATIVE("Irritative", "what do you want now 😂", "😈"),
    FUNNY("Funny", "professional troublemaker 🤣", "😄")
}

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val isFromSister: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val mode: String = PersonalityMode.COMFORT.name,
    val isRead: Boolean = true,
    val attachmentUri: String? = null,
    val attachmentType: String? = null, // "IMAGE", "PDF", "DOC", "TEXT", "AUDIO"
    val attachmentName: String? = null,
    val attachmentSize: Long = 0L
)

@Entity(tableName = "calendar_events")
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val dateMillis: Long,
    val startTime: String,
    val endTime: String,
    val location: String = "",
    val notes: String = "",
    val category: String = "College", // College, Exams, Personal, Family, Study, Other
    val reminderEnabled: Boolean = true
)

@Entity(tableName = "reminders")
data class ReminderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val timeText: String,
    val targetTimestamp: Long = 0L,
    val isEnabled: Boolean = true,
    val priority: String = "Normal",
    val category: String = "Daily"
)

@Entity(tableName = "notes")
data class NoteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val updatedAt: Long = System.currentTimeMillis(),
    val category: String = "Thoughts",
    val isPinned: Boolean = false,
    val isDiary: Boolean = false,
    val mood: String = "neutral"
)

@Entity(tableName = "tasks")
data class TodoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val dueDate: String = "",
    val priority: String = "Medium",
    val category: String = "General"
)

@Entity(tableName = "study_exams")
data class StudyExamItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subject: String,
    val examName: String,
    val examDateMillis: Long,
    val targetGrade: String = "A+",
    val totalTopics: Int = 10,
    val completedTopics: Int = 3
)

@Entity(tableName = "memories")
data class MemoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val quote: String = "",
    val isFromRealBrother: Boolean = true,
    val timestamp: Long = System.currentTimeMillis(),
    val tag: String = "Special"
)

@Entity(tableName = "voice_notes")
data class VoiceNoteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val durationSeconds: Int,
    val recordedAt: Long = System.currentTimeMillis(),
    val filePath: String = ""
)
