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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PersonalityMode
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BackgroundWarmDark
import com.example.ui.theme.ModeComfortAccent
import com.example.ui.theme.ModeComfortBg
import com.example.ui.theme.ModeComfortBorder
import com.example.ui.theme.ModeFunnyAccent
import com.example.ui.theme.ModeFunnyBg
import com.example.ui.theme.ModeFunnyBorder
import com.example.ui.theme.ModeIrritativeAccent
import com.example.ui.theme.ModeIrritativeBg
import com.example.ui.theme.ModeIrritativeBorder
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceBorderSubtle
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextCream
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.WhatsAppBlueTicks
import com.example.ui.theme.WhatsAppGreenBubble
import com.example.ui.theme.WhatsAppReceivedBubble
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModePill(
            title = "Comfort",
            emoji = "🥺",
            isSelected = currentMode == PersonalityMode.COMFORT,
            accentColor = ModeComfortAccent,
            bgColor = ModeComfortBg,
            borderColor = ModeComfortBorder,
            onClick = { onModeSelected(PersonalityMode.COMFORT) },
            modifier = Modifier.weight(1f)
        )
        ModePill(
            title = "Irritative",
            emoji = "😈",
            isSelected = currentMode == PersonalityMode.IRRITATIVE,
            accentColor = ModeIrritativeAccent,
            bgColor = ModeIrritativeBg,
            borderColor = ModeIrritativeBorder,
            onClick = { onModeSelected(PersonalityMode.IRRITATIVE) },
            modifier = Modifier.weight(1f)
        )
        ModePill(
            title = "Funny",
            emoji = "😄",
            isSelected = currentMode == PersonalityMode.FUNNY,
            accentColor = ModeFunnyAccent,
            bgColor = ModeFunnyBg,
            borderColor = ModeFunnyBorder,
            onClick = { onModeSelected(PersonalityMode.FUNNY) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ModePill(
    title: String,
    emoji: String,
    isSelected: Boolean,
    accentColor: Color,
    bgColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (isSelected) bgColor else SurfaceCardDark.copy(alpha = 0.6f)
    val border = if (isSelected) borderColor else SurfaceBorderSubtle

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .border(1.2.dp, border, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = emoji, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            color = if (isSelected) TextCream else TextMuted,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

@Composable
fun WhatsAppChatBubble(
    text: String,
    isFromSister: Boolean,
    timestamp: Long,
    modifier: Modifier = Modifier
) {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val formattedTime = timeFormat.format(Date(timestamp))

    val bubbleBg = if (isFromSister) WhatsAppGreenBubble else WhatsAppReceivedBubble
    val bubbleShape = if (isFromSister) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 3.dp),
        contentAlignment = if (isFromSister) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 290.dp)
                .clip(bubbleShape)
                .background(bubbleBg)
                .border(
                    width = 0.8.dp,
                    color = if (isFromSister) Color(0x3353BDEB) else SurfaceBorderSubtle,
                    shape = bubbleShape
                )
                .padding(horizontal = 12.dp, vertical = 7.dp)
        ) {
            Text(
                text = text,
                color = TextCream,
                fontSize = 14.5.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedTime,
                    color = if (isFromSister) TextCream.copy(alpha = 0.65f) else TextSubtle,
                    fontSize = 10.5.sp
                )
                if (isFromSister) {
                    Spacer(modifier = Modifier.width(4.dp))
                    // WhatsApp style double tick
                    Row {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Read",
                            tint = WhatsAppBlueTicks,
                            modifier = Modifier.size(12.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = WhatsAppBlueTicks,
                            modifier = Modifier
                                .size(12.dp)
                                .offset(x = (-6).dp)
                        )
                    }
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
    val reactions = listOf(
        "I miss you 🥺",
        "I'm sad today",
        "Roast me 😂",
        "Why are you ignoring me 😒",
        "You're the best 💙",
        "Start 25 min timer ⏱️",
        "Remind me to drink water 💧"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        reactions.forEach { reaction ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceCardElevated)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                    .clickable { onSelectReaction(reaction) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = reaction,
                    color = TextCream,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium
                )
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
            .border(width = 1.dp, color = SurfaceBorder, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        containerColor = SurfaceDark,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(Screen.Home, Icons.Filled.Home, Icons.Outlined.Home),
            Triple(Screen.Chat, Icons.Filled.Chat, Icons.Outlined.Chat),
            Triple(Screen.ToolsMenu, Icons.Filled.GridView, Icons.Outlined.GridView),
            Triple(Screen.Settings, Icons.Filled.Settings, Icons.Outlined.Settings)
        )

        items.forEach { (screen, selectedIcon, unselectedIcon) ->
            val isSelected = currentScreen.route == screen.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) selectedIcon else unselectedIcon,
                        contentDescription = screen.title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentAmber,
                    selectedTextColor = AccentAmber,
                    indicatorColor = SurfaceCardElevated,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}
