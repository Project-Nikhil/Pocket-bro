/**
 * Pocket Brother Application Configuration
 * All APK paths, build metadata, and distribution links are configured here.
 */

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

export const APP_CONFIG: AppConfig = {
  appName: "Pocket Brother",
  tagline: "a little piece of home, whenever you need me ♡",
  headline: "your brother, always a message away",
  description: "A little companion for your everyday life — made with love, just for you",
  subDescription: "An offline AI brother companion and daily life toolkit with chat, calendar, reminders, notes, timer, study planner, and memories.",
  version: "0.0.1",
  versionCode: 1,
  packageId: "com.aistudio.pocketbrother.kzxp",
  minSdk: "Android 7.0+ (API 24)",
  targetSdk: "Android 15 (API 36)",
  buildType: "Debug / Direct APK (Release Ready)",
  distributionType: "Direct APK (Outside Google Play)",
  apkFileName: "pocket-brother.apk",
  // Primary download path deployed with Netlify
  apkDownloadPath: "./downloads/pocket-brother.apk",
  // Optional external mirror (e.g. GitHub Releases, Google Drive, AWS S3)
  externalMirrorUrl: "",
  apkEstimatedSize: "21.9 MB",
  // The genuine APK binary is compiled and present in public/downloads/pocket-brother.apk
  isApkReady: true,
  builtFromRealSource: true,
};
