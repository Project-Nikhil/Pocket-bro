package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.PersonalityMode
import com.example.data.model.RelationshipType
import com.example.ui.components.BrotherAvatar
import com.example.ui.components.PocketModeSwitcher
import com.example.ui.components.QuickReactionsRow
import com.example.ui.components.WhatsAppChatBubble
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextCream
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.WhatsAppChatBg
import com.example.ui.theme.WhatsAppGreenBubble

@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    currentMode: PersonalityMode,
    isTyping: Boolean,
    relationship: RelationshipType,
    sisterName: String,
    onSendMessage: (String) -> Unit,
    onModeChange: (PersonalityMode) -> Unit,
    onRoleChange: (RelationshipType) -> Unit,
    onClearChat: () -> Unit,
    onBack: () -> Unit,
    onNavigateToSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }
    var showCallDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Scroll to bottom when messages change
    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhatsAppChatBg)
            .statusBarsPadding()
            .windowInsetsPadding(
                WindowInsets.navigationBars.union(WindowInsets.ime)
            )
    ) {
        // 1. WhatsApp-Style Header matching mockup
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceDark)
                .border(width = 0.8.dp, color = SurfaceBorder)
                .padding(horizontal = 6.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("chat_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextCream
                )
            }

            BrotherAvatar(size = 40.dp, showOnlineDot = true)

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Brother",
                        color = TextCream,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(Color(0xFF25D366), CircleShape)
                    )
                }
                Text(
                    text = if (isTyping) "typing..." else currentMode.subtitle,
                    color = if (isTyping) Color(0xFF25D366) else TextMuted,
                    fontSize = 11.5.sp
                )
            }

            // Call Icon
            IconButton(
                onClick = { showCallDialog = true },
                modifier = Modifier.testTag("chat_call_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call Brother",
                    tint = AccentAmber
                )
            }

            // Menu Icon
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = TextCream
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(SurfaceCardElevated)
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Memory, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("AI Models & RAM (Gemma)", color = TextCream)
                            }
                        },
                        onClick = {
                            showMenu = false
                            onNavigateToSettings()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (relationship == RelationshipType.ELDER_BROTHER)
                                    "Switch to: My Younger Brother"
                                else "Switch to: My Elder Brother",
                                color = TextCream
                            )
                        },
                        onClick = {
                            val nextRole = if (relationship == RelationshipType.ELDER_BROTHER)
                                RelationshipType.YOUNGER_BROTHER else RelationshipType.ELDER_BROTHER
                            onRoleChange(nextRole)
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Clear Conversation", color = Color(0xFFF87171)) },
                        onClick = {
                            onClearChat()
                            showMenu = false
                        }
                    )
                }
            }
        }

        // 2. Personality Mode Switcher Pills matching mockup
        PocketModeSwitcher(
            currentMode = currentMode,
            onModeSelected = onModeChange
        )

        // 3. Chat Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            // "Today" Date Chip matching mockup
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceCardDark)
                            .border(0.6.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Today",
                            color = TextSubtle,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Sister-First Conversation Welcome Card (Displayed when starting fresh)
            if (messages.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BrotherAvatar(size = 56.dp, showOnlineDot = true)

                        Text(
                            text = if (relationship == RelationshipType.ELDER_BROTHER)
                                "Your Elder Brother is online 💙"
                            else "Your Younger Brother is online 💙",
                            color = TextCream,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "“Hey $sisterName! It's quiet here. I'm all ears — text me anything. Complain about your day, tell me a secret, ask for advice, or just say hi ♡”",
                            color = AccentAmber,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Tap a starter prompt or type your own below:",
                            color = TextMuted,
                            fontSize = 11.5.sp
                        )

                        val starters = listOf(
                            "Hey brother! 👋",
                            "I'm feeling really stressed about exams 🥺",
                            "Tell me something funny 😂",
                            "Remind me to drink water in 1 hour 💧",
                            "What are you doing right now?"
                        )

                        starters.forEach { starter ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(SurfaceCardDark)
                                    .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp))
                                    .clickable { onSendMessage(starter) }
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(starter, color = TextCream, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            items(messages, key = { it.id }) { msg ->
                WhatsAppChatBubble(
                    text = msg.text,
                    isFromSister = msg.isFromSister,
                    timestamp = msg.timestamp
                )
            }

            // Typing Indicator
            if (isTyping) {
                item {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceCardDark)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Brother is typing...",
                            color = AccentAmber,
                            fontSize = 12.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }

        // 4. Quick Reaction Chips matching mockup
        QuickReactionsRow(
            onSelectReaction = { reaction ->
                onSendMessage(reaction)
            }
        )

        // 5. Message Composer Bar matching mockup
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceDark)
                .border(width = 0.8.dp, color = SurfaceBorder)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji Icon
            Icon(
                imageVector = Icons.Default.SentimentSatisfiedAlt,
                contentDescription = "Emoji",
                tint = TextMuted,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { inputText += " 😊" }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Input Field
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(22.dp))
                    .background(SurfaceCardDark)
                    .border(0.8.dp, SurfaceBorder, RoundedCornerShape(22.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                if (inputText.isEmpty()) {
                    Text(
                        text = "Message...",
                        color = TextSubtle,
                        fontSize = 14.sp
                    )
                }
                BasicTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    textStyle = TextStyle(
                        color = TextCream,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(AccentAmber),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("chat_input_field")
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Attachment Icon
            Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = "Attach",
                tint = TextMuted,
                modifier = Modifier
                    .size(22.dp)
                    .clickable {
                        onSendMessage("📸 Sent a memory photo")
                    }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Camera Icon
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Camera",
                tint = TextMuted,
                modifier = Modifier
                    .size(22.dp)
                    .clickable {
                        onSendMessage("📷 Sent a selfie")
                    }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Send or Mic Button
            val isSendActive = inputText.isNotBlank()
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (isSendActive) WhatsAppGreenBubble else AccentAmber)
                    .clickable {
                        if (isSendActive) {
                            onSendMessage(inputText.trim())
                            inputText = ""
                        } else {
                            onSendMessage("🎤 [Voice Note]: hey brother! thinking of you today ♡")
                        }
                    }
                    .testTag("chat_send_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSendActive) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
                    contentDescription = if (isSendActive) "Send" else "Voice",
                    tint = if (isSendActive) TextCream else Color(0xFF261405),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    // Call Dialog
    if (showCallDialog) {
        AlertDialog(
            onDismissRequest = { showCallDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BrotherAvatar(size = 36.dp, showOnlineDot = false)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Brother Calling...", color = TextCream, fontSize = 16.sp)
                }
            },
            text = {
                Text(
                    text = "“Oye $sisterName! Pick up if you need to vent, or stop spamming my phone 😂 Remember i'm always one text away. Love you $sisterName ♡”",
                    color = AccentAmber,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showCallDialog = false }) {
                    Text("Love you bhaiya ♡", color = AccentAmber)
                }
            },
            containerColor = SurfaceCardElevated
        )
    }
}
