# Pocket Brother APK Directory

Place the compiled Android APK file in this directory with the exact filename:
`pocket-brother.apk`

When deployed to Netlify, this file will be publicly accessible at:
`https://<your-netlify-domain>.netlify.app/downloads/pocket-brother.apk`

---

## How to Build the Genuine APK in Android Studio

1. Open **Android Studio**.
2. Click **Open** and select the Android project folder:
   `e:\pocket bro\pocket-brother`
3. Wait for Gradle sync to complete. If prompted to fix Kotlin/Compose plugin versions, accept the recommendations.
4. (Optional) In `app/build.gradle.kts`, if you encounter signing errors, ensure you build the Debug variant:
   `Build > Select Build Variant > debug`
5. Click **Build** menu at the top -> **Build Bundle(s) / APK(s)** -> **Build APK(s)**.
6. Once the build finishes, Android Studio will show a popup:
   `APK(s) generated successfully for 1 module. [locate]`
7. Click **locate**. The generated file is located at:
   `e:\pocket bro\pocket-brother\app\build\outputs\apk\debug\app-debug.apk`
8. Copy that `app-debug.apk` into this folder and rename it:
   `pocket-brother.apk`
9. Run `npm run build` in `pocket-brother-web` and deploy to Netlify!

---

## Netlify Hosting Limit Note
- Netlify allows static files up to **100 MB** per file on standard deployments.
- The standard Pocket Brother Jetpack Compose APK is approximately **18 to 25 MB** (without large local weights), which is well within Netlify's 100 MB limit.
- If you bundle large multi-gigabyte Gemma model weights inside the APK itself, consider hosting the APK on **GitHub Releases** or **Google Drive**, and updating `externalMirrorUrl` in `src/config/appConfig.ts`.
