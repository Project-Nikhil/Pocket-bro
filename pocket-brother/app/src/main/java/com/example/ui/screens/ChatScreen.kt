package com.example.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.PersonalityMode
import com.example.data.model.RelationshipType
import com.example.ui.components.BrotherAvatar
import com.example.ui.components.BrotherVoiceCallDialog
import com.example.ui.components.EmojiPickerView
import com.example.ui.components.PocketModeSwitcher
import com.example.ui.components.QuickReactionsRow
import com.example.ui.components.WhatsAppChatBubble
import com.example.ui.components.insertEmojiAtCursor
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
import android.widget.Toast
import com.example.ai.AIModelStatus
import com.example.ai.EngineType
import com.example.ai.GemmaDownloadState
import com.example.ai.ModelRamTier
import java.util.Locale

data class PendingAttachment(
    val uri: String,
    val type: String,
    val name: String,
    val size: Long
)

@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    currentMode: PersonalityMode,
    isTyping: Boolean,
    relationship: RelationshipType,
    sisterName: String,
    recentEmojis: List<String> = emptyList(),
    aiStatus: AIModelStatus = AIModelStatus(),
    onSendMessage: (String) -> Unit,
    onSendAttachmentMessage: (String, String?, String?, String?, Long) -> Unit = { text, _, _, _, _ -> onSendMessage(text) },
    onAddRecentEmoji: (String) -> Unit = {},
    onModeChange: (PersonalityMode) -> Unit,
    onRoleChange: (RelationshipType) -> Unit,
    onClearChat: () -> Unit,
    onBack: () -> Unit,
    onNavigateToSettings: () -> Unit = {},
    onSelectEngine: (EngineType) -> Unit = {},
    onSelectTier: (ModelRamTier) -> Unit = {},
    onActivateTier: (ModelRamTier) -> Unit = {},
    onDownloadTier: (ModelRamTier) -> Unit = {},
    onImportTier: (ModelRamTier, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var inputFieldState by remember { mutableStateOf(TextFieldValue("")) }
    var showMenu by remember { mutableStateOf(false) }
    var showCallDialog by remember { mutableStateOf(false) }
    var showEmojiPicker by remember { mutableStateOf(false) }
    var showAiModelDialog by remember { mutableStateOf(false) }
    var importTargetTier by remember { mutableStateOf<ModelRamTier?>(null) }
    var showImportDialog by remember { mutableStateOf(false) }
    var importPathInput by remember { mutableStateOf("") }
    var selectedAttachment by remember { mutableStateOf<PendingAttachment?>(null) }

    val listState = rememberLazyListState()

    // Activity result launcher for importing offline LLM model weights (.bin, .gguf, .tflite)
    val modelPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null && importTargetTier != null) {
            val tier = importTargetTier!!
            onImportTier(tier, uri.toString())
            onActivateTier(tier)
            Toast.makeText(context, "Loaded and activated ${tier.modelName}!", Toast.LENGTH_SHORT).show()
            importTargetTier = null
        }
    }

    // Activity result launcher for images / photos
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val name = queryFileName(context, uri) ?: "photo_${System.currentTimeMillis()}.jpg"
            val size = queryFileSize(context, uri)
            selectedAttachment = PendingAttachment(
                uri = uri.toString(),
                type = "IMAGE",
                name = name,
                size = size
            )
        }
    }

    // Activity result launcher for all documents, PDFs, DOC/DOCX, text, audio files
    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val mime = context.contentResolver.getType(uri) ?: ""
            val name = queryFileName(context, uri) ?: "file_${System.currentTimeMillis()}"
            val size = queryFileSize(context, uri)
            val type = when {
                mime.startsWith("image/") -> "IMAGE"
                mime.contains("pdf") || name.endsWith(".pdf", ignoreCase = true) -> "PDF"
                mime.contains("word") || name.endsWith(".doc", ignoreCase = true) || name.endsWith(".docx", ignoreCase = true) -> "DOC"
                mime.startsWith("audio/") || name.endsWith(".mp3", ignoreCase = true) || name.endsWith(".m4a", ignoreCase = true) || name.endsWith(".wav", ignoreCase = true) -> "AUDIO"
                mime.startsWith("text/") || name.endsWith(".txt", ignoreCase = true) -> "TEXT"
                else -> "FILE"
            }
            selectedAttachment = PendingAttachment(
                uri = uri.toString(),
                type = type,
                name = name,
                size = size
            )
        }
    }

    // Handle back button on modals and pickers first
    BackHandler(enabled = showEmojiPicker || selectedAttachment != null || showCallDialog || showAiModelDialog || showImportDialog) {
        if (showEmojiPicker) {
            showEmojiPicker = false
        } else if (selectedAttachment != null) {
            selectedAttachment = null
        } else if (showCallDialog) {
            showCallDialog = false
        } else if (showImportDialog) {
            showImportDialog = false
        } else if (showAiModelDialog) {
            showAiModelDialog = false
        }
    }

    // Scroll to bottom when messages change or brother starts typing
    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhatsAppChatBg)
    ) {
        // 1. WhatsApp-Style Header matching mockup with statusBarsPadding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceDark)
                .statusBarsPadding()
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
                    Spacer(modifier = Modifier.width(8.dp))

                    // Model Badge chip - tap to open Choose AI Model dialog
                    val isGemmaActive = aiStatus.activeEngine == EngineType.GEMMA_LOCAL_LLM
                    val modelLabel = if (isGemmaActive) {
                        "✨ ${aiStatus.selectedTier.modelName.substringBefore(" (")}"
                    } else {
                        "⚡ Instant Core"
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isGemmaActive) Color(0xFF382914) else Color(0xFF1E293B))
                            .border(
                                width = 0.8.dp,
                                color = if (isGemmaActive) AccentAmber else Color(0xFF475569),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { showAiModelDialog = true }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$modelLabel ▾",
                            color = if (isGemmaActive) AccentAmber else Color(0xFF94A3B8),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(
                    text = if (isTyping) "typing..." else currentMode.subtitle,
                    color = if (isTyping) Color(0xFF25D366) else TextMuted,
                    fontSize = 11.5.sp
                )
            }

            // Voice Call Icon with genuine STT-AI-TTS call pipeline
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

            // Menu Icon (3 Dots)
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
                    val isCurrentlyGemma = aiStatus.activeEngine == EngineType.GEMMA_LOCAL_LLM
                    val currentTierDownload = aiStatus.tierDownloadStates[aiStatus.selectedTier.id]
                    val isSelectedTierReady = currentTierDownload is GemmaDownloadState.Ready

                    // 1. Primary Option: Choose AI Model to Answer
                    DropdownMenuItem(
                        text = {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Choose AI Model to Answer", color = TextCream, fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = if (isCurrentlyGemma)
                                        "Active: ${aiStatus.selectedTier.modelName.substringBefore(" (")} (Neural LLM)"
                                    else "Active: Instant Offline Core (Rule Engine)",
                                    color = if (isCurrentlyGemma) AccentAmber else Color(0xFF80D8FF),
                                    fontSize = 10.5.sp,
                                    modifier = Modifier.padding(start = 26.dp)
                                )
                            }
                        },
                        onClick = {
                            showMenu = false
                            showAiModelDialog = true
                        }
                    )

                    // 2. Quick Switch between AI Model and Instant Core
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isCurrentlyGemma) Icons.Default.Bolt else Icons.Default.Memory,
                                    contentDescription = null,
                                    tint = if (isCurrentlyGemma) Color(0xFF80D8FF) else Color(0xFF9CCC65),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isCurrentlyGemma)
                                        "Switch to: Instant Core"
                                    else if (isSelectedTierReady)
                                        "Switch to: ${aiStatus.selectedTier.modelName.substringBefore(" (")}"
                                    else "Download & Switch to Gemma AI",
                                    color = TextCream
                                )
                            }
                        },
                        onClick = {
                            showMenu = false
                            if (isCurrentlyGemma) {
                                onSelectEngine(EngineType.BUILTIN_OFFLINE_CORE)
                                Toast.makeText(context, "Switched to Instant Core engine", Toast.LENGTH_SHORT).show()
                            } else {
                                if (isSelectedTierReady) {
                                    onActivateTier(aiStatus.selectedTier)
                                    Toast.makeText(context, "Switched to ${aiStatus.selectedTier.modelName}! AI model will now answer.", Toast.LENGTH_SHORT).show()
                                } else {
                                    showAiModelDialog = true
                                }
                            }
                        }
                    )

                    // 3. Switch Brother Role
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

                    // 4. Clear Conversation
                    DropdownMenuItem(
                        text = { Text("Clear Conversation", color = Color(0xFFF87171)) },
                        onClick = {
                            onClearChat()
                            showMenu = false
                        }
                    )

                    // 5. Open Detailed AI & Storage Settings
                    DropdownMenuItem(
                        text = {
                            Text("Manage AI Models & Settings", color = TextSubtle, fontSize = 12.sp)
                        },
                        onClick = {
                            showMenu = false
                            onNavigateToSettings()
                        }
                    )
                }
            }
        }

        // 2. Personality Mode Switcher Pills
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
                    timestamp = msg.timestamp,
                    attachmentUri = msg.attachmentUri,
                    attachmentType = msg.attachmentType,
                    attachmentName = msg.attachmentName,
                    attachmentSize = msg.attachmentSize
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

        // 4. Quick Reaction Chips
        QuickReactionsRow(
            onSelectReaction = { reaction ->
                onSendMessage(reaction)
            }
        )

        // 5. Message Composer Bar & Attachment Preview sitting directly above the keyboard
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime)),
            color = SurfaceDark,
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Attachment preview before sending
                AnimatedVisibility(
                    visible = selectedAttachment != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    selectedAttachment?.let { att ->
                        val sizeStr = if (att.size > 0) {
                            if (att.size > 1024 * 1024) String.format(Locale.getDefault(), "%.1f MB", att.size / (1024f * 1024f))
                            else "${att.size / 1024} KB"
                        } else ""

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(SurfaceCardDark)
                                .border(1.dp, AccentAmber.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val emojiIcon = when (att.type) {
                                "IMAGE" -> "🖼️"
                                "PDF" -> "📄"
                                "AUDIO" -> "🎧"
                                "DOC" -> "📝"
                                "TEXT" -> "📋"
                                else -> "📁"
                            }
                            Text(emojiIcon, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = att.name,
                                    color = TextCream,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${att.type} ${if (sizeStr.isNotBlank()) "• $sizeStr" else ""}",
                                    color = AccentAmber,
                                    fontSize = 11.sp
                                )
                            }
                            IconButton(
                                onClick = { selectedAttachment = null },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove attachment", tint = TextMuted)
                            }
                        }
                    }
                }

                // Composer input row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Emoji Icon with real emoji picker
                    IconButton(
                        onClick = { showEmojiPicker = !showEmojiPicker },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SentimentSatisfiedAlt,
                            contentDescription = "Emoji",
                            tint = if (showEmojiPicker) AccentAmber else TextMuted,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Input Field with cursor position support
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(22.dp))
                            .background(SurfaceCardDark)
                            .border(0.8.dp, SurfaceBorder, RoundedCornerShape(22.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        if (inputFieldState.text.isEmpty()) {
                            Text(
                                text = "Message brother...",
                                color = TextSubtle,
                                fontSize = 14.sp
                            )
                        }
                        BasicTextField(
                            value = inputFieldState,
                            onValueChange = {
                                inputFieldState = it
                                if (showEmojiPicker) showEmojiPicker = false
                            },
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

                    Spacer(modifier = Modifier.width(6.dp))

                    // File / Document Attachment Icon
                    IconButton(
                        onClick = {
                            documentPickerLauncher.launch("*/*")
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = "Attach Document or File",
                            tint = TextMuted,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Photo Picker Icon
                    IconButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Attach Photo",
                            tint = TextMuted,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Send or Voice Note Button
                    val hasText = inputFieldState.text.isNotBlank()
                    val hasAttachment = selectedAttachment != null
                    val isSendActive = hasText || hasAttachment

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (isSendActive) WhatsAppGreenBubble else AccentAmber)
                            .clickable {
                                if (isSendActive) {
                                    val textToSend = inputFieldState.text.trim()
                                    val att = selectedAttachment
                                    if (att != null) {
                                        onSendAttachmentMessage(textToSend, att.uri, att.type, att.name, att.size)
                                        selectedAttachment = null
                                    } else {
                                        onSendMessage(textToSend)
                                    }
                                    inputFieldState = TextFieldValue("")
                                    showEmojiPicker = false
                                } else {
                                    onSendMessage("🎤 [Voice Note]: hey brother! thinking of you today ♡")
                                }
                            }
                            .testTag("chat_send_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSendActive) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
                            contentDescription = if (isSendActive) "Send" else "Voice Note",
                            tint = if (isSendActive) TextCream else Color(0xFF261405),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Proper Emoji Picker View with search, categories, and recent history
                AnimatedVisibility(
                    visible = showEmojiPicker,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    EmojiPickerView(
                        onEmojiSelected = { emoji ->
                            inputFieldState = insertEmojiAtCursor(inputFieldState, emoji)
                            onAddRecentEmoji(emoji)
                        },
                        recentEmojis = recentEmojis,
                        onClose = { showEmojiPicker = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    )
                }
            }
        }
    }

    // Genuine STT-AI-TTS Voice Call Dialog
    if (showCallDialog) {
        BrotherVoiceCallDialog(
            sisterName = sisterName,
            relationship = relationship,
            currentMode = currentMode,
            onSendMessageToHistory = { voicePrompt ->
                onSendMessage(voicePrompt)
            },
            onDismiss = { showCallDialog = false }
        )
    }

    // Interactive Choose AI Model Dialog
    if (showAiModelDialog) {
        ChooseAiModelDialog(
            aiStatus = aiStatus,
            onSelectEngine = onSelectEngine,
            onSelectTier = onSelectTier,
            onActivateTier = onActivateTier,
            onDownloadTier = onDownloadTier,
            onImportTierClick = { tier ->
                importTargetTier = tier
                showImportDialog = true
            },
            onDismiss = { showAiModelDialog = false }
        )
    }

    // Import Model File Dialog
    if (showImportDialog && importTargetTier != null) {
        val tier = importTargetTier!!
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = {
                Text(
                    text = "Import ${tier.modelName}",
                    color = TextCream,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Import downloaded weights (.bin, .gguf, .tflite) for this offline LLM tier:",
                        color = TextMuted,
                        fontSize = 12.5.sp
                    )
                    Button(
                        onClick = {
                            showImportDialog = false
                            modelPickerLauncher.launch("*/*")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentAmber),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.FolderOpen, contentDescription = null, tint = Color(0xFF261405), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pick File from Storage...", color = Color(0xFF261405), fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Or enter file path on device:", color = TextSubtle, fontSize = 11.5.sp)
                    OutlinedTextField(
                        value = importPathInput,
                        onValueChange = { importPathInput = it },
                        placeholder = { Text("/sdcard/Download/${tier.fileName}", color = TextSubtle, fontSize = 11.5.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val path = importPathInput.ifBlank { "/sdcard/Download/${tier.fileName}" }
                        onImportTier(tier, path)
                        onActivateTier(tier)
                        Toast.makeText(context, "Imported and activated ${tier.modelName}!", Toast.LENGTH_SHORT).show()
                        showImportDialog = false
                    }
                ) {
                    Text("Import & Activate", color = AccentAmber, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = SurfaceCardDark
        )
    }
}

@Composable
fun ChooseAiModelDialog(
    aiStatus: AIModelStatus,
    onSelectEngine: (EngineType) -> Unit,
    onSelectTier: (ModelRamTier) -> Unit,
    onActivateTier: (ModelRamTier) -> Unit,
    onDownloadTier: (ModelRamTier) -> Unit,
    onImportTierClick: (ModelRamTier) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val ramInfo = aiStatus.deviceRamInfo

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, SurfaceBorder, RoundedCornerShape(24.dp)),
            color = SurfaceDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF3E2D18)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = AccentAmber,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Choose AI Model",
                                color = TextCream,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Select how Brother answers you in chat",
                                color = TextMuted,
                                fontSize = 11.5.sp
                            )
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable List of Options
                LazyColumn(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Active Engine Status Banner
                    item {
                        val isGemma = aiStatus.activeEngine == EngineType.GEMMA_LOCAL_LLM
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isGemma) Color(0xFF2E2416) else Color(0xFF1B2836))
                                .border(1.2.dp, if (isGemma) AccentAmber else Color(0xFF38BDF8), RoundedCornerShape(14.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(if (isGemma) Color(0xFF22C55E) else Color(0xFF38BDF8), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isGemma) "Active: ${aiStatus.selectedTier.modelName}" else "Active: Instant Offline Core",
                                        color = TextCream,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isGemma) "Dynamic On-Device Neural LLM • AI reasoning active" else "Rule & Intent Engine • Fixed reply templates",
                                        color = if (isGemma) AccentAmber else Color(0xFF7DD3FC),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    // Section 1: Gemma Local LLM (AI Model)
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "🧠 Gemma On-Device Local LLM (AI Model)",
                                color = AccentAmber,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Answers dynamically like an LLM — nuanced advice, study answers, and sisterly humor rather than hardcoded replies.",
                                color = TextMuted,
                                fontSize = 11.5.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // Tiers List
                    items(ModelRamTier.entries) { tier ->
                        val tierState = aiStatus.tierDownloadStates[tier.id] ?: GemmaDownloadState.NotDownloaded
                        val isReady = tierState is GemmaDownloadState.Ready
                        val isDownloading = tierState is GemmaDownloadState.Downloading
                        val isThisTierActive = aiStatus.activeEngine == EngineType.GEMMA_LOCAL_LLM && aiStatus.selectedTier == tier
                        val isRecommended = ramInfo.recommendedTier == tier
                        val isExceedingRam = tier.minRamGb > ramInfo.totalRamGb

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isThisTierActive) Color(0xFF2E2318) else SurfaceCardDark)
                                .border(
                                    width = if (isThisTierActive) 1.5.dp else 1.dp,
                                    color = if (isThisTierActive) AccentAmber else SurfaceBorder,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Header row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = tier.modelName,
                                            color = TextCream,
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (isThisTierActive) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(AccentAmber)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text("ACTIVE", color = Color(0xFF261405), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    Text(tier.subtitle, color = TextMuted, fontSize = 11.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SurfaceCardElevated)
                                        .padding(horizontal = 7.dp, vertical = 3.dp)
                                ) {
                                    Text("${tier.downloadSizeMb} MB", color = AccentAmber, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // RAM compatibility badges
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (isRecommended) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFF233E1A))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("⭐ Best match for your phone", color = Color(0xFF86EFAC), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                } else if (isExceedingRam) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFF3F1D1D))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("Requires ${tier.minRamGb}GB RAM", color = Color(0xFFFCA5A5), fontSize = 10.sp)
                                    }
                                }
                            }

                            // State & Action Buttons
                            when {
                                isThisTierActive -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFF1E3A1A))
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Currently Answering Chat ✓", color = Color(0xFF86EFAC), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                isReady -> {
                                    Button(
                                        onClick = {
                                            onActivateTier(tier)
                                            Toast.makeText(context, "Switched to ${tier.modelName}! AI model will now answer.", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = AccentAmber),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF261405), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Set as Active AI Model", color = Color(0xFF261405), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                                isDownloading -> {
                                    val prog = (tierState as GemmaDownloadState.Downloading)
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Downloading model weights...", color = TextCream, fontSize = 11.sp)
                                            Text("${(prog.progress * 100).toInt()}% (${prog.downloadedMb.toInt()}/${prog.totalMb.toInt()} MB)", color = AccentAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                        LinearProgressIndicator(
                                            progress = { prog.progress },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(6.dp)
                                                .clip(RoundedCornerShape(3.dp)),
                                            color = AccentAmber,
                                            trackColor = SurfaceCardElevated
                                        )
                                    }
                                }
                                else -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { onDownloadTier(tier) },
                                            colors = ButtonDefaults.buttonColors(containerColor = if (isRecommended) AccentAmber else SurfaceCardElevated),
                                            modifier = Modifier.weight(1.3f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Download,
                                                contentDescription = null,
                                                tint = if (isRecommended) Color(0xFF261405) else TextCream,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Download (${tier.downloadSizeMb} MB)",
                                                color = if (isRecommended) Color(0xFF261405) else TextCream,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        OutlinedButton(
                                            onClick = { onImportTierClick(tier) },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextCream),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                                            modifier = Modifier.weight(0.9f)
                                        ) {
                                            Text("Import", fontSize = 11.5.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Section 2: Instant Offline Core
                    item {
                        val isCoreActive = aiStatus.activeEngine == EngineType.BUILTIN_OFFLINE_CORE
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isCoreActive) Color(0xFF1B2836) else SurfaceCardDark)
                                .border(
                                    width = if (isCoreActive) 1.5.dp else 1.dp,
                                    color = if (isCoreActive) Color(0xFF38BDF8) else SurfaceBorder,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("⚡ Instant Offline Core", color = TextCream, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                                        if (isCoreActive) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFF38BDF8))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text("ACTIVE", color = Color(0xFF0F172A), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    Text("Zero storage • Rule & intent engine with tool automation", color = TextMuted, fontSize = 11.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SurfaceCardElevated)
                                        .padding(horizontal = 7.dp, vertical = 3.dp)
                                ) {
                                    Text("0 MB", color = Color(0xFF38BDF8), fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            if (isCoreActive) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF0F2942))
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Currently Answering Chat ✓", color = Color(0xFF7DD3FC), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Button(
                                    onClick = {
                                        onSelectEngine(EngineType.BUILTIN_OFFLINE_CORE)
                                        Toast.makeText(context, "Switched to Instant Core engine", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardElevated),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Switch to Instant Core", color = TextCream, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }

                    // Helpful Hint Box
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E242B))
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "💡 When Gemma AI is active, Brother's replies are generated dynamically and prefixed with [Gemma 2B]. You can switch models anytime from the 3 dots menu or by tapping the badge in the chat header.",
                                    color = TextSubtle,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmber),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Done", color = Color(0xFF261405), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private fun queryFileName(context: Context, uri: Uri): String? {
    if (uri.scheme == "content") {
        try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    return cursor.getString(nameIndex)
                }
            }
        } catch (_: Exception) {}
    }
    return uri.lastPathSegment
}

private fun queryFileSize(context: Context, uri: Uri): Long {
    if (uri.scheme == "content") {
        try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (sizeIndex != -1 && cursor.moveToFirst()) {
                    return cursor.getLong(sizeIndex)
                }
            }
        } catch (_: Exception) {}
    }
    return 0L
}
