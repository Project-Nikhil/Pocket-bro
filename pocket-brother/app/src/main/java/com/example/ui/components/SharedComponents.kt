package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PersonalityMode
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PocketModeSwitcher(
    currentMode: PersonalityMode,
    onModeSelected: (PersonalityMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceDark)
                .border(0.8.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                .padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ModeSegmentButton(
                label = "COMFORT",
                subLabel = "Empathetic",
                isSelected = currentMode == PersonalityMode.COMFORT,
                accentColor = ModeComfortAccent,
                onClick = { onModeSelected(PersonalityMode.COMFORT) },
                modifier = Modifier.weight(1f)
            )
            ModeSegmentButton(
                label = "SHARP TEASE",
                subLabel = "Roast & Banter",
                isSelected = currentMode == PersonalityMode.IRRITATIVE,
                accentColor = ModeIrritativeAccent,
                onClick = { onModeSelected(PersonalityMode.IRRITATIVE) },
                modifier = Modifier.weight(1f)
            )
            ModeSegmentButton(
                label = "WITTY LOGIC",
                subLabel = "Humor & Quick",
                isSelected = currentMode == PersonalityMode.FUNNY,
                accentColor = ModeFunnyAccent,
                onClick = { onModeSelected(PersonalityMode.FUNNY) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ModeSegmentButton(
    label: String,
    subLabel: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (isSelected) SurfaceCardElevated else Color.Transparent
    val border = if (isSelected) accentColor.copy(alpha = 0.5f) else Color.Transparent

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(0.8.dp, border, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 5.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(accentColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = label,
                color = if (isSelected) TextCream else TextMuted,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
        Text(
            text = subLabel,
            color = if (isSelected) accentColor else TextSubtle,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun WhatsAppChatBubble(
    text: String,
    isFromSister: Boolean,
    timestamp: Long,
    attachmentUri: String? = null,
    attachmentType: String? = null,
    attachmentName: String? = null,
    attachmentSize: Long = 0L,
    modifier: Modifier = Modifier
) {
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val formattedTime = timeFormat.format(Date(timestamp))

    val bubbleBg = if (isFromSister) Color(0xFF131F33) else Color(0xFF0F1420)
    val bubbleBorder = if (isFromSister) Color(0xFF263D66) else SurfaceBorder
    val bubbleShape = RoundedCornerShape(
        topStart = 10.dp,
        topEnd = 10.dp,
        bottomStart = if (isFromSister) 10.dp else 2.dp,
        bottomEnd = if (isFromSister) 2.dp else 10.dp
    )

    // Check if message has an AI model header tag
    val hasModelTag = text.startsWith("[") && text.contains("]")
    val modelTag = if (hasModelTag) text.substringAfter("[").substringBefore("]") else null
    val bodyText = if (hasModelTag) text.substringAfter("]").trim() else text

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = if (isFromSister) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 310.dp)
                .clip(bubbleShape)
                .background(bubbleBg)
                .border(0.8.dp, bubbleBorder, bubbleShape)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Developer Terminal Metadata Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isFromSister) "> OPERATOR" else "> BROTHER_NODE",
                    color = if (isFromSister) AccentCyan else AccentIndigo,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold
                )

                if (modelTag != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF1E283E))
                            .border(0.6.dp, AccentCyan.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(AccentEmerald, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = modelTag.uppercase(Locale.getDefault()),
                                color = AccentCyan,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Render attachment payload if present
            if (attachmentUri != null || attachmentType != null) {
                val type = attachmentType?.uppercase(Locale.getDefault()) ?: "DATA_PAYLOAD"
                val name = attachmentName ?: "payload"
                val sizeStr = if (attachmentSize > 0) {
                    if (attachmentSize > 1024 * 1024) String.format(Locale.getDefault(), "%.1f MB", attachmentSize / (1024f * 1024f))
                    else "${attachmentSize / 1024} KB"
                } else ""

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF0A0E17))
                        .border(0.6.dp, SurfaceBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val badgeColor = when (type) {
                        "IMAGE" -> AccentCyan
                        "PDF" -> AccentRose
                        "AUDIO" -> AccentPurple
                        "DOC", "DOCX" -> Color(0xFF38BDF8)
                        else -> AccentAmber
                    }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(badgeColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (type) {
                                "IMAGE" -> "IMG"
                                "PDF" -> "PDF"
                                "AUDIO" -> "AUD"
                                "DOC", "DOCX" -> "DOC"
                                "TEXT" -> "TXT"
                                else -> "BIN"
                            },
                            color = badgeColor,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = name,
                            color = TextCream,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                        Text(
                            text = if (sizeStr.isNotBlank()) "TYPE: $type | SIZE: $sizeStr" else "TYPE: $type",
                            color = TextSubtle,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Message Payload Content
            if (bodyText.isNotBlank()) {
                Text(
                    text = bodyText,
                    color = TextCream,
                    fontSize = 13.5.sp,
                    lineHeight = 19.sp,
                    letterSpacing = 0.2.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Telemetry Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedTime,
                    color = TextSubtle,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.5.sp
                )
                if (isFromSister) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "✓✓",
                        color = AccentCyan,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun QuickReactionsRow(
    onSelectReaction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val prompts = listOf(
        "Run brother check-in",
        "Help me understand a topic",
        "Give me honest life advice",
        "Drop a sibling roast",
        "Start 25m focus timer",
        "Add exam study reminder",
        "What's your current model status?"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        prompts.forEach { prompt ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceCardDark)
                    .border(0.8.dp, SurfaceBorder, RoundedCornerShape(6.dp))
                    .clickable { onSelectReaction(prompt) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "> ",
                        color = AccentCyan,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = prompt,
                        color = TextMuted,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun PocketBottomBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .border(width = 0.8.dp, color = SurfaceBorder, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        containerColor = SurfaceDark,
        tonalElevation = 4.dp
    ) {
        val items = listOf(
            Triple(Screen.Home, Icons.Filled.Home, Icons.Outlined.Home),
            Triple(Screen.Chat, Icons.Filled.Chat, Icons.Outlined.Chat),
            Triple(Screen.ToolsMenu, Icons.Filled.GridView, Icons.Outlined.GridView),
            Triple(Screen.Settings, Icons.Filled.Settings, Icons.Outlined.Settings)
        )

        items.forEach { (screen, selectedIcon, unselectedIcon) ->
            val isSelected = currentScreen.route == screen.route
            val navLabel = when (screen) {
                Screen.Home -> "DASHBOARD"
                Screen.Chat -> "CONSOLE"
                Screen.ToolsMenu -> "MODULES"
                Screen.Settings -> "SYSTEM"
                else -> screen.title.uppercase(Locale.getDefault())
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) selectedIcon else unselectedIcon,
                        contentDescription = screen.title,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = navLabel,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        letterSpacing = 0.5.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentCyan,
                    selectedTextColor = AccentCyan,
                    indicatorColor = Color(0xFF1E283E),
                    unselectedIconColor = TextSubtle,
                    unselectedTextColor = TextSubtle
                )
            )
        }
    }
}
