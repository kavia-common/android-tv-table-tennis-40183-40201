# Ping Pong for Android TV

## Overview
Ping Pong for Android TV is an interactive, lightweight table tennis experience designed specifically for the living room. The app focuses on simple, responsive gameplay with Android TV remote D-pad controls, legible UI on large screens, and TV-first navigation patterns. The visual style applies a modern, minimal design with the Ocean Professional palette for color and emphasis, while honoring a retro arcade feel in the game view.

- Target platform: Android TV (Leanback)
- Tech stack: Kotlin, Android View system, Leanback theme, ConstraintLayout, Coroutines
- Primary features:
  - D-pad controlled Pong gameplay
  - Simple AI opponent
  - Adjustable difficulty and speed in Settings
  - Pause/resume overlay and on-screen hints

## Architecture
The app is organized as a single Android application module with the following core components:

- MainActivity: The TV entry point that hosts the game surface, top score bar, bottom hints bar, and pause overlay. It handles D-pad input and navigation to Settings.
- GameView: A custom View implementing the Pong game loop, physics, collisions, and drawing. It exposes simple methods for pause/resume, paddle movement, and settings application.
- SettingsActivity: A TV-friendly settings screen that allows the player to configure Difficulty (EASY, MEDIUM, HARD) and Speed (SLOW, NORMAL, FAST).
- SettingsRepository: A small repository that persists GameSettings using SharedPreferences.

High-level data flow:
- MainActivity receives key events and delegates movement and pause actions to GameView.
- GameView runs a coroutine-based loop (~60 FPS), updates physics, and draws the scene. It notifies MainActivity of score and pause state changes via callbacks.
- SettingsActivity reads/writes GameSettings through SettingsRepository. When returning to MainActivity, settings are applied to GameView.

## Containers and Components
- Container: ping_pong_frontend (Android TV app)
  - Framework: Android TV (Leanback)
  - Responsibilities: Game rendering and input, score display, settings UI, and theme application.
  - Public interfaces: Android TV app UI

Key files:
- app/src/main/AndroidManifest.xml: Declares Leanback launcher category, activities, permissions, banner, and TV requirements.
- app/src/main/java/com/example/ping_pong_frontend/MainActivity.kt: Hosts the game and handles DPAD/menu actions.
- app/src/main/java/com/example/ping_pong_frontend/GameView.kt: Implements game loop, physics, collisions, AI paddle, and drawing.
- app/src/main/java/com/example/ping_pong_frontend/SettingsActivity.kt: Shows TV-friendly settings screen.
- app/src/main/java/com/example/ping_pong_frontend/SettingsRepository.kt: Persists Difficulty and Speed using SharedPreferences.
- app/src/main/res/layout/activity_main.xml: Main screen layout (score bar, game view, hints bar, pause overlay).
- app/src/main/res/layout/activity_settings.xml: Settings card layout with Spinners and buttons.
- app/src/main/res/values/colors.xml and styles.xml: Ocean Professional palette and theme; TV pill buttons and Leanback theme integration.

## UI and UX Guidelines (Android TV)
- Navigation and focus:
  - Rely on D-pad for primary navigation. The on-screen buttons are focusable with clear focus states.
  - DPAD UP/DOWN moves the left paddle during gameplay.
  - DPAD CENTER/ENTER toggles pause.
  - MENU or DPAD RIGHT (when paused) opens Settings.
- Legibility for large screens:
  - Use larger text sizes and high contrast. Score is displayed at the top in a prominent surface with rounded corners and soft shadows.
  - Bottom hints bar communicates controls and is always legible with textSecondary color.
- Focus and states:
  - Buttons use pill-shaped backgrounds with distinct focused state colors (primary/secondary).
  - Pause overlay dims the game area and shows a centered card with clear actions.
- Theme and palette (Ocean Professional):
  - Primary: #2563EB
  - Secondary/Success: #F59E0B
  - Error: #EF4444
  - Background: #f9fafb
  - Surface: #ffffff
  - Text Primary: #111827
  - Text Secondary: #475569
- Retro theme direction mapped to Ocean Professional:
  - Retro arcade flavor is conveyed through the minimalist paddles, a visible net line, and a playfield “table” surface with subtle gradient and border.
  - Accent color shifts of the ball (primary/secondary) impart retro feedback without noisy textures, keeping the overall look modern and clean.
  - Rounded corners and shadows on surfaces align with the modern aesthetic while still echoing retro cabinet styling.

## Development Setup
Prerequisites:
- JDK 17
- Android Studio (Giraffe+)
- Android SDK and TV emulator or TV device

Project structure:
- android-tv-table-tennis-40183-40201/
  - ping_pong_frontend/ (Android app module)
    - app/ (source)
    - build.gradle.kts (module)
    - settings.gradle.kts, build.gradle.kts (project)

Build and run:
1. Open the project folder android-tv-table-tennis-40183-40201/ping_pong_frontend in Android Studio.
2. Ensure the TV emulator is created (Android TV image) or connect an Android TV device.
3. Sync Gradle and run the app.

Gradle notes:
- Java/Kotlin targets are configured for JDK 17.
- ViewBinding is enabled for generated binding classes.

## Roadmap
Short-term:
- In-game overlay for changing difficulty/speed without leaving the table.
- Add sound effects and basic vibration feedback (where available).
- Persist and display high scores.

Medium-term:
- Local multiplayer (two controllers or split control mapping).
- Additional game modes (e.g., “Endless rally” or “First to N points”).
- Add a tutorial/onboarding flow.

Long-term:
- Online multiplayer (matchmaking and latency compensation).
- Profile and achievements integration.
- Theming options beyond Ocean Professional while retaining accessibility and legibility.

## License
This project is provided as-is for demonstration and educational purposes.

Sources: 
- ping_pong_frontend/app/src/main/AndroidManifest.xml
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/MainActivity.kt
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/GameView.kt
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/SettingsActivity.kt
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/SettingsRepository.kt
- ping_pong_frontend/app/src/main/res/layout/activity_main.xml
- ping_pong_frontend/app/src/main/res/layout/activity_settings.xml
- ping_pong_frontend/app/src/main/res/values/colors.xml
- ping_pong_frontend/app/src/main/res/values/styles.xml
- ping_pong_frontend/app/build.gradle.kts
