package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Obsidian & Slate Professional Developer Palette (Linear / Raycast / JetBrains Dark)
val BackgroundDark = Color(0xFF090D14)         // Deep obsidian console background
val SurfaceDark = Color(0xFF0F1420)            // Level 1 workspace surface
val SurfaceCardDark = Color(0xFF151C2C)        // Level 2 cards and panels
val SurfaceCardElevated = Color(0xFF1E283E)    // Level 3 interactive / elevated items
val SurfaceBorder = Color(0xFF27344F)          // Crisp 1px engineering border
val SurfaceBorderSubtle = Color(0xFF1B2436)    // Subtle dividers

// Professional Text Scale
val TextPrimary = Color(0xFFF1F5F9)           // Slate 100 - crisp, high-readability text
val TextSecondary = Color(0xFFCBD5E1)         // Slate 300 - secondary labels
val TextMuted = Color(0xFF94A3B8)             // Slate 400 - descriptions and hints
val TextSubtle = Color(0xFF64748B)            // Slate 500 - micro-copy, timestamps, offsets
val TextCode = Color(0xFF38BDF8)              // Cyber Cyan for code and technical tags

// Developer Accents (Precision tokens)
val AccentCyan = Color(0xFF38BDF8)            // Primary tech action & LLM active
val AccentIndigo = Color(0xFF818CF8)          // Secondary developer brand accent
val AccentEmerald = Color(0xFF10B981)         // Online status, verified, 200 OK
val AccentAmber = Color(0xFFF59E0B)           // Telemetry warning, model download, RAM alert
val AccentRose = Color(0xFFF43F5E)            // Error, delete, kill, reset
val AccentPurple = Color(0xFFA855F7)          // Neural LLM, AI inference token

// Chat & Console Message Tokens
val ConsoleUserBubble = Color(0xFF17253D)      // User prompt container (Slate-Blue)
val ConsoleUserBorder = Color(0xFF2B4775)      // User prompt boundary
val ConsoleBrotherBubble = Color(0xFF111726)   // AI response container (Obsidian)
val ConsoleBrotherBorder = Color(0xFF202C45)   // AI response boundary
val CodeBlockBg = Color(0xFF0A0F1A)           // Inset terminal / code block

// Mode Accents (Developer style: Precision telemetry)
val ModeComfortBg = Color(0xFF121E33)
val ModeComfortBorder = Color(0xFF233B64)
val ModeComfortAccent = Color(0xFF60A5FA)

val ModeIrritativeBg = Color(0xFF26121B)
val ModeIrritativeBorder = Color(0xFF501C2B)
val ModeIrritativeAccent = Color(0xFFFB7185)

val ModeFunnyBg = Color(0xFF241C10)
val ModeFunnyBorder = Color(0xFF4C3714)
val ModeFunnyAccent = Color(0xFFFBBF24)

// Category Tags
val CategoryCollege = Color(0xFF38BDF8)
val CategoryExams = Color(0xFFF43F5E)
val CategoryPersonal = Color(0xFFA855F7)
val CategoryFamily = Color(0xFF10B981)
val CategoryStudy = Color(0xFFF59E0B)
val CategoryOther = Color(0xFF94A3B8)

// Backward compatibility aliases
val BackgroundWarmDark = BackgroundDark
val TextCream = TextPrimary
val AccentGold = AccentIndigo
val AccentPeach = AccentAmber
val AccentWarmGlow = Color(0x3338BDF8)
val WhatsAppGreenBubble = ConsoleUserBubble
val WhatsAppGreenBubbleLight = ConsoleUserBorder
val WhatsAppReceivedBubble = ConsoleBrotherBubble
val WhatsAppBlueTicks = AccentCyan
val WhatsAppChatBg = BackgroundDark
