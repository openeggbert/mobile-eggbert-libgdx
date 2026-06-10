# Mobile Eggbert LibGDX — Claude Code Guidelines

## Project Overview

Java/LibGDX port of **Mobile Eggbert** (Speedy Blupi), originally a Windows Phone XNA C# game (2013).

Port source: `/rv/data/development/github.com/openeggbert/mobile-eggbert-legacy/mobile-eggbert-core` (decompiled C# XNA source)

---

## CRITICAL: Naming Conventions

**This project deliberately uses C#/XNA naming conventions instead of Java conventions.**
This is intentional and must be preserved throughout the entire codebase.

### Package names — mirror C# namespaces exactly

| C# namespace | Java package |
|---|---|
| `System` | `System` |
| `System.Text` | `System.Text` |
| `System.IO.IsolatedStorage` | `System.IO.IsolatedStorage` |
| `Microsoft.Xna.Framework` | `Microsoft.Xna.Framework` |
| `Microsoft.Xna.Framework.Graphics` | `Microsoft.Xna.Framework.Graphics` |
| `Microsoft.Xna.Framework.Audio` | `Microsoft.Xna.Framework.Audio` |
| `Microsoft.Xna.Framework.Input.Touch` | `Microsoft.Xna.Framework.Input.Touch` |
| `Microsoft.Xna.Framework.Content` | `Microsoft.Xna.Framework.Content` |
| game code | `com.openeggbert.mobileeggbert` (only exception — no C# equivalent) |

Directory structure matches: package `System.IO.IsolatedStorage` → `src/main/java/System/IO/IsolatedStorage/`

### Method names — PascalCase like C#

```java
// CORRECT — C# style
public void DrawIcon(int channel, int icon, TinyRect rect) { }
public void LoadContent() { }
public TinyRect DrawBounds() { }
public TinyPoint Origin() { }
public boolean GetHide() { }
public void SetHide(boolean v) { }

// WRONG — Java convention, do NOT use
public void drawIcon(...) { }
public void loadContent() { }
public TinyRect getDrawBounds() { }
```

### Field and property names — PascalCase like C#

```java
// C# property `public bool Sounds { get; set; }` becomes:
public boolean Sounds;
// or with explicit accessors matching C# property name:
public boolean GetSounds() { return Sounds; }
public void SetSounds(boolean v) { Sounds = v; }
```

### Class names — already PascalCase (no change needed)

### Constant names — ALL_CAPS static finals (same as C#)

```java
public static final int TX_BUTTON_PLAY = 100;
public static final int LXIMAGE = 640;
```

### Enum values — PascalCase like C#

```java
public enum Phase { None, First, Wait, Init, Play, Pause, Lost, Win, ... }
// NOT: NONE, FIRST, WAIT — keep C# casing
```

---

## Source Layout

```
core/src/main/java/
  System/                        System.* emulation (.NET BCL)
    EventArgs.java
    TimeSpan.java
    IO/IsolatedStorage/
      IsolatedStorageFile.java
      IsolatedStorageFileStream.java
      IsolatedStorageException.java
  Microsoft/Xna/Framework/       XNA 4.0 bridge over LibGDX
    Game.java                    → ApplicationListener
    GameTime.java
    GraphicsDeviceManager.java
    Rectangle.java
    Vector2.java
    Color.java
    TitleContainer.java
    Graphics/
      GraphicsDevice.java
      Viewport.java
      SpriteBatch.java           → gdx SpriteBatch
      Texture2D.java             → gdx Texture
      SpriteEffects.java
      BlendState.java
      SpriteSortMode.java
    Audio/
      SoundEffect.java           → gdx Sound
      SoundEffectInstance.java
      SoundState.java
    Input/Touch/
      TouchPanel.java
      TouchLocation.java
      TouchLocationState.java
      TouchPanelCapabilities.java
    Content/
      ContentManager.java
  com/openeggbert/mobileeggbert/  Game code
    Def.java, TinyPoint.java, TinyRect.java, Config.java, DDebug.java
    Env.java, EnvClasses.java
    Misc.java, MyResource.java, GameData.java
    Tables.java, Worlds.java, Text.java
    Jauge.java, Slider.java
    Sound.java, Pixmap.java, InputPad.java
    Decor.java, Game1.java
desktop/src/main/java/
  com/openeggbert/mobileeggbert/desktop/DesktopLauncher.java
android/src/main/java/
  com/openeggbert/mobileeggbert/android/AndroidLauncher.java
```

---

## Code Rules

### General
- **Every method, class, package name must mirror the C# original as closely as possible.**
- Do not rename methods to Java camelCase — keep PascalCase from C#.
- Do not rename packages to lowercase — keep C#-style casing.
- Do not add features, abstractions, or Java-specific patterns not present in the C# source.

### C# → Java mapping reference

| C# pattern | Java equivalent |
|---|---|
| `public static class Foo` | `public class Foo` with all-static members |
| `struct TinyPoint` | `class TinyPoint` — add `Copy()` method for value-type semantics |
| `default(TinyPoint)` | `new TinyPoint()` |
| `out T param` | `T[] param` (single-element array) |
| `int?` (nullable) | `Integer` |
| `IEnumerable<T>` + yield | `List<T>` built with ArrayList |
| `string` | `String` |
| `bool` | `boolean` |
| `String.Format(...)` | `String.format(...)` |
| `Math.Min/Max` | `Math.min/max` |
| `List<T>` | `java.util.ArrayList<T>` |
| `Dictionary<K,V>` | `java.util.HashMap<K,V>` |
| `foreach` | enhanced `for` |
| `string.IsNullOrEmpty` | `str == null \|\| str.isEmpty()` |
| `int.TryParse` | `try { Integer.parseInt(...) } catch (NumberFormatException e)` |
| `CultureInfo.InvariantCulture` | `Locale.ROOT` |
| `#if KNI && WEB` blocks | skip — desktop/Android only |
| `IsolatedStorageFile` | `System.IO.IsolatedStorage.IsolatedStorageFile` → LibGDX files |

### TinyRect field order
`TinyRect` has **non-standard field order**: `Left, Right, Top, Bottom`
(not `Left, Top, Right, Bottom`)

Constructor: `new TinyRect(left, right, top, bottom)`

### Asset loading
XNA `Content.Load<Texture2D>("icons/blupi")` → LibGDX appends `.png`
XNA `Content.Load<SoundEffect>("sounds/sound000")` → LibGDX appends `.wav`

### IsolatedStorage → LibGDX Files
`IsolatedStorageFile.GetUserStoreForApplication()` → `Gdx.files.local(path)`

---

## Build

```bash
# Desktop run
./gradlew desktop:run

# Desktop JAR
./gradlew desktop:jar

# Android APK
./gradlew android:assembleDebug
```

Requires JDK 11+. Assets from `speedyblupi-data` repo must be copied to `core/assets/`.

---

## Porting Status

See [PLAN.md](PLAN.md) for detailed porting progress and phase breakdown.

Current state: infrastructure + bridge layers complete, game classes being ported.
