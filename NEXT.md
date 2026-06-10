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
- `DesktopLauncher` (LWJGL3 backend)
- `AndroidLauncher` (Android backend)
- `CLAUDE.md` with full naming convention guidelines

### XNA Bridge — `System.*`
| File | Status |
|---|---|
| `System/EventArgs.java` | ✅ Done |
| `System/TimeSpan.java` | ✅ Done |
| `System/IO/IsolatedStorage/IsolatedStorageFile.java` | ✅ Done |
| `System/IO/IsolatedStorage/IsolatedStorageFileStream.java` | ✅ Done |
| `System/IO/IsolatedStorage/IsolatedStorageException.java` | ✅ Done |

### XNA Bridge — `Microsoft.Xna.Framework.*`
| File | Status |
|---|---|
| `Game.java` | ✅ Done |
| `GameTime.java` | ✅ Done |
| `GraphicsDeviceManager.java` | ✅ Done |
| `Color.java` | ✅ Done |
| `Rectangle.java` | ✅ Done |
| `Vector2.java` | ✅ Done |
| `TitleContainer.java` | ✅ Done |
| `Graphics/GraphicsDevice.java` | ✅ Done |
| `Graphics/Viewport.java` | ✅ Done |
| `Graphics/SpriteBatch.java` | ✅ Done |
| `Graphics/Texture2D.java` | ✅ Done |
| `Graphics/SpriteEffects.java` | ✅ Done |
| `Graphics/BlendState.java` | ✅ Done |
| `Graphics/SpriteSortMode.java` | ✅ Done |
| `Audio/SoundEffect.java` | ✅ Done |
| `Audio/SoundEffectInstance.java` | ✅ Done |
| `Audio/SoundState.java` | ✅ Done |
| `Input/Touch/TouchPanel.java` | ✅ Done |
| `Input/Touch/TouchLocation.java` | ✅ Done |
| `Input/Touch/TouchLocationState.java` | ✅ Done |
| `Input/Touch/TouchPanelCapabilities.java` | ✅ Done |
| `Content/ContentManager.java` | ✅ Done |

### Game Classes — `com.openeggbert.mobileeggbert`
| C# Source | Java File | Lines (C#) | Status |
|---|---|---|---|
| `Config.cs` | `Config.java` | 6 | ✅ Done |
| `DDebug.cs` | `DDebug.java` | 22 | ✅ Done |
| `Def.cs` | `Def.java` | 702 | ✅ Done |
| `Env.cs` | `Env.java` | 24 | ✅ Done |
| `EnvClasses.cs` | `EnvClasses.java` | 131 | ✅ Done |
| `TinyPoint.cs` | `TinyPoint.java` | 22 | ✅ Done |
| `TinyRect.cs` | `TinyRect.java` | 53 | ✅ Done |
| `Misc.cs` | `Misc.java` | 126 | ✅ Done |
| `GameData.cs` | `GameData.java` | 221 | ✅ Done |
| `Worlds.cs` | `Worlds.java` | 556 | ✅ Done |
| `Text.cs` | `Text.java` | 294 | ✅ Done |
| `Tables.cs` | `Tables.java` | 1,714 | ✅ Done |
| `MyResource.cs` | `MyResource.java` | 775 | ✅ Done |
| `Jauge.cs` | `Jauge.java` | 158 | ✅ Done |
| `Slider.cs` | `Slider.java` | 95 | ✅ Done |
| `Sound.cs` | `Sound.java` | 254 | ✅ Done |
| `Pixmap.cs` | `Pixmap.java` | 571 | ✅ Done |
| `InputPad.cs` | `InputPad.java` | 1,002 | ✅ Done |
| `Game1.cs` | `Game1.java` | 1,008 | ✅ Done |
| `Decor.cs` | `Decor.java` | **10,596** | ❌ **Missing** |

### Progress by Lines of Code
- **Ported**: ~7,800 lines of C# game logic (42%)
- **Missing**: 10,596 lines (`Decor.cs`) (58%)
- **By files**: 19 of 20 C# files complete (95%)

