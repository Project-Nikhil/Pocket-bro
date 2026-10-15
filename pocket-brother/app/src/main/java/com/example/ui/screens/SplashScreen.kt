package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BrotherAvatar
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.BackgroundWarmDark
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.TextCream
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onLoaded: () -> Unit,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableFloatStateOf(0.05f) }
    var statusText by remember { mutableStateOf("Warming up Pocket Brother...") }
    var tipIndex by remember { mutableIntStateOf(0) }

    val tips = listOf(
        "💡 Tip: Need to focus? Ask brother to start a 25-minute Pomodoro timer.",
        "💡 Tip: Switch brother's mode to 'Irritative' when you want classic sibling banter 😂",
        "💡 Tip: Brother saves all your notes, exams, and voice memos 100% offline.",
        "💡 Tip: You can toggle between Elder and Younger Brother anytime in Settings ♡"
    )

    // Rotating tips
    LaunchedEffect(Unit) {
        while (true) {
            delay(1600)
            tipIndex = (tipIndex + 1) % tips.size
        }
    }

    // Genuine service initialization steps
    LaunchedEffect(Unit) {
        delay(300)
        progress = 0.25f
        statusText = "Loading local database & memories..."
        delay(350)
        progress = 0.55f
        statusText = "Initializing offline neural engine..."
        delay(350)
        progress = 0.85f
        statusText = "Checking device RAM & model capabilities..."
        delay(300)
        progress = 1.0f
        statusText = "Ready! Always here for you ♡"
        delay(400)
        onLoaded()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "halo")
    val haloScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "haloScale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWarmDark)
            .statusBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Glowing Avatar
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(130.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(125.dp)
                        .scale(haloScale)
                        .clip(CircleShape)
                        .background(AccentAmber.copy(alpha = 0.12f))
                )
                BrotherAvatar(size = 96.dp, showOnlineDot = true)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Pocket Brother",
                color = TextCream,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "your always-here kinda brother ♡",
                color = AccentAmber,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = AccentAmber,
                trackColor = SurfaceCardDark
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = statusText,
                color = TextMuted,
                fontSize = 12.5.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Rotating Tip Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceCardDark)
                    .border(0.8.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tips[tipIndex],
                    color = TextSubtle,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
