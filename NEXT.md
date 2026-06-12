# Mobile Eggbert LibGDX — Status & Next Steps

## Overview

Java/LibGDX port of **Mobile Eggbert** (Speedy Blupi), originally a Windows Phone XNA C# game (2013).

- **Source**: `/mobile-eggbert-legacy/mobile-eggbert-core/` — decompiled C# XNA source (~18,400 lines)
- **Framework**: LibGDX 1.12.1 with custom XNA 4.0 bridge layer
- **Naming**: All names deliberately use C#/XNA conventions (PascalCase) despite being Java

---

## What Is Done ✅

### Infrastructure
- Gradle 8.12 multi-module project: `core/`, `desktop/`, `android/`
- `DesktopLauncher` (LWJGL3 backend, 640×480 window, 30 fps)
- `AndroidLauncher` (Android backend)
- `CLAUDE.md` with full naming convention guidelines
- AGP 8.5.0 + Gradle 8.12 for JDK 21 compatibility

### XNA Bridge — `System.*` and `Microsoft.Xna.Framework.*`
All 24 bridge files complete (EventArgs, TimeSpan, IsolatedStorage, Game, GameTime, GraphicsDeviceManager, Color, Rectangle, Vector2, TitleContainer, GraphicsDevice, Viewport, SpriteBatch, Texture2D, SpriteEffects, BlendState, SpriteSortMode, SoundEffect, SoundEffectInstance, SoundState, TouchPanel, TouchLocation, TouchLocationState, TouchPanelCapabilities, ContentManager).

### Game Classes — `com.openeggbert.mobileeggbert`
All 20 C# source files ported (~18,400 lines, 100%):
Config, DDebug, Def, Env, EnvClasses, TinyPoint, TinyRect, Misc, GameData, Worlds, Text, Tables, MyResource, Jauge, Slider, Sound, Pixmap, InputPad, Game1, Decor.

### Porting fixes applied
- 49 naming-convention errors (camelCase call sites) fixed
- C# struct fields (`TinyPoint`, `TinyRect`) null at runtime — fixed with initializers
- Array element null initialization helpers added
- C# format strings `{0}`, `{1}` replaced by `%s`
- `String.split(",")` trailing-field bug fixed with `split(",", -1)` in Worlds.java

### `out`/`ref` parameter fixes
All C# `ref`/`out` parameters audited in `Decor.java`, `Misc.java`, `GameData.java`:
- `ref TinyPoint`: 8 methods — correct (modify fields in-place)
- `out bool`: **`AscenseurVertigo`** fixed to `boolean[]` pattern
- `out TinyRect`: `IntersectRect`/`UnionRect` use `TinyRect[]` correctly
- `out int`: `GetGamerInfo` uses `int[]` correctly

### Struct aliasing fixes (C# value semantics → Java)
Two full passes over `Decor.java`:
- **Pass 1** — 26 method parameters + 65 field assignments fixed with `.Copy()`
- **Pass 2** — 9 critical aliasing bugs fixed (doors, box-push, moving platforms, helicopter)

### SpriteBatch letterbox projection
`SpriteBatch.Begin()` sets orthographic projection mapping game coords (0,0)–(640,480) to a letterboxed screen area. Manual zoom/origin removed from all Pixmap drawing methods. `DrawBounds()` fixed to always return 640×480. `Origin()` returns (0,0).

### Input coordinate conversion
Universal letterbox formula in `InputPad.java` converts screen pixels → game coords on all platforms and window sizes.

### Android APK
`android:assembleDebug` → **BUILD SUCCESSFUL**
APK at `android/build/outputs/apk/debug/android-debug.apk`

---

## What Remains ❌

### 1. Gameplay testing
Run and manually verify (desktop, window 640×480):
- [ ] Main menu renders correctly, buttons work
- [ ] Blupi moves left/right, jumps, collides with terrain
- [ ] Collecting chest → door opens
- [ ] Box pushing works
- [ ] Moving platforms carry Blupi
- [ ] Level complete / win condition triggers
- [ ] Lives lost on enemies/traps

### 2. Sound
- [ ] Verify sound effects play (via LibGDX `Sound` backend, OpenAL)
- [ ] The PipeWire config warning in log is benign — verify no silent failure

### 3. Save / load
- [ ] Test `IsolatedStorageFile` save/load of settings and world progress
- [ ] Confirm files written to LibGDX local storage path

### 4. Android device / emulator testing
- [ ] Install `android-debug.apk` on device or emulator
- [ ] Verify touch input works with letterbox formula
- [ ] Verify landscape orientation and fullscreen

### 5. MoveObjectCopy audit
`MoveObjectCopy(ref MoveObject dst, MoveObject src)` in C# uses `ref` on `dst` — in Java
the method signature is `MoveObjectCopy(MoveObject dst, MoveObject src)` which works because
`MoveObject` is a reference type. **Needs verification** that the swap code at lines ~9064–9093
actually swaps objects correctly and not just field copies (the current code copies fields
via the method, so this should be fine — but warrants a quick read).

---

## Build

```bash
# Desktop run (use cached binary)
/home/robertvokac/.gradle/wrapper/dists/gradle-8.12-bin/cetblhg4pflnnks72fxwobvgv/gradle-8.12/bin/gradle desktop:run

# Desktop JAR
./gradlew desktop:jar

# Android debug APK
./gradlew android:assembleDebug
```

Assets from `speedyblupi-data` repo must be in `core/assets/`.

---

## Notes

- `Resource.cs` (61 lines) replaced by `MyResource.java` — no Java counterpart needed
- All method names are PascalCase (C# style) — intentional, do not change
- `TinyRect` field order is non-standard: `Left, Right, Top, Bottom`
- Gradle 8.12 + AGP 8.5 required for Android build (JDK 21 compatibility)
- Cached Gradle binary: `/home/robertvokac/.gradle/wrapper/dists/gradle-8.12-bin/cetblhg4pflnnks72fxwobvgv/gradle-8.12/bin/gradle`
- `./gradlew` does not work in this environment — use the cached binary above
