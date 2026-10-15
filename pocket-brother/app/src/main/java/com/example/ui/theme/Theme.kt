package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PocketBrotherColorScheme = darkColorScheme(
    primary = AccentCyan,
    onPrimary = Color(0xFF031526),
    primaryContainer = Color(0xFF0F2942),
    onPrimaryContainer = Color(0xFFBAE6FD),
    secondary = AccentIndigo,
    onSecondary = Color(0xFF131438),
    secondaryContainer = Color(0xFF262C54),
    onSecondaryContainer = Color(0xFFE0E7FF),
    tertiary = AccentEmerald,
    onTertiary = Color(0xFF022315),
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
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
