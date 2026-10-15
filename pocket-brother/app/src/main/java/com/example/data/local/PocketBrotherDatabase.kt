package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CalendarEvent
import com.example.data.model.ChatMessage
import com.example.data.model.MemoryItem
import com.example.data.model.NoteItem
import com.example.data.model.PersonalityMode
import com.example.data.model.ReminderItem
import com.example.data.model.StudyExamItem
import com.example.data.model.TodoTask
import com.example.data.model.VoiceNoteItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ChatMessage::class,
        CalendarEvent::class,
        ReminderItem::class,
        NoteItem::class,
        TodoTask::class,
        StudyExamItem::class,
        MemoryItem::class,
        VoiceNoteItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PocketBrotherDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun calendarDao(): CalendarDao
    abstract fun reminderDao(): ReminderDao
    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao
    abstract fun studyExamDao(): StudyExamDao
    abstract fun memoryDao(): MemoryDao
    abstract fun voiceNoteDao(): VoiceNoteDao

    companion object {
        @Volatile
        private var INSTANCE: PocketBrotherDatabase? = null

        fun getDatabase(context: Context): PocketBrotherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PocketBrotherDatabase::class.java,
                    "pocket_brother_db"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }

            private suspend fun populateInitialData(database: PocketBrotherDatabase) {
                val now = System.currentTimeMillis()

                // Chat conversation starts empty so user initiates chat directly

                // Initial Calendar Events matching showcase
                val calendarDao = database.calendarDao()
                calendarDao.insertEvent(
                    CalendarEvent(
                        title = "College Class",
                        dateMillis = now,
                        startTime = "10:00 AM",
                        endTime = "11:00 AM",
                        location = "Campus Hall B",
                        category = "College"
                    )
                )
                calendarDao.insertEvent(
                    CalendarEvent(
                        title = "Project Work",
                        dateMillis = now,
                        startTime = "2:00 PM",
                        endTime = "4:00 PM",
                        location = "Library Lab",
                        category = "Exams"
                    )
                )
                calendarDao.insertEvent(
                    CalendarEvent(
                        title = "Talk to Brother 💙",
                        dateMillis = now,
                        startTime = "9:00 PM",
                        endTime = "10:00 PM",
                        location = "Home",
                        category = "Personal"
                    )
                )

                // Initial Reminders matching showcase
                val reminderDao = database.reminderDao()
                reminderDao.insertReminder(
                    ReminderItem(
                        title = "Drink water",
                        timeText = "Today, 10:00 AM",
                        isEnabled = true,
                        category = "Health"
                    )
                )
                reminderDao.insertReminder(
                    ReminderItem(
                        title = "Study for exam",
                        timeText = "Today, 4:00 PM",
                        isEnabled = false,
                        category = "Study"
                    )
                )
                reminderDao.insertReminder(
                    ReminderItem(
                        title = "Call mom",
                        timeText = "Today, 8:00 PM",
                        isEnabled = true,
                        category = "Family"
                    )
                )
                reminderDao.insertReminder(
                    ReminderItem(
                        title = "Chat with brother 💙",
                        timeText = "Today, 9:00 PM",
                        isEnabled = true,
                        category = "Personal"
                    )
                )

                // Initial Notes matching showcase
                val noteDao = database.noteDao()
                noteDao.insertNote(
                    NoteItem(
                        title = "My Thoughts",
                        content = "Sometimes college feels overwhelming, but talking to bhaiya always reminds me to keep moving forward. I'm capable of anything I set my mind to.",
                        category = "Thoughts",
                        isPinned = true,
                        isDiary = true,
                        mood = "happy"
                    )
                )
                noteDao.insertNote(
                    NoteItem(
                        title = "College Notes",
                        content = "Review Chapter 4 on Distributed Systems. Key principles: fault tolerance, consensus algorithms, partition tolerance.",
                        category = "College",
                        isPinned = false
                    )
                )
                noteDao.insertNote(
                    NoteItem(
                        title = "Things to Do",
                        content = "1. Finish lab submission by Thursday\n2. Order new notebook\n3. Call brother on Sunday evening",
                        category = "Tasks",
                        isPinned = false
                    )
                )

                // Initial Tasks
                val taskDao = database.taskDao()
                taskDao.insertTask(
                    TodoTask(
                        title = "Complete AI lab assignment",
                        isCompleted = false,
                        dueDate = "Tomorrow",
                        priority = "High"
                    )
                )
                taskDao.insertTask(
                    TodoTask(
                        title = "Revise Operating Systems unit 3",
                        isCompleted = true,
                        dueDate = "Today",
                        priority = "Medium"
                    )
                )
                taskDao.insertTask(
                    TodoTask(
                        title = "Drink 8 glasses of water",
                        isCompleted = false,
                        dueDate = "Daily",
                        priority = "Low"
                    )
                )

                // Initial Exams
                val examDao = database.studyExamDao()
                examDao.insertExam(
                    StudyExamItem(
                        subject = "Computer Networks",
                        examName = "Mid-Term Examination",
                        examDateMillis = now + (7 * 24 * 3600 * 1000L),
                        targetGrade = "A+",
                        totalTopics = 8,
                        completedTopics = 5
                    )
                )
                examDao.insertExam(
                    StudyExamItem(
                        subject = "Database Management",
                        examName = "Semester Final",
                        examDateMillis = now + (18 * 24 * 3600 * 1000L),
                        targetGrade = "A",
                        totalTopics = 12,
                        completedTopics = 4
                    )
                )

                // Initial Memories
                val memoryDao = database.memoryDao()
                memoryDao.insertMemory(
                    MemoryItem(
                        title = "When you miss me message",
                        description = "Whenever you feel alone or stressed, remember: no matter the distance, your brother always has your back. You are capable of amazing things.",
                        quote = "“You are stronger than you think and i'm always proud of you ♡”",
                        isFromRealBrother = true,
                        tag = "Brother Note"
                    )
                )
                memoryDao.insertMemory(
                    MemoryItem(
                        title = "Late night ice cream run",
                        description = "Sneaking out at 11 PM to grab chocolate cones before exams. We laughed until our stomachs hurt.",
                        quote = "“The best memories are the unplanned ones.”",
                        isFromRealBrother = true,
                        tag = "Memory"
                    )
                )
            }
        }
    }
}
