# Mobile Eggbert LibGDX — Port Plan

## Overview

Port of Mobile Eggbert (Speedy Blupi) from C#/XNA to Java/LibGDX.
Source: `/rv/data/development/github.com/openeggbert/mobile-eggbert-legacy/mobile-eggbert-core`

The C# source is the decompiled original Windows Phone XNA game (2013).

---

## Project Layout

```
mobile-eggbert-libgdx/
├── core/src/main/java/
│   ├── system/                        .NET BCL emulation (System.*)
│   │   ├── Math.java                  System.Math → java.lang.Math wrapper
│   │   ├── Random.java                System.Random → java.util.Random wrapper
│   │   ├── TimeSpan.java              System.TimeSpan — custom
│   │   ├── EventArgs.java             System.EventArgs — marker
│   │   ├── text/
│   │   │   └── StringBuilder.java     thin alias (use java.lang.StringBuilder directly)
│   │   └── io/
│   │       └── isolatedstorage/
│   │           ├── IsolatedStorageFile.java      → LibGDX Files (local storage)
│   │           └── IsolatedStorageFileStream.java → Java InputStream/OutputStream
│   ├── microsoft/xna/framework/       XNA 4.0 bridge over LibGDX
│   │   ├── Game.java                  → ApplicationListener
│   │   ├── GameTime.java              wraps LibGDX delta
│   │   ├── GraphicsDeviceManager.java wraps Gdx.graphics
│   │   ├── Rectangle.java             wraps com.badlogic.gdx.math.Rectangle
│   │   ├── Vector2.java               wraps com.badlogic.gdx.math.Vector2
│   │   ├── Color.java                 wraps com.badlogic.gdx.graphics.Color
│   │   ├── TitleContainer.java        asset loading via Gdx.files
│   │   ├── graphics/
│   │   │   ├── GraphicsDevice.java    viewport access
│   │   │   ├── Viewport.java          screen dimensions
│   │   │   ├── SpriteBatch.java       → gdx SpriteBatch
│   │   │   ├── Texture2D.java         → gdx Texture
│   │   │   ├── SpriteEffects.java     flip flags
│   │   │   ├── BlendState.java        blend modes
│   │   │   └── SpriteSortMode.java    draw order enum
│   │   ├── audio/
│   │   │   ├── SoundEffect.java       → gdx Sound
│   │   │   ├── SoundEffectInstance.java → gdx Sound control
│   │   │   └── SoundState.java        playback state enum
│   │   └── input/touch/
│   │       ├── TouchLocation.java     touch point
│   │       ├── TouchLocationState.java
│   │       ├── TouchPanel.java        → Gdx.input touch query
│   │       └── TouchPanelCapabilities.java
│   └── com/openeggbert/mobileeggbert/  Game code (direct C#→Java port)
│       ├── Env.java / EnvClasses.java  platform detection
│       ├── Config.java
│       ├── DDebug.java
│       ├── Def.java
│       ├── TinyPoint.java
│       ├── TinyRect.java
│       ├── Misc.java
│       ├── MyResource.java
│       ├── GameData.java
│       ├── Tables.java                (largest non-Decor file)
│       ├── Worlds.java
│       ├── Text.java
│       ├── Jauge.java
│       ├── Slider.java
│       ├── Sound.java
│       ├── Pixmap.java
│       ├── InputPad.java
│       ├── Decor.java                 (largest — 10 596 lines C#)
│       └── Game1.java
├── desktop/src/main/java/
│   └── com/openeggbert/mobileeggbert/desktop/
│       └── DesktopLauncher.java
└── android/src/main/java/
    └── com/openeggbert/mobileeggbert/android/
        └── AndroidLauncher.java
```

---

## Porting Progress

### Phase 1 — Infrastructure [ ]
- [ ] `system.*` — .NET BCL emulation
- [ ] `microsoft.xna.framework.*` — XNA bridge over LibGDX

