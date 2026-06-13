# Mobile Eggbert LibGDX — Status & Next Steps

## Overview

Java/LibGDX port of **Mobile Eggbert** (Speedy Blupi), originally a Windows Phone XNA C# game (2013).

- **Source**: `/mobile-eggbert-legacy/mobile-eggbert-core/` — decompiled C# XNA source (~18,400 lines)
- **Framework**: LibGDX 1.12.1 with custom XNA 4.0 bridge layer
- **Naming**: All names deliberately use C#/XNA conventions (PascalCase) despite being Java

---

## Current State

**The port is structurally complete and the game runs.** All 20 game classes and 24 XNA bridge files are ported. The desktop build launches cleanly (no exceptions), saves game data to disk, and the Android APK builds successfully.

What has not been manually verified yet is the in-game visual correctness and gameplay mechanics — that requires someone to watch the window and play.

---

## What Is Done ✅

- Gradle 8.12 multi-module build: `core/`, `desktop/`, `android/`, `html/`
- All 24 XNA bridge files (`System.*`, `Microsoft.Xna.Framework.*`)
- All 20 game classes ported (~18,400 lines)
- All runtime bugs fixed: struct aliasing, `out`/`ref` params, null init, format strings
- SpriteBatch letterbox projection (640×480 game coords on any screen)
- Input coordinate conversion (screen pixels → game coords, all platforms)
- Save/load: `IsolatedStorageFile` → LibGDX local files, confirmed working (`desktop/SpeedyBlupi` 640 bytes written on first run)
- Android APK: `android:assembleDebug` → BUILD SUCCESSFUL
- **Web (GWT)**: `html:war` → `html/build/libs/html.war` (43 MB, GWT 2.8.2, 5 permutations compiled)
- **Linux desktop JAR**: `desktop:jar` → `desktop/build/libs/mobile-eggbert.jar` (34 MB fat JAR with all natives)
- **GWT compatibility fixes**: removed `String.format()` (GWT 2.8.x emulation gap), `Class.cast()` → explicit cast, `IsolatedStorageFileStream` now uses `FileHandle.readBytes()/writeBytes()` (no `java.io.*`), `TitleContainer` now returns `FileHandle` instead of `InputStream`
- `MoveObjectCopy` 3-way swap verified correct
- **Sound fix**: `SoundEffectInstance` — non-looped sounds now set `state = Stopped` immediately after `play()` since LibGDX `Sound` has no completion callback; without this fix all channels became permanently "busy" after first play
- **Input fix**: `InputPad` — `TouchPanel.GetState()` is now skipped on Desktop (LWJGL3 maps `isTouched()` to mouse, which duplicated every click with the explicit mouse handler below it)

---

## What Remains — Manual Testing

### 1. Desktop gameplay (priority)
Run: `/home/robertvokac/.gradle/wrapper/dists/gradle-8.12-bin/cetblhg4pflnnks72fxwobvgv/gradle-8.12/bin/gradle desktop:run`

- [ ] Main menu renders correctly, buttons respond to click
- [ ] Blupi moves left/right, jumps, collides with terrain
- [ ] Collecting chest → door opens
- [ ] Box pushing works
- [ ] Moving platforms carry Blupi
- [ ] Win condition (level complete) triggers
- [ ] Lives lost on enemies/traps
- [ ] Sound effects are audible

### 2. Android device / emulator
APK: `android/build/outputs/apk/debug/android-debug.apk`

- [ ] Install and launch on device or emulator
- [ ] Touch input works (letterbox formula)
- [ ] Landscape orientation and fullscreen correct

---

## Next After Testing

If gameplay testing reveals bugs, likely candidates:

1. **Rendering glitches** — sprite positioning or tile mapping off → check `Pixmap.DrawIcon` / `Decor.DrawFloor`
2. **Input not registering** — double-click bug already fixed; if still wrong check hit area geometry in `InputPad.ButtonDetect`
3. **Crash on level load** — missing asset or bad world file parse → check `Worlds.readWorld` + `Decor.Create`
4. **No sound** — channel-busy bug already fixed; if still silent check OpenAL/PipeWire or `Def.HasSound` flag

---

## Build Reference

```bash
# Desktop run
/home/robertvokac/.gradle/wrapper/dists/gradle-8.12-bin/cetblhg4pflnnks72fxwobvgv/gradle-8.12/bin/gradle desktop:run

# Desktop JAR
/home/robertvokac/.gradle/wrapper/dists/gradle-8.12-bin/cetblhg4pflnnks72fxwobvgv/gradle-8.12/bin/gradle desktop:jar

# Android debug APK
/home/robertvokac/.gradle/wrapper/dists/gradle-8.12-bin/cetblhg4pflnnks72fxwobvgv/gradle-8.12/bin/gradle android:assembleDebug

# HTML/GWT WAR (deployable to servlet container or unzip to serve statically)
/home/robertvokac/.gradle/wrapper/dists/gradle-8.12-bin/cetblhg4pflnnks72fxwobvgv/gradle-8.12/bin/gradle html:war
```

- Assets must be in `core/assets/` (from `speedyblupi-data` repo)
- `./gradlew` does not work in this environment — use the cached binary above
- Save files written to `desktop/SpeedyBlupi` and `desktop/CurrentGame` (in `.gitignore`)
- `TinyRect` field order: `Left, Right, Top, Bottom` (non-standard)
