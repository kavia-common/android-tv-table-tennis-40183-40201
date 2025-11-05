# Development Setup

## Prerequisites
- JDK 17
- Android Studio (Giraffe or newer)
- Android SDK with Android TV system image
- An Android TV emulator or a physical Android TV device

## Getting Started
1. Open the project at android-tv-table-tennis-40183-40201/ping_pong_frontend in Android Studio.
2. Let Gradle sync; ensure Kotlin and Android Gradle Plugin versions match the project settings.
3. Create an Android TV AVD (Android TV image) or connect a real TV device.
4. Build and run the app. The Leanback launcher icon should appear in the TV launcher.

## Build Configuration Highlights
- Java/Kotlin target: 17 (see app/build.gradle.kts).
- ViewBinding enabled for type-safe access to views.
- Leanback theme and TV features declared in AndroidManifest.xml.

## Run and Controls
- From the TV emulator/device:
  - DPAD UP/DOWN moves the player paddle.
  - DPAD CENTER/ENTER toggles pause.
  - MENU opens Settings; DPAD RIGHT opens Settings when paused.
  - BACK pauses if the game is running, or exits the overlay if already paused.

## Contribution Guide
- Branching: Use feature/<short-description> branches for changes.
- Commit messages: Prefer conventional messages (feat:, fix:, docs:, refactor:, chore:).
- Code style: Kotlin official style; keep rendering and input logic in GameView and orchestration in activities.
- UI changes: Keep TV focus behavior in mind; verify focus traversal on emulator.
- Testing: Add unit tests for settings and logic where possible. Visual/gameplay verification should be done on a TV emulator or hardware.

## Known Limitations and TODO
- No sound effects or haptics yet.
- No multiplayer; AI only.
- Retrofit/OkHttp/Gson/Media3/Glide are present for future extensions but not used.
- TODO:
  - Extract collision/physics helpers for better testability.
  - In-game quick settings overlay.
  - Local multiplayer and controller mapping.
  - Online multiplayer and backend services (matchmaking, profiles, stats).
  - Achievements and high-score persistence with cloud sync.

Sources:
- ping_pong_frontend/app/build.gradle.kts
- ping_pong_frontend/app/src/main/AndroidManifest.xml
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/MainActivity.kt
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/GameView.kt
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/SettingsActivity.kt
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/SettingsRepository.kt
