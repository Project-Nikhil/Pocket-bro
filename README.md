# Pocket Brother ♡
> *"a little piece of home, whenever you need me ♡"*

An offline AI brother companion and daily life toolkit designed natively for Android with **Jetpack Compose** and **Room SQLite**.

[![Android](https://img.shields.io/badge/Platform-Android_7.0+-3DDC84?style=flat&logo=android&logoColor=white)](https://android.com)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Offline Core](https://img.shields.io/badge/AI_Engine-100%25_Offline-F5C58A?style=flat)](#architecture)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 🌟 Overview

**Pocket Brother** was created to be a warm, gentle, and reliable everyday companion for sisters. Beyond conversational support, it serves as a full daily life hub with study timers, reminders, calendar planning, offline diary, and memories.

### 💖 Key Features

* **3 Distinct Chat Modes**:
  * 🫂 **Comfort Mode**: Gentle, empathetic validation when stressed, sad, or overwhelmed.
  * 😂 **Funny Banter Mode**: Lighthearted sibling jokes, teasing, and everyday laughter.
  * 😈 **Playful Irritation Mode**: Classic brotherly banter and dramatic humor.
* **100% Offline Core Engine**: Instant conversational responses without requiring an internet connection or remote servers.
* **Gemma On-Device LLM Support**: Designed for on-device quantized Gemma models with RAM tier detection (Nano 2GB, Balanced 4GB, Pro 8GB).
* **Private Daily Toolkit**:
  * 📅 **Calendar & Schedule Planner**: College classes, exams, and brother talk slots.
  * 🔔 **Sister Reminders**: Drinking water, taking meds, revision, and calling home.
  * ⏱️ **Study Pomodoro Timer**: 25-minute focus intervals with customized breaks.
  * 📖 **Offline Diary & Notes**: Pinned reflections, thoughts, and mood tracking.
  * ✨ **Memories Sanctuary**: Encouraging quotes and notes from home.

---

## 🔒 Privacy & Architecture

* **100% On-Device SQLite**: Chat history, diary notes, and schedules are stored purely on your phone in an isolated Room SQLite database (`pocket_brother_db`).
* **Zero Telemetry**: No tracking, no user profiling, no account creation, no analytics.
* **Direct Sideloading**: Distributed directly as an open APK package outside walled-garden stores.

---

## 📁 Repository Structure

```
pocket-bro/
├── pocket-brother/           # 📱 Android Application (Kotlin + Jetpack Compose)
│   ├── app/                  # Main Android application module
│   │   ├── src/main/java/    # UI screens, ViewModels, Room DB, Offline Engine
│   │   └── build.gradle.kts  # Dependencies, SDK configurations
│   ├── gradle/               # Gradle wrapper configuration
│   └── build.gradle.kts      # Root Android build configuration
│
└── pocket-brother-web/       # 🌐 Landing & Direct APK Download Website (React + Vite)
    ├── src/                  # Modern mobile-first UI components & design system
    ├── public/               # Public assets & icons
    └── index.html            # Responsive HTML template
```

---

## 🚀 Getting Started

### Building the Android App

**Prerequisites:**
* Android SDK (API level 24 Nougat up to API level 36)
* JDK 17+ (JDK 21 or 24 recommended)

```bash
# Clone the repository
git clone https://github.com/Project-Nikhil/Pocket-bro.git
cd Pocket-bro/pocket-brother

# Build debug APK via Gradle wrapper
./gradlew assembleDebug
```

The compiled APK will be generated at:
`pocket-brother/app/build/outputs/apk/debug/app-debug.apk`

---

### Running the Download Website Locally

**Prerequisites:**
* Node.js 18+ and npm

```bash
cd Pocket-bro/pocket-brother-web

# Install dependencies
npm install

# Start local dev server
npm run dev
```

---

## 📄 License

This project is open-source under the [MIT License](LICENSE).
Built with love for sisters everywhere.
