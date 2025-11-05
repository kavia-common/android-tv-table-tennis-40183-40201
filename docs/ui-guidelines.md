# UI and UX Guidelines

## Design Principles
The UI follows a modern, minimalist aesthetic using the Ocean Professional palette. The presentation is optimized for large screens and TV viewing distance. Surfaces use rounded corners and subtle shadows to create depth without visual noise. Interactions are clear and discoverable using focused states and well-placed hints.

## Color Tokens (Ocean Professional)
- primary: #2563EB
- secondary: #F59E0B
- success: #F59E0B
- error: #EF4444
- background: #f9fafb
- surface: #ffffff
- textPrimary: #111827
- textSecondary: #475569
- shadow: #14000000

These align with res/values/colors.xml and are applied across surfaces, buttons, and typography for consistency and accessibility.

## Layout and Composition
- Main screen structure:
  - Top: A surface bar displaying the score and quick actions (Pause, Settings).
  - Center: The GameView within a bordered, rounded “table” surface (bg_table) that includes a subtle gradient and center line.
  - Bottom: A surface bar for hints and control guidance.
  - Overlay: A semi-transparent dim background with a centered surface card for pause, resume, and settings.
- Spacing and sizing:
  - Generous margins and paddings to suit viewing distance and overscan.
  - Buttons at least 48dp high for focusable television interaction.
  - Text sizes favor legibility: Score ~28sp, labels 18–20sp.

## Focus and Navigation (TV patterns)
- D-pad navigation:
  - UP/DOWN moves the player’s paddle during gameplay.
  - CENTER/ENTER toggles the pause overlay.
  - MENU opens Settings. When paused, DPAD RIGHT also opens Settings.
- Focus states:
  - Focusable UI controls use pill-shaped backgrounds with primary/secondary emphasis.
  - bg_button_primary and bg_button_secondary apply distinct colors on focus.
  - Pause overlay captures focus and blocks gameplay until dismissed.

## Typography and Surfaces
- Text:
  - Use textPrimary for high contrast on light surfaces.
  - Use textSecondary for secondary information like hints.
- Surfaces:
  - bg_surface_round and bg_surface_round_light create layered, raised cards with subtle offsets and shadows.
  - The game “table” draws a gradient and border for depth, plus a center line for classic Pong flavor.

## Retro Theme Mapping to Ocean Professional
- Retro direction:
  - Keep elements geometric and minimal: round paddles, clean borders, and a flat net line.
  - Use the secondary accent for dynamic highlights (e.g., ball color when moving right).
- Modern polish:
  - Rounded corners and soft shadows.
  - Smooth transitions are implicit in user interactions; the game loop provides continuous motion without heavy animations.

## Accessibility
- Contrast:
  - primary and textPrimary meet legibility expectations on surface/background colors.
- Size:
  - Larger click targets and ample spacing accommodate remote controls and viewing distance.
- Feedback:
  - Clear state changes (Pause overlay alpha 0 → 1).
  - Ball color accents provide contextual movement feedback without distracting effects.

## Implementation Pointers
- Theme:
  - styles.xml defines AppTheme inheriting Leanback with color tokens mapped appropriately.
  - TvPillButton and TvPillButton.Secondary centralize button styling.
- Layout:
  - activity_main.xml composes all major surfaces and the game view.
  - activity_settings.xml provides a centered settings card, with Spinners for Difficulty and Speed.
- Game rendering:
  - GameView handles drawing paddles, ball, and net. It uses primary/secondary colors to enhance motion feedback.

Sources:
- ping_pong_frontend/app/src/main/res/values/colors.xml
- ping_pong_frontend/app/src/main/res/values/styles.xml
- ping_pong_frontend/app/src/main/res/layout/activity_main.xml
- ping_pong_frontend/app/src/main/res/layout/activity_settings.xml
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/GameView.kt
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/MainActivity.kt
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/SettingsActivity.kt
