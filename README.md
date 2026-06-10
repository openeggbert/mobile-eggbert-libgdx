# Mobile Eggbert — LibGDX Port

Java/LibGDX port of **Mobile Eggbert** (Speedy Blupi), originally a Windows Phone XNA game from 2013 by Epsitec SA.

## Port history

```
Original C# XNA (WP7/8) → decompiled with ILSpy
  → MonoGame C# port
    → C++ port with CNA framework (mobile-eggbert)
      → Java port with LibGDX (this repo)
```

## Build

Requires JDK 11+.

```bash
# Desktop
./gradlew desktop:run

# Build desktop JAR
./gradlew desktop:jar

# Android APK
./gradlew android:assembleDebug
```

## Assets

Game assets are not included. Copy from the `speedyblupi-data` repository into `core/assets/`:

```bash
cp -r /path/to/speedyblupi-data/icons    core/assets/
cp -r /path/to/speedyblupi-data/backgrounds core/assets/
cp -r /path/to/speedyblupi-data/sounds   core/assets/
cp -r /path/to/speedyblupi-data/worlds   core/assets/
```

## Project structure

```
core/     — game logic + XNA bridge + .NET emulation
desktop/  — LWJGL3 desktop launcher
android/  — Android launcher
```

See [PLAN.md](PLAN.md) for porting notes and progress.

## License

See [LICENSE](LICENSE).
