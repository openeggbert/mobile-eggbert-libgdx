# Mobile Eggbert LibGDX — Status & Next Steps

## Overview

Java/LibGDX port of **Mobile Eggbert** (Speedy Blupi), originally a Windows Phone XNA C# game (2013).

- **Source**: `/mobile-eggbert-legacy/mobile-eggbert-core/` — decompiled C# XNA source (~18,400 lines)
- **Framework**: LibGDX 1.12.1 with custom XNA 4.0 bridge layer
- **Naming**: All names deliberately use C#/XNA conventions (PascalCase) despite being Java

---

## What Is Done ✅

### Infrastructure
- Gradle 8.4 multi-module project: `core/`, `desktop/`, `android/`
- `DesktopLauncher` (LWJGL3 backend, 640×480 window, 30 fps)
- `AndroidLauncher` (Android backend)
- `CLAUDE.md` with full naming convention guidelines

### XNA Bridge — `System.*` and `Microsoft.Xna.Framework.*`
All 24 bridge files complete (EventArgs, TimeSpan, IsolatedStorage, Game, GameTime, GraphicsDeviceManager, Color, Rectangle, Vector2, TitleContainer, GraphicsDevice, Viewport, SpriteBatch, Texture2D, SpriteEffects, BlendState, SpriteSortMode, SoundEffect, SoundEffectInstance, SoundState, TouchPanel, TouchLocation, TouchLocationState, TouchPanelCapabilities, ContentManager).

### Game Classes — `com.openeggbert.mobileeggbert`
All 20 C# source files ported (~18,400 lines, 100%):
Config, DDebug, Def, Env, EnvClasses, TinyPoint, TinyRect, Misc, GameData, Worlds, Text, Tables, MyResource, Jauge, Slider, Sound, Pixmap, InputPad, Game1, Decor.

### Porting fixes applied during initial run
| Issue | Fix |
|---|---|
| 49 naming-convention errors (camelCase call sites) | Fixed in Game1, InputPad, Misc, Text, Pixmap |
| Qualified enum cases in switch (`Def.ButtonGlyph.X`) — invalid in Java 11 | Removed qualifier |
| C# struct fields (`TinyPoint`, `TinyRect`) null at runtime | Added `= new TinyPoint()` / `= new TinyRect()` at declaration |
| `Cellule[][]`, `MoveObject[]`, `TinyPoint[]` element nulls | Added `initCellule2D`, `initMoveObject`, `initTinyPoints` helpers |
| C# format strings `{0}`, `{1}` not replaced by `%s` | Fixed in Decor.java |
| `String.split(",")` drops trailing empty fields | Changed to `split(",", -1)` in Worlds.java |
| `android/build.gradle` missing `natives` configuration | Added `configurations { natives }` |

### `out`/`ref` parameter fixes

All C# `ref`/`out` parameters audited and fixed in `Decor.java`, `Misc.java`, `GameData.java`:
- `ref TinyPoint`: 8 methods — `TestPath` uses `end.X/Y =` in-place (correct), others only modify fields (naturally correct in Java)
- `out bool`: **`AscenseurVertigo`** was broken (primitives don't propagate) — fixed to `boolean[]` pattern ✅
- `out TinyRect`: `IntersectRect` and `UnionRect` use `TinyRect[]` correctly ✅
- `out int`: `GetGamerInfo` uses `int[]` correctly ✅

### Struct aliasing fixes (C# struct value semantics → Java)

C# structs are copied on assignment; Java objects are aliased. Two fix passes were done in `Decor.java`:

**Pass 1** — 26 method parameters + 65 field assignments fixed with `.Copy()`.

**Pass 2** — 9 additional critical bugs found and fixed:

| Location | Bug | Effect |
|---|---|---|
| Line 4417: `tinyPoint = end` | alias — modifying `end` also changed `tinyPoint` | Wall-sliding correction never fired; Blupi got stuck |
| Line 5606: `celSwitch = m_moveObject[icon].posCurrent` | alias — `celSwitch.X -= 34` corrupted `posCurrent.X` | `PlaySound` used wrong position |
| Lines 5630, 5701, 5726: `end = m_moveObject[icon].posCurrent` | alias — `end.X = newValue` changed `posCurrent.X` | Box pushing always computed `move.X = 0`; pushing broken |
| Line 7336: `tinyPoint = m_moveObject[i].posCurrent` | alias — `posCurrent.X - tinyPoint.X` always 0 | Moving platform velocity always zero; Blupi didn't ride platforms |
| Line 7341: `end = m_moveObject[i].posCurrent` | alias — `end.X++` advanced `posCurrent.X`; start == end | No path/collision testing for type-97 enemies |
| Line 7455: `end = m_moveObject[i].posCurrent` | alias — `end.X = (end.X+32)/64` (grid coord conversion) corrupted `posCurrent.X` to ~5–30 every frame | ALL animated objects (doors, enemies, coins) drawn near top-left; doors appeared not to open |
| 7 locations: `m_blupiPosHelico = m_blupiPos` | alias — helicopter position always equaled Blupi position | Helicopter mode behaved incorrectly |

### SpriteBatch projection (game-space letterboxing)
`SpriteBatch.Begin()` now sets an orthographic projection that maps game coordinates (0,0)–(640,480) directly to a letterboxed screen area. Drawing code no longer needs to apply zoom or origin offsets manually.

---

## What Remains ❌

### 1. Pixmap.java — remove manual zoom/origin offsets ✅ DONE

SpriteBatch now handles the letterboxed projection. Manual zoom/origin removed from:
`GetDstRectangle`, `DrawBackground` (simplified to one DrawPart at (0,0)), `DrawChar`, `HudIcon`, `DrawPart` channel-5, `DrawInputButton` cheat button, `HotSpotToHud`.
`Start()` now clears to `Color.Black` for black letterbox bars.

### 2. Gameplay testing
Run and verify:
- Blupi moves and collides correctly
- Doors open after collecting chest
- Box pushing works
- Moving platforms carry Blupi
- Level complete / win condition triggers

### 3. Sound
Verify audio plays correctly via LibGDX `Sound` backend.

### 4. Save / load
Test `IsolatedStorageFile` save/load of game progress (settings, world progress).

### 5. Android
APK builds successfully ✅ (`android:assembleDebug` → `android/build/outputs/apk/debug/android-debug.apk`).
Needs testing on device/emulator.

---

## Build

```bash
# Desktop run
./gradlew desktop:run

# Desktop JAR
./gradlew desktop:jar

# Android debug APK
./gradlew android:assembleDebug
```

Assets from `speedyblupi-data` repo must be in `core/assets/`.

---

## Notes

- `Resource.cs` (61 lines) from the C# source has no direct Java counterpart — replaced by `MyResource.java`
- All method names are PascalCase (C# style) — intentional, do not change
- `TinyRect` field order is non-standard: `Left, Right, Top, Bottom` (not `Left, Top, Right, Bottom`)
- Gradle 8.12 + AGP 8.5 required for Android build (JDK 21 compatibility); desktop also works with 8.12
- Cached Gradle binary: `/home/robertvokac/.gradle/wrapper/dists/gradle-8.12-bin/cetblhg4pflnnks72fxwobvgv/gradle-8.12/bin/gradle`
