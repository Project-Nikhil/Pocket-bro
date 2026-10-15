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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RelationshipType
import com.example.ui.components.BrotherAvatar
import com.example.ui.components.NightSkyIllustration
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.BackgroundWarmDark
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextCream
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    sisterName: String,
    relationship: RelationshipType,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = System.currentTimeMillis()
            delay(1000)
        }
    }

    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
    val formattedTime = timeFormat.format(Date(currentTime))
    val formattedDate = dateFormat.format(Date(currentTime))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWarmDark)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // 1. Top Greeting Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BrotherAvatar(size = 48.dp, showOnlineDot = true)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hey $sisterName 👋",
                    color = TextCream,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "remember, i'm always here for you ♡",
                    color = TextMuted,
                    fontSize = 12.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Date & Time Night Sky Card matching mockup
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
        ) {
            NightSkyIllustration(modifier = Modifier.fillMaxSize())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = formattedTime,
                    color = TextCream,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formattedDate,
                    color = AccentAmber,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "A new day, new chances.\nYou can do it! ♡",
                    color = TextCream.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 3. Grid of 8 core feature cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HubCard(
                title = "Chat",
                subtitle = "Talk to me anytime ♡",
                icon = Icons.Default.ChatBubble,
                iconTint = Color(0xFF60A5FA),
                iconBg = Color(0xFF1E2E42),
                onClick = { onNavigate(Screen.Chat) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("hub_card_chat")
            )
            HubCard(
                title = "Calendar",
                subtitle = "Your events & schedules",
                icon = Icons.Default.CalendarMonth,
                iconTint = Color(0xFFFB923C),
                iconBg = Color(0xFF3F2618),
                onClick = { onNavigate(Screen.Calendar) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("hub_card_calendar")
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HubCard(
                title = "Reminders",
                subtitle = "Never forget important things",
                icon = Icons.Default.Notifications,
                iconTint = Color(0xFFFBBF24),
                iconBg = Color(0xFF3E3014),
                onClick = { onNavigate(Screen.Reminders) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("hub_card_reminders")
            )
            HubCard(
                title = "Notes",
                subtitle = "Write your thoughts",
                icon = Icons.Default.Description,
                iconTint = Color(0xFFFACC15),
                iconBg = Color(0xFF3C3415),
                onClick = { onNavigate(Screen.Notes) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("hub_card_notes")
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HubCard(
                title = "Timer",
                subtitle = "Focus & be productive",
                icon = Icons.Default.Timer,
                iconTint = Color(0xFFF87171),
                iconBg = Color(0xFF3D1E1E),
                onClick = { onNavigate(Screen.Timer) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("hub_card_timer")
            )
            HubCard(
                title = "Calculator",
                subtitle = "For your daily use",
                icon = Icons.Default.Calculate,
                iconTint = Color(0xFFC084FC),
                iconBg = Color(0xFF321F42),
                onClick = { onNavigate(Screen.Calculator) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("hub_card_calculator")
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HubCard(
                title = "Gallery",
                subtitle = "Your memories",
                icon = Icons.Default.Collections,
                iconTint = Color(0xFF2DD4BF),
                iconBg = Color(0xFF133632),
                onClick = { onNavigate(Screen.Memories) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("hub_card_memories")
            )
            HubCard(
                title = "Voice Notes",
                subtitle = "Talk instead of typing",
                icon = Icons.Default.Mic,
                iconTint = Color(0xFFF472B6),
                iconBg = Color(0xFF3C1C2B),
                onClick = { onNavigate(Screen.VoiceNotes) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("hub_card_voice_notes")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Compact Quick Access Row for Other Everyday Tools
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceCardDark)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickToolItem("To-Do", Icons.Default.Description, onClick = { onNavigate(Screen.Tasks) })
            QuickToolItem("Planner", Icons.Default.CalendarMonth, onClick = { onNavigate(Screen.StudyPlanner) })
            QuickToolItem("Clock", Icons.Default.Schedule, onClick = { onNavigate(Screen.Clock) })
            QuickToolItem("Weather", Icons.Default.WbSunny, onClick = { onNavigate(Screen.Weather) })
            QuickToolItem("Gemma AI", Icons.Default.Memory, onClick = { onNavigate(Screen.Settings) })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Quote card with brother & cat
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceCardElevated)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "“you are stronger than you think and i'm always proud of you ♡”",
                    color = AccentAmber,
                    fontSize = 13.5.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (relationship == RelationshipType.ELDER_BROTHER) "— your elder brother" else "— your younger brother",
                    color = TextSubtle,
                    fontSize = 11.5.sp
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            BrotherAvatar(size = 42.dp, showOnlineDot = false)
        }

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun HubCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceCardDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                color = TextCream,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
fun QuickToolItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = AccentAmber,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            color = TextMuted,
            fontSize = 11.sp
        )
    }
}
