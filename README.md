# SeedlessDS

A Nintendo DS emulator for Android handhelds with physical controls, including devices with two displays.

SeedlessDS is the smaller sibling of WatermelonDS: the same watermelon idea, with the seeds taken out.

<p align="center">
<img width="350" height="300" alt="SeedlessDS library" src="https://github.com/user-attachments/assets/9e9fbcee-28d5-4938-8b81-bbf5eb30fc36" />
<img width="350" height="300" alt="SeedlessDS interface" src="https://github.com/user-attachments/assets/74c48268-3037-4c81-9abe-400f0b790b9f" />
<img width="350" height="300" alt="SeedlessDS settings" src="https://github.com/user-attachments/assets/b23fb273-9a10-4244-b2b5-f05003c48c4d" />
</p>

> **Current status:** `1.0.0`. The Android app uses [SeedlessCoreDS](core/README.md), a Nintendo DS emulation library reconstructed from DraStic r2.6.0.4a and built from source. The original DraStic APK and prebuilt core are not required to build or run SeedlessDS.

## What it is

SeedlessDS combines a game library, settings, input mapping, save states, cheats and RetroAchievements with a renderer for one or two physical displays. The Android frontend and reconstructed core communicate through JNI and the core's C API.

## Why SeedlessDS exists

The project focuses on Android handhelds where emulation performance and physical controls matter. It complements the melonDS-based WatermelonDS project with a different core. Performance depends on the game, device and internal resolution; higher resolutions require more processing power.

## The differentiator

### DualScreen Presets

Presets coordinate scale, aspect ratio, alignment, position and touch mapping across both displays. Layouts can also be adjusted for a single display.

### Dual-panel rendering

The app coordinates each emulated frame and its presentation through EGL/OpenGL ES, with a separate presentation thread for the external display. This helps games that connect the two DS screens into one scene. Behavior still depends on the device's display implementation.

### Controller-first interface

The interface supports D-pad navigation, shoulder-button tab switching and configurable emulator shortcuts. On supported dual-display layouts, the second panel can show information about the selected item.

## Features

### Library

- User-selected game folders, favorites and accent-insensitive search.
- Library filters, a Continue Playing section and per-game save slots.
- Box art from libretro thumbnails, with a bundled index and disk cache.

### Emulation

- Save states with screenshots, quick save and quick load.
- Bundled cheat database and custom cheat codes.
- Global settings, per-game overrides and profiles.
- Editable on-screen controls, screen layouts, image filters and integer scaling.
- Internal resolution settings from 1x to 8x.

### RetroAchievements

The integration uses [`rcheevos`](https://github.com/RetroAchievements/rcheevos), pinned to v12.3.0. It provides achievements, leaderboards and profile information, including casual and hardcore score displays. Compatibility is still being tested; a score display alone does not imply that every Hardcore feature is supported.

### Built for a gamepad

Map DS buttons and emulator actions to physical controls: fast-forward, quick save/load, swapping screens, cycling layouts, microphone input and turbo.

### Front-end friendly

Launchers can start a game through the `LaunchGame` activity alias:

```sh
am start -n com.seedlessds.app/com.seedlessds.app.LaunchGame \
    -e rom_path /storage/emulated/0/roms/nds/game.nds \
    --ei load_slot 1
```

The app recognizes `.nds`, `.dsi`, `.srl` and `.ids` game files and includes archive handling for ZIP, 7z and RAR. File recognition does not guarantee that a game or every archive layout is compatible. Device testing of the 7z/RAR paths remains in progress.

Touch themes can be imported from **Settings > On-screen controls > Import community theme (.zip)**. The settings screen only exposes ZIP theme import and standalone background image import:

You can also import only one image from **Settings > On-screen controls > Import background image**, without importing a complete theme package.



The imported theme is stored locally and can be selected globally or for an individual game. The background is drawn around the DS screens, while the colors style the touch buttons.