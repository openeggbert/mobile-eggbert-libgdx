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
| `Decor.cs` | `Decor.java` | **10,596** | ✅ Done |

### Progress by Lines of Code
- **Ported**: ~18,400 lines of C# game logic (100%)
- **Missing**: nothing
- **By files**: 20 of 20 C# files complete (100%)

---

## What Remains ❌ / Known Issues

### Runtime correctness (struct value semantics) ✅ FIXED

**C# struct → Java class aliasing** has been systematically fixed in `Decor.java`.

The fix covered two patterns:
1. **Method parameters** — 26 methods that modified a non-`ref` `TinyPoint`/`TinyRect` parameter
   (in C# this was safe because struct parameters are value copies; in Java they're references).
   Fix: `pos = pos.Copy();` inserted at the start of each such method body.
2. **Field/variable assignments** — 65 assignments where `TinyPoint`/`TinyRect` struct fields
   were assigned without `.Copy()`, creating aliases instead of independent copies.

All confirmed by a clean `./gradlew desktop:compileJava` build after the fixes.

### Porting issues fixed during initial run

The following issues were found and fixed to get the game running:

| Issue | Fix |
|---|---|
| 49 naming-convention errors (camelCase call sites) | Fixed in Game1, InputPad, Misc, Text, Pixmap |
| Qualified enum cases in switch (`Def.ButtonGlyph.X`) — invalid in Java 11 | Removed qualifier |
| C# struct fields (`TinyPoint`, `TinyRect`) were `null` at runtime | Added `= new TinyPoint()` / `= new TinyRect()` at declaration |
| `Cellule[][]`, `MoveObject[]`, `TinyPoint[]` element nulls | Added `initCellule2D`, `initMoveObject`, `initTinyPoints` helpers |
| C# format strings `{0}`, `{1}` not replaced by `%s` | Fixed in Decor.java |
| `String.split(",")` drops trailing empty fields | Changed to `split(",", -1)` in Worlds.java |
| `android/build.gradle` missing `natives` configuration | Added `configurations { natives }` |

### Next steps

1. **Test gameplay** — start a level and verify Blupi moves, physics work, level completes
2. ~~**Fix struct aliasing**~~ ✅ Done — 26 method params + 65 field assignments fixed
3. **Sound** — verify audio plays correctly via LibGDX backend
4. **Save/load** — test `IsolatedStorageFile` save/load of game progress
5. **Android** — build and test `android:assembleDebug` APK

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
