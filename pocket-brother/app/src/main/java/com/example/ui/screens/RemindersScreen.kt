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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.data.model.ReminderItem
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.BackgroundWarmDark
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextCream
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle

@Composable
fun RemindersScreen(
    reminders: List<ReminderItem>,
    onToggle: (ReminderItem) -> Unit,
    onAdd: (ReminderItem) -> Unit,
    onDelete: (ReminderItem) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

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
            IconButton(onClick = onBack, modifier = Modifier.testTag("reminders_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextCream
                )
            }
            Text(
                text = "Reminders",
                color = TextCream,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SurfaceCardElevated)
                    .testTag("reminders_add_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Reminder",
                    tint = TextCream
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Reminders List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(reminders, key = { it.id }) { reminder ->
                val icon = when {
                    reminder.title.contains("water", ignoreCase = true) -> Icons.Default.LocalDrink
                    reminder.title.contains("study", ignoreCase = true) || reminder.title.contains("exam", ignoreCase = true) -> Icons.Default.School
                    reminder.title.contains("mom", ignoreCase = true) || reminder.title.contains("call", ignoreCase = true) -> Icons.Default.Phone
                    reminder.title.contains("brother", ignoreCase = true) || reminder.title.contains("chat", ignoreCase = true) -> Icons.Default.Chat
                    else -> Icons.Default.Notifications
                }

                val iconBg = when {
                    reminder.title.contains("water", ignoreCase = true) -> Color(0xFF1E3A5F)
                    reminder.title.contains("study", ignoreCase = true) -> Color(0xFF3E2F18)
                    reminder.title.contains("mom", ignoreCase = true) -> Color(0xFF3F212B)
                    reminder.title.contains("brother", ignoreCase = true) -> Color(0xFF2C1F3F)
                    else -> Color(0xFF2E2620)
                }

                val iconColor = when {
                    reminder.title.contains("water", ignoreCase = true) -> Color(0xFF60A5FA)
                    reminder.title.contains("study", ignoreCase = true) -> AccentAmber
                    reminder.title.contains("mom", ignoreCase = true) -> Color(0xFFF472B6)
                    reminder.title.contains("brother", ignoreCase = true) -> Color(0xFFA78BFA)
                    else -> TextMuted
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceCardDark)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = reminder.title,
                            tint = iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = reminder.title,
                            color = TextCream,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = reminder.timeText,
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Switch(
                        checked = reminder.isEnabled,
                        onCheckedChange = { onToggle(reminder) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF261405),
                            checkedTrackColor = AccentAmber,
                            uncheckedThumbColor = TextSubtle,
                            uncheckedTrackColor = SurfaceCardElevated
                        )
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(onClick = { onDelete(reminder) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = TextSubtle,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddReminderDialog(
            onDismiss = { showDialog = false },
            onConfirm = { title, time ->
                onAdd(
                    ReminderItem(
                        title = title,
                        timeText = time,
                        isEnabled = true
                    )
                )
                showDialog = false
            }
        )
    }
}

@Composable
fun AddReminderDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var timeText by remember { mutableStateOf("Today, 6:00 PM") }

    val templates = listOf(
        "Drink water",
        "Study for exam",
        "Take a short break",
        "Call mom",
        "Chat with brother 💙",
        "Prepare for tomorrow"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Reminder", color = TextCream) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Quick templates:", color = TextMuted, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    templates.take(3).forEach { t ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceCardElevated)
                                .clickable { title = t }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(t, color = AccentAmber, fontSize = 11.sp)
                        }
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Reminder Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextCream,
                        unfocusedTextColor = TextCream
                    )
                )
                OutlinedTextField(
                    value = timeText,
                    onValueChange = { timeText = it },
                    label = { Text("When") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextCream,
                        unfocusedTextColor = TextCream
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) onConfirm(title, timeText)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AccentAmber)
            ) {
                Text("Add Reminder", color = Color(0xFF261405))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        containerColor = SurfaceCardElevated
    )
}
