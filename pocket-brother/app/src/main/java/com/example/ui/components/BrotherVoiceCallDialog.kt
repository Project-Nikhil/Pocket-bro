package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.ai.OfflineBrotherEngine
import com.example.data.model.PersonalityMode
import com.example.data.model.RelationshipType
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.BackgroundWarmDark
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextCream
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

enum class CallState(val label: String) {
    CONNECTING("Connecting with Brother..."),
    LISTENING("Listening to you... Speak now ♡"),
    THINKING("Brother is thinking..."),
    SPEAKING("Brother is speaking..."),
    READY("Tap mic to speak to Brother")
}

@Composable
fun BrotherVoiceCallDialog(
    sisterName: String,
    relationship: RelationshipType,
    currentMode: PersonalityMode,
    onSendMessageToHistory: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var callDurationSeconds by remember { mutableIntStateOf(0) }
    var callState by remember { mutableStateOf(CallState.CONNECTING) }
    var isMuted by remember { mutableStateOf(false) }
    var sisterTranscript by remember { mutableStateOf("") }
    var brotherTranscript by remember { mutableStateOf("") }
    var voicePitchElder by remember { mutableStateOf(relationship == RelationshipType.ELDER_BROTHER) }

    val offlineEngine = remember { OfflineBrotherEngine() }
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var speechRecognizer: SpeechRecognizer? by remember { mutableStateOf(null) }

    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasAudioPermission = granted
    }

    // Call duration timer
    LaunchedEffect(Unit) {
        if (!hasAudioPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
        while (true) {
            delay(1000)
            callDurationSeconds++
        }
    }

    fun speakBrotherReply(text: String) {
        val engine = tts ?: return
        callState = CallState.SPEAKING
        brotherTranscript = text

        val pitch = if (voicePitchElder) 0.90f else 1.15f
        val rate = if (voicePitchElder) 0.96f else 1.05f
        engine.setPitch(pitch)
        engine.setSpeechRate(rate)

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "brother_voice")
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, params, "brother_voice")
    }

    fun startListening() {
        if (isMuted) return
        val recognizer = speechRecognizer ?: return

        coroutineScope.launch {
            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                }
                callState = CallState.LISTENING
                recognizer.startListening(intent)
            } catch (_: Exception) {
                callState = CallState.READY
            }
        }
    }

    // Initialize TTS
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        callState = CallState.SPEAKING
                    }

                    override fun onDone(utteranceId: String?) {
                        callState = CallState.READY
                        // Automatically start listening for sister's reply after a brief pause
                        coroutineScope.launch {
                            delay(500)
                            if (callState == CallState.READY && !isMuted) {
                                startListening()
                            }
                        }
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        callState = CallState.READY
                    }
                })

                // Warm welcome greeting on call connect
                coroutineScope.launch {
                    delay(800)
                    val greeting = if (relationship == RelationshipType.ELDER_BROTHER) {
                        "Hey $sisterName! I picked up. Tell me what happened, I'm listening."
                    } else {
                        "Hey $sisterName! Oye did you miss me? What's up!"
                    }
                    speakBrotherReply(greeting)
                }
            }
        }
    }

    // Initialize SpeechRecognizer
    LaunchedEffect(hasAudioPermission) {
        if (hasAudioPermission && SpeechRecognizer.isRecognitionAvailable(context)) {
            val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
            recognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    callState = CallState.LISTENING
                }

                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {
                    callState = CallState.THINKING
                }

                override fun onError(error: Int) {
                    callState = CallState.READY
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = matches?.firstOrNull()?.trim()
                    if (!text.isNullOrBlank()) {
                        sisterTranscript = text
                        callState = CallState.THINKING

                        // Persist to chat history
                        onSendMessageToHistory(text)

                        // Generate brother answer
                        coroutineScope.launch {
                            val resp = offlineEngine.generateResponse(
                                sisterMessage = text,
                                relationship = relationship,
                                currentMode = currentMode,
                                sisterName = sisterName,
                                recentHistory = listOf(text)
                            )
                            val cleanSpoken = resp.replyMessage
                                .replace("♡", "")
                                .replace("🫂", "")
                                .replace("🥺", "")
                                .replace("😂", "")
                                .replace("🤣", "")
                                .replace("✨", "")
                                .trim()

                            speakBrotherReply(cleanSpoken)
                        }
                    } else {
                        callState = CallState.READY
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                    if (!partial.isNullOrBlank()) {
                        sisterTranscript = partial
                    }
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
            speechRecognizer = recognizer
        }
    }

    // Cleanup resources
    DisposableEffect(Unit) {
        onDispose {
            try {
                tts?.stop()
                tts?.shutdown()
            } catch (_: Exception) {}
            try {
                speechRecognizer?.stopListening()
                speechRecognizer?.destroy()
            } catch (_: Exception) {}
        }
    }

    // Pulsing animation for speaking avatar
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (callState == CallState.SPEAKING || callState == CallState.LISTENING) 1.14f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val formatSeconds = remember(callDurationSeconds) {
        val mins = callDurationSeconds / 60
        val secs = callDurationSeconds % 60
        "%02d:%02d".format(mins, secs)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWarmDark)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top: Brother Info & Duration
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Text(
                        text = if (relationship == RelationshipType.ELDER_BROTHER) "Elder Brother" else "Younger Brother",
                        color = TextCream,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatSeconds,
                        color = AccentAmber,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = callState.label,
                        color = when (callState) {
                            CallState.SPEAKING -> Color(0xFF38BDF8)
                            CallState.LISTENING -> Color(0xFF22C55E)
                            CallState.THINKING -> AccentAmber
                            else -> TextMuted
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Middle: Pulsing Avatar with glow rings
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(vertical = 20.dp)
                ) {
                    if (callState == CallState.SPEAKING || callState == CallState.LISTENING) {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(
                                    if (callState == CallState.SPEAKING)
                                        Color(0xFF38BDF8).copy(alpha = 0.15f)
                                    else Color(0xFF22C55E).copy(alpha = 0.15f)
                                )
                        )
                    }

                    BrotherAvatar(size = 96.dp, showOnlineDot = true)
                }

                // Transcription box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(SurfaceCardDark)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    if (sisterTranscript.isNotBlank()) {
                        Text(
                            text = "You: \"$sisterTranscript\"",
                            color = TextCream,
                            fontSize = 13.5.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    if (brotherTranscript.isNotBlank()) {
                        Text(
                            text = "Brother: \"$brotherTranscript\"",
                            color = AccentAmber,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 18.sp
                        )
                    } else if (sisterTranscript.isBlank()) {
                        Text(
                            text = if (!hasAudioPermission)
                                "Tap 'Mic' to grant microphone access for speech recognition."
                            else "“Oye $sisterName! Start talking, I'm listening to your voice ♡”",
                            color = TextMuted,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Bottom Call Controls: Mute, Push-to-Talk, Voice Pitch, End Call
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mute button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = {
                                    isMuted = !isMuted
                                    if (isMuted) speechRecognizer?.stopListening()
                                },
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(if (isMuted) Color(0xFFDC2626) else SurfaceDark)
                                    .border(1.dp, SurfaceBorder, CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                                    contentDescription = "Mute",
                                    tint = TextCream
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(if (isMuted) "Unmute" else "Mute", color = TextMuted, fontSize = 11.sp)
                        }

                        // Push-to-Talk / Speak button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(AccentAmber)
                                    .clickable {
                                        if (!hasAudioPermission) {
                                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        } else {
                                            startListening()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Speak to Brother",
                                    tint = Color(0xFF261405),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Tap to Speak", color = AccentAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Voice Tone Pitch Toggle
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = { voicePitchElder = !voicePitchElder },
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceDark)
                                    .border(1.dp, SurfaceBorder, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Voice Pitch",
                                    tint = if (voicePitchElder) AccentAmber else Color(0xFF38BDF8)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(if (voicePitchElder) "Elder Voice" else "Young Voice", color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // End Call button
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallEnd,
                            contentDescription = "End Call",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    }
}
