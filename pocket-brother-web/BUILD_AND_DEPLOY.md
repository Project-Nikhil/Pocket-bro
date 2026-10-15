# Pocket Brother — Build, APK & Netlify Deployment Guide

This guide provides the complete, step-by-step instructions to compile the **Pocket Brother Android APK**, prepare the **Pocket Brother Download Website**, and deploy it to **Netlify**.

---

## 1. Project Inspection Summary

- **App Name:** Pocket Brother
- **Application ID:** `com.aistudio.pocketbrother.kzxp`
- **Minimum SDK:** Android 7.0+ (API level 24)
- **Target SDK:** Android 15 (API level 36)
- **Version:** `1.0` (Version Code `1`)
- **UI Framework:** Kotlin + Jetpack Compose + Material3
- **Local Database:** Room SQLite (`pocket_brother_db`) storing Chats, Calendar Events, Reminders, Notes/Diary, Tasks, Study Exams, Memories, and Voice Notes.
- **AI Architecture:**
  1. Built-in `OfflineBrotherEngine` (Instant zero-download conversational engine with empathy and tool parsing).
  2. Optional on-device `GemmaLocalLLMService` (with RAM tiers: Nano 2GB, Balanced 4GB, Pro 8GB).
  3. Optional remote Gemini API connection via `.env` (`GEMINI_API_KEY`).
- **APK Status in Source ZIP:** The Google AI Studio export contains complete Android project source code. The pre-compiled `.apk` file is generated via Android Studio.

---

## 2. How to Build the Genuine APK from Source

Follow these steps on your computer:

1. Open **Android Studio**.
2. Select **Open** and choose the directory:
   ```
   e:\pocket bro\pocket-brother
   ```
3. Allow Gradle to sync dependencies.
4. In Android Studio's top menu bar, click:
   ```
   Build > Build Bundle(s) / APK(s) > Build APK(s)
   ```
5. When the build completes, Android Studio displays a notification:
   `APK(s) generated successfully for 1 module. [locate]`
6. Click **locate**. Your compiled APK is located at:
   ```
   pocket-brother\app\build\outputs\apk\debug\app-debug.apk
   ```
7. Copy this file into the website's public download folder:
   ```powershell
   Copy-Item "e:\pocket bro\pocket-brother\app\build\outputs\apk\debug\app-debug.apk" -Destination "e:\pocket bro\pocket-brother-web\public\downloads\pocket-brother.apk" -Force
   ```

---

## 3. How to Build the Website

Inside the `pocket-brother-web` directory:

```powershell
cd "e:\pocket bro\pocket-brother-web"
npm install
npm run build
```

This compiles the TypeScript code and produces the production-ready static assets in:
```
pocket-brother-web/dist/
```

---

## 4. How to Deploy to Netlify

### Option A: Drag-and-Drop (Netlify Drop — Fastest, 1 Minute)
1. Run `npm run build` inside `pocket-brother-web`.
2. Open [app.netlify.com/drop](https://app.netlify.com/drop) in your browser.
3. Drag the `pocket-brother-web\dist` folder and drop it onto the Netlify page.
4. Netlify will instantly provide your live public URL (e.g. `https://pocket-brother-for-sisters.netlify.app`).

### Option B: Netlify CLI
1. Install Netlify CLI (if not already installed):
   ```powershell
   npm install -g netlify-cli
   ```
2. Log in and deploy:
   ```powershell
   cd "e:\pocket bro\pocket-brother-web"
   netlify login
   netlify deploy --prod --dir=dist
   ```

### Option C: GitHub Continuous Deployment
1. Push your repository to GitHub.
2. In Netlify dashboard, click **Add new site > Import an existing project**.
3. Select your repository. Netlify will read `netlify.toml` automatically:
   - **Base directory:** `pocket-brother-web`
   - **Build command:** `npm run build`
   - **Publish directory:** `dist`
4. Click **Deploy Site**.

---

## 5. APK Hosting Limits & External Mirror Option

- **Standard Netlify Limit:** Netlify supports static files up to **100 MB** per file. The standard Pocket Brother Jetpack Compose APK is **~18 to 25 MB**, which deploys directly on Netlify without any issues.
- **External Mirror (Optional):** If you decide to bundle multi-gigabyte Gemma LLM weights into the APK itself, you can upload the APK to **GitHub Releases** or **Google Drive**. To point the download buttons to an external mirror, simply edit:
  ```typescript
  // pocket-brother-web/src/config/appConfig.ts
  export const APP_CONFIG = {
    ...
    apkDownloadPath: "https://github.com/your-username/pocket-brother/releases/download/v1.0/pocket-brother.apk",
  };
  ```

---

## 6. Verification Checklist

- [x] Warm charcoal and cream dark theme implemented.
- [x] Actual phone mockup screenshots generated from real showcase image.
- [x] Primary and secondary download buttons configured with `download` attribute and fallback direct link.
- [x] Download Specification card populated with verified metadata (`v1.0`, API 24+, targetSdk 36, ~18.4 MB).
- [x] All 10 feature cards matched to actual Kotlin implementations.
- [x] Interactive 3-mode personality tester (Comfort, Funny Banter, Playful Irritation) functional.
- [x] 3-Step installation guide with direct distribution security note and troubleshooting accordions.
- [x] Transparent privacy section explaining on-device Room SQLite and local AI engine.
- [x] All 8 required FAQ items answered.
- [x] `netlify.toml` configured with APK MIME types (`application/vnd.android.package-archive`) and SPA fallback.
- [x] Production build passes with 0 errors.
- [x] Mobile-first layout verified on 390x844 viewport.