### Phase 2 — Simple game classes [ ]
- [ ] `TinyPoint`
- [ ] `TinyRect`
- [ ] `Config`
- [ ] `DDebug`
- [ ] `Def`
- [ ] `EnvClasses` / `Env`
- [ ] `Misc`

### Phase 3 — Data and serialisation [ ]
- [ ] `GameData`
- [ ] `Worlds`
- [ ] `MyResource`
- [ ] `Text`
- [ ] `Tables`

### Phase 4 — Subsystems [ ]
- [ ] `Sound`
- [ ] `Pixmap`
- [ ] `Jauge`
- [ ] `Slider`
- [ ] `InputPad`

### Phase 5 — Core gameplay [ ]
- [ ] `Decor`
- [ ] `Game1`

### Phase 6 — Launchers [ ]
- [ ] `DesktopLauncher`
- [ ] `AndroidLauncher`

---

## Key Mapping Notes

### C# → Java type mapping
| C# | Java |
|---|---|
| `int` | `int` |
| `byte` | `byte` / `int & 0xFF` for unsigned |
| `bool` | `boolean` |
| `string` | `String` |
| `double` | `double` |
| `float` | `float` |
| `struct` | `class` (value semantics copied manually at call sites) |
| `out T` | returned via single-element array `int[]` or wrapper |
| `static class` | `class` with all static methods |
| `?? default` | `== null ? new X() : x` |
| `string.Format` | `String.format` |
| `Math.Min/Max` | `Math.min/max` |
| `List<T>` | `java.util.List<T>` / `ArrayList<T>` |
| `new int[]{}` | `new int[]{}` (identical) |
| `foreach` | enhanced `for` |
| `nameof()` | `"fieldName"` (literal) |
| `int?` | `Integer` (nullable) |

### XNA → LibGDX mapping
| XNA | LibGDX |
|---|---|
| `SpriteBatch.Begin/End` | `batch.begin()` / `batch.end()` |
| `Texture2D` | `com.badlogic.gdx.graphics.Texture` |
| `Rectangle(x,y,w,h)` | same (LibGDX uses float) |
| `Color.White` | `Color.WHITE` |
| `GraphicsDevice.Viewport.Width` | `Gdx.graphics.getWidth()` |
| `SoundEffect.CreateInstance()` | `gdxSound.loop()` / `gdxSound.play()` |
| `Content.Load<Texture2D>(name)` | `new Texture(Gdx.files.internal(name+".png"))` |
| `Content.Load<SoundEffect>(name)` | `Gdx.audio.newSound(Gdx.files.internal(name+".wav"))` |
| `TitleContainer.OpenStream(path)` | `Gdx.files.internal(path).read()` |
| `IsolatedStorageFile` (save data) | `Gdx.files.local(path)` |
| `TouchPanel.GetState()` | `Gdx.input.isTouched()` |
| `Keyboard.GetState()` | `Gdx.input.isKeyPressed(Keys.*)` |

### Tricky C# patterns
- `out` parameters → return value struct or single-element array
- `TinyRect`/`TinyPoint` are `struct` in C# (value types) → copy on assignment in Java
- `default(TinyRect)` → `new TinyRect()`
- `string.IsNullOrEmpty` → `str == null || str.isEmpty()`
- `int.TryParse` → try/catch `Integer.parseInt`
- `double.TryParse` → try/catch `Double.parseDouble`
- `bool.TryParse` → `"true".equalsIgnoreCase(s)`
- `CultureInfo.InvariantCulture` in format → `Locale.US` / `Locale.ROOT`
- `#if KNI && WEB` preprocessor blocks → ignored (desktop/Android only)

---

## Assets

Game assets (textures, sounds, world files) come from `speedyblupi-data` repo.
Copy or symlink into `core/assets/` before building.

Expected structure:
```
core/assets/
  icons/      blupi.png, blupi1.png, button.png, element.png, explo.png,
              jauge.png, object-m.png, pad.png, text.png
  backgrounds/ blupiyoupie.png, gear.png, speedyblupi.png
  sounds/     sound000.wav … sound092.wav
  worlds/     world000.txt … world???.txt
```
