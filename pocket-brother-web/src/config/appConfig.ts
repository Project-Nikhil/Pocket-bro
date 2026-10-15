/**
 * Pocket Brother Application Configuration
 * All APK paths, build metadata, and distribution links are configured here.
 */

export interface VersionFix {
  title: string;
  category: 'Feature' | 'Fix' | 'Improvement' | 'UI/UX';
  description: string;
  detail?: string;
}

export interface AppRelease {
  version: string;
  versionCode: number;
  releaseDate: string;
  apkFileName: string;
  apkDownloadPath: string;
  apkSize: string;
  isLatest: boolean;
  statusBadge: string;
  headline: string;
  summary: string;
  highlights: string[];
  fixes: VersionFix[];
}

export interface AppConfig {
  appName: string;
  tagline: string;
  headline: string;
  description: string;
  subDescription: string;
  version: string;
  versionCode: number;
  packageId: string;
  minSdk: string;
  targetSdk: string;
  buildType: string;
  distributionType: string;
  apkFileName: string;
  apkDownloadPath: string;
  externalMirrorUrl?: string;
  apkEstimatedSize: string;
  isApkReady: boolean;
  builtFromRealSource: boolean;
}

export const APP_RELEASES: AppRelease[] = [
  {
    version: "0.0.2",
    versionCode: 2,
    releaseDate: "October 2026",
    apkFileName: "pocket-brother-v0.0.2.apk",
    apkDownloadPath: "./downloads/pocket-brother-v0.0.2.apk",
    apkSize: "22.1 MB",
    isLatest: true,
    statusBadge: "Latest Release ✨",
    headline: "Gemma AI Model Chooser, Attachments, Voice Call & Keyboard Fixes",
    summary: "Major update introducing in-chat Gemma AI model switching with real-time RAM diagnostics, chat attachments, full emoji picker, interactive voice calls, and keyboard layout fixes.",
    highlights: [
      "✨ In-Chat Gemma AI Selector: 3-dots menu & top header status badge with real-time RAM telemetry",
      "⚡ Dual Engine: Switch instantly between Gemma Local LLMs (Nano, Balanced, Pro, Extreme) and Instant Offline Core",
      "⌨️ Chat Keyboard Fix: Seamless adjustResize handling; input bar stays flush with keyboard without black gaps",
      "🫂 AI Mood Selection Fix: Single-query pipeline per message; in-flight lock prevents duplicate sends",
      "😀 Real Emoji Picker: 7 categorized tabs, live search filtering, and cursor-aware insertion",
      "📎 Chat Attachments: Photos, PDFs, Word docs, text files, and audio recordings with preview banners",
      "📞 Voice-Call Pipeline: Real-time offline speech-to-text, brother intent engine, and vocal TTS playback",
      "🔙 Stack-based Back Navigation: Proper back-stack handling across modals and sheets",
      "🚀 Cozy Splash Screen: Dark amber theme, rotating sibling tips, and genuine initialization checks",
      "🛠️ Upgraded Tools: Weather city selector, voice note recording timer, 8 avatar presets, theme switcher, and SQLite storage breakdown",
    ],
    fixes: [
      {
        title: "Direct AI Model Selector in 3-Dots Menu & Chat Header",
        category: "Feature",
        description: "Choose AI Model dialog with device RAM telemetry (Total RAM, Available RAM, and device recommendations). Select from Gemma 2B Nano (Q4_K_M, ~1.2GB), Balanced (Q5_K_M, ~1.6GB), Pro (Q8_0, ~2.3GB), or Gemma 7B Extreme (Q4_K_S, ~4.1GB). Includes local weight importing (.bin/.gguf) and instant toggle back to Instant Core.",
      },
      {
        title: "Chat Keyboard Layout & Window Insets Fix",
        category: "Fix",
        description: "Resolved issue where screen background shrank away from window bounds, creating an empty black gap above the keyboard. Added adjustResize to AndroidManifest and pinned bottom composer flush above IME insets.",
      },
      {
        title: "AI Mood Selection & In-Flight Lock",
        category: "Fix",
        description: "Resolved redundant prompt pipeline executing across all 3 personality modes simultaneously. Optimized to query only the active mode (Comfort, Irritative, or Funny) and added submission lock to stop duplicate sends on rapid tapping.",
      },
      {
        title: "Full Categorized Emoji Picker",
        category: "Feature",
        description: "Added 7 categorized tabs (Recent, Smileys, People, Hearts, Food, Animals, Activities) with search filtering and cursor-aware insertion so emojis don't overwrite existing text.",
      },
      {
        title: "Chat Attachments Pipeline",
        category: "Feature",
        description: "Extended Room schema with attachment metadata (URI, type, name, size). Integrated Photo Picker and GetContent for PDFs, DOCX, text files, and audio recordings with pre-send preview banners and custom chat badges.",
      },
      {
        title: "Offline Voice-Call Pipeline",
        category: "Feature",
        description: "Interactive voice call dialog with speech-to-text (SpeechRecognizer), offline brother intent engine responses, text-to-speech (TTS) vocal playback, elapsed duration timer, and live transcription bubbles.",
      },
      {
        title: "Stack-Based Back Navigation",
        category: "Improvement",
        description: "Replaced hardcoded home navigation with stack-based back navigation. Closes emoji pickers, pending attachments, or dialogs before popping screen backstack.",
      },
      {
        title: "Cozy Branded Splash Screen",
        category: "UI/UX",
        description: "App launch screen featuring the amber glowing icon, rotating warm tips, database integrity check, offline neural engine check, and hardware RAM diagnostics before advancing.",
      },
      {
        title: "Tools, Settings & Theme Expansion",
        category: "Improvement",
        description: "Manual city selection with quick chips for Indian and global cities, voice notes timer/import, 8 sister avatar presets, Dark/Light/System theme toggles, and live SQLite database storage calculator with text backup export.",
      },
    ],
  },
  {
    version: "0.0.1",
    versionCode: 1,
    releaseDate: "October 2026",
    apkFileName: "pocket-brother-v0.0.1.apk",
    apkDownloadPath: "./downloads/pocket-brother-v0.0.1.apk",
    apkSize: "21.9 MB",
    isLatest: false,
    statusBadge: "Previous Stable",
    headline: "Initial Release: 100% Offline Sister Companion Hub",
    summary: "The debut release of Pocket Brother featuring offline empathetic chat, 3 personality modes, study timer, reminders, calendar, and notes.",
    highlights: [
      "💬 Brother Chat with 3 Personality Modes: Comfort 🫂, Funny Banter 😂, and Playful Irritation 😈",
      "⚡ 100% Offline Core Engine: Instant brotherly replies with zero internet or cloud servers required",
      "⏱️ Study Pomodoro Timer: 25-minute focus intervals with short & long break chimes",
      "🔔 Daily Reminders: Water, medicine, study revision, and calling home",
      "📅 Sibling Calendar & Notes: Schedule planning and private offline diary with mood tags",
      "🔒 100% On-Device Room SQLite Database: All personal data stays safely on the phone",
    ],
    fixes: [
      {
        title: "First Standalone APK Build",
        category: "Feature",
        description: "Initial standalone Android package distributed directly for sideloading on Android 7.0 (API 24) through Android 15 (API 36).",
      },
      {
        title: "Room SQLite Database Architecture",
        category: "Feature",
        description: "Local database initialization for chat messages, notes, calendar events, and sister profile preferences.",
      },
    ],
  },
];

export const APP_CONFIG: AppConfig = {
  appName: "Pocket Brother",
  tagline: "a little piece of home, whenever you need me ♡",
  headline: "your brother, always a message away",
  description: "A little companion for your everyday life — made with love, just for you",
  subDescription: "An offline AI brother companion and daily life toolkit with chat, calendar, reminders, notes, timer, study planner, and memories.",
  version: "0.0.2",
  versionCode: 2,
  packageId: "com.aistudio.pocketbrother.kzxp",
  minSdk: "Android 7.0+ (API 24)",
  targetSdk: "Android 15 (API 36)",
  buildType: "Debug / Direct APK (Release Ready)",
  distributionType: "Direct APK (Outside Google Play)",
  apkFileName: "pocket-brother-v0.0.2.apk",
  apkDownloadPath: "./downloads/pocket-brother-v0.0.2.apk",
  externalMirrorUrl: "",
  apkEstimatedSize: "22.1 MB",
  isApkReady: true,
  builtFromRealSource: true,
};
