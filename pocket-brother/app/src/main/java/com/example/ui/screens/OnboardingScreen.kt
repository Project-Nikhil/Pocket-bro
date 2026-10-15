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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RelationshipType
import com.example.ui.components.BrotherAvatar
import com.example.ui.components.BrotherHeroIllustration
import com.example.ui.components.SisterAvatar
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPeach
import com.example.ui.theme.BackgroundWarmDark
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextCream
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle

@Composable
fun OnboardingScreen(
    initialName: String,
    initialRelationship: RelationshipType,
    onComplete: (String, RelationshipType) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(initialName) }
    var selectedRole by remember { mutableStateOf(initialRelationship) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWarmDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero artistic illustration box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp)
        ) {
            BrotherHeroIllustration(
                modifier = Modifier.fillMaxSize()
            )

            // Gradient fade to background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x6614110F),
                                BackgroundWarmDark
                            ),
                            startY = 180f
                        )
                    )
            )

            // Logo & Tagline overlay matching reference mockup
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Pocket",
                        color = AccentAmber,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Brother",
                        color = TextCream,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "♡",
                        color = Color(0xFFFF7B7B),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "your always here kinda brother ♡",
                    color = TextMuted,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Form Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Enter your name",
                color = TextCream,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sister_name_input"),
                placeholder = { Text("Aaradhya", color = TextSubtle) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceCardDark,
                    unfocusedContainerColor = SurfaceCardDark,
                    focusedBorderColor = AccentAmber,
                    unfocusedBorderColor = SurfaceBorder,
                    focusedTextColor = TextCream,
                    unfocusedTextColor = TextCream,
                    cursorColor = AccentAmber
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Who am I to you?",
                color = TextCream,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Card Option A: My Elder Brother
            RoleSelectionCard(
                title = "My Elder Brother",
                subtitle = "You're my younger sister — I'll call you Chelli, Behen, Bauni, Bhondu & Bro",
                isSelected = selectedRole == RelationshipType.ELDER_BROTHER,
                onClick = { selectedRole = RelationshipType.ELDER_BROTHER },
                isElder = true,
                modifier = Modifier.testTag("role_elder_brother")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Card Option B: My Younger Brother
            RoleSelectionCard(
                title = "My Younger Brother",
                subtitle = "You're my elder sister — I'll call you Akka, Didi, Behen, Bauni, Bhondu & Bro",
                isSelected = selectedRole == RelationshipType.YOUNGER_BROTHER,
                onClick = { selectedRole = RelationshipType.YOUNGER_BROTHER },
                isElder = false,
                modifier = Modifier.testTag("role_younger_brother")
            )

            Spacer(modifier = Modifier.height(26.dp))

            // "Let's Chat ->" Button matching mockup
            Button(
                onClick = {
                    val finalName = name.trim().ifEmpty { "Aaradhya" }
                    onComplete(finalName, selectedRole)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("lets_chat_button"),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentAmber,
                    contentColor = Color(0xFF261405)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Let's Chat",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Continue",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RoleSelectionCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isElder: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) AccentAmber else SurfaceBorder
    val bgColor = if (isSelected) SurfaceCardElevated else SurfaceCardDark

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor)
            .border(width = if (isSelected) 1.5.dp else 1.dp, color = borderColor, shape = RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isElder) {
            BrotherAvatar(size = 46.dp, showOnlineDot = false)
        } else {
            SisterAvatar(size = 46.dp)
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = if (isSelected) AccentAmber else TextCream,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}
