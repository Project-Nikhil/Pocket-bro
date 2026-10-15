package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PocketBrotherColorScheme = darkColorScheme(
    primary = AccentAmber,
    onPrimary = Color(0xFF2A1806),
    primaryContainer = Color(0xFF4A341E),
    onPrimaryContainer = Color(0xFFFFDDB8),
    secondary = AccentGold,
    onSecondary = Color(0xFF331C07),
    secondaryContainer = Color(0xFF4E3722),
    onSecondaryContainer = Color(0xFFFFDEC4),
    tertiary = ModeComfortAccent,
    onTertiary = Color(0xFF13204A),
    background = BackgroundWarmDark,
    onBackground = TextCream,
    surface = SurfaceDark,
    onSurface = TextCream,
    surfaceVariant = SurfaceCardDark,
    onSurfaceVariant = TextMuted,
    outline = SurfaceBorder,
    outlineVariant = SurfaceBorderSubtle
)

@Composable
fun PocketBrotherTheme(
    darkTheme: Boolean = true, // Defaults to cozy dark theme as requested
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PocketBrotherColorScheme,
        typography = Typography,
        content = content
    )
}

// Retain alias for template compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    PocketBrotherTheme(content = content)
}