---

## What Remains ❌

### Decor.java — The Main Missing Piece

`Decor.java` is the largest and most complex class (10,596 lines C# → estimated ~8,000–10,000 lines Java).
It contains the **entire gameplay simulation**: tiles, physics, Blupi character AI, collision detection,
move objects (enemies/platforms/items), sound triggers, cheat codes, save/load, world transitions.

#### Key methods to port:

| Method | C# lines | Complexity |
|---|---|---|
| Inner classes (`Cellule`, `MoveObject`, `ByeByeObject`) | ~70 | Low |
| Fields (~80 fields) | ~250 | Low |
| `Constructor`, `Create()`, `InitDecor()` | ~180 | Low |
| `PlayPrepare()`, `BuildPrepare()` | ~80 | Low |
| `Build()` — rendering loop | ~410 | Medium |
| `DrawInfo()` | ~125 | Medium |
| `SetSpeedX/Y()`, `KeyChange()` | ~20 | Low |
| `SoundEnviron()`, `PlaySound()`, `StopSound()` | ~100 | Low |
| `AdaptMotorVehicleSound()`, `PosSound()` | ~60 | Low |
| `GetDim/SetDim`, `GetMission/SetMission`, etc. | ~50 | Low |
| `InitializeDoors()`, `MemorizeDoors()` | ~10 | Low |
| `GetCheatTinyText()` | ~30 | Low |
| `CheatAction()` | ~270 | Medium |
| `BlupiSearchIcon()` | ~315 | Medium |
| `BlupiRect()`, `BlupiAdjust()`, `BlupiBloque()` | ~120 | Medium |
| **`BlupiStep()`** | **~3,630** | **Very High** |
| `BlupiDead()` | ~65 | Low |
| `DecorDetect()` | ~80 | Medium |
| `TestPath()` | ~90 | Medium |
| `MoveObjectStep()`, `MoveObjectStepLine()`, `MoveObjectStepIcon()` | ~1,120 | High |
| `AscenseurDetect()`, `AscenseurVertigo()`, etc. | ~150 | High |
| Caisse (crate) helpers | ~250 | High |
| Collision helpers (IsLave, IsPiege, IsBlitz, etc.) | ~300 | Medium |
| Trajectory helpers (Balle/Move traj) | ~80 | Low |
| ByeBye animation helpers | ~100 | Medium |
| Voyage (life transition) helpers | ~200 | Medium |
| Border helpers (`IsRightBorder`, `AdaptBorder`, etc.) | ~500 | High |
| `CurrentRead()`, `CurrentWrite()` | ~300 | Low |
| `Read()` | ~60 | Low |
| `MainSwitchInitialize()`, `AdaptDoors()` | ~100 | Medium |
| Door/Gold helpers | ~110 | Low |

#### Known translation challenges:
- C# `struct` (value type) → Java `class` + `.Copy()` for every assignment
- `m_decor[x, y]` → `m_decor[x][y]` (2D array syntax)
- `ref T` parameters → direct mutation of Java objects (fields are public)
- `out bool param` → `boolean[]` single-element array
- `ref int param` → `int[]` single-element array
- `Worlds.GetDecorField(...) ?? (-1)` → null-coalescing must use ternary
- `m_random.Next(min, max)` → `m_random.nextInt(max - min) + min`

#### Porting strategy (recommended):

**Option A — Write in chunks** (no env var needed):
1. Write skeleton: inner classes + fields + all stub methods (~600 lines)
2. Edit in method bodies group by group (~500 lines per Edit call)

**Option B — Raise output limit**:
```bash
export CLAUDE_CODE_MAX_OUTPUT_TOKENS=100000
```
Then write the complete file in one shot.

---

## Build Instructions

```bash
# Desktop run (from project root)
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
- All method names are PascalCase (C# style), not camelCase — this is intentional
- `TinyRect` field order is non-standard: `Left, Right, Top, Bottom` (not `Left, Top, Right, Bottom`)
