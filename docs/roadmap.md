# Roadmap

## Vision
Deliver a delightful, performant Android TV Pong experience with simple controls, crisp visuals, and scalable architecture that can grow into multiplayer and online features.

## Phases

### Phase 1: Polish the Single-Player Core
- Add SFX and optional background music with volume settings.
- Improve pause overlay with more descriptive options (Restart, Settings, Exit).
- Persist high scores and session stats.
- Extract physics utilities for test coverage and deterministic simulations.

### Phase 2: Local Multiplayer
- Dual-controller support with player assignment and ready checks.
- Split-controls fallback (e.g., alternate DPAD for player two) where feasible.
- Add game modes: First to N points, timed mode, endless rally with speed ramp.

### Phase 3: Online Features
- Backend for matchmaking and leaderboards (selection and documentation of protocols).
- Networked multiplayer with latency compensation (client-side prediction, reconciliation).
- Player profiles, achievements, and cloud-synced stats.

### Phase 4: Theming and Accessibility
- Theme packs beyond Ocean Professional while preserving contrast and legibility.
- Colorblind-friendly palettes and configurable UI size.
- Regionalization and string externalization pass for full localization.

### Phase 5: Media and Content Expansion
- Replays and highlight capture.
- Tutorial and skill challenges.
- Seasonal events or tournament modes.

## Risks and Mitigations
- TV input fragmentation: Test on multiple emulators and at least one hardware device.
- Network latency: Prototype lag compensation strategies early.
- Performance regressions: Keep rendering simple; profile frame times regularly.

## Success Metrics
- Stable 60 FPS on standard Android TV hardware.
- Session length and retention improvements.
- Error-free onboarding and clear control discoverability.
- Positive accessibility audits (contrast, focus traversal, legibility).

Sources:
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/MainActivity.kt
- ping_pong_frontend/app/src/main/java/com/example/ping_pong_frontend/GameView.kt
- ping_pong_frontend/app/src/main/res/layout/activity_main.xml
- ping_pong_frontend/app/build.gradle.kts
