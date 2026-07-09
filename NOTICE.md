# Notice

This repository is an unofficial Forge 1.20.1 port of LDLib2.

## Original Project

- Project: Low-Drag-MC/LDLib2
- URL: https://github.com/Low-Drag-MC/LDLib2
- Original author metadata retained by this port: KilaBash
- Organization: Low-Drag-MC
- License: LGPL-3.0

The original project name, mod id, mod display name, author metadata, and project URL are preserved in the mod metadata.

## Port Target

- Minecraft: 1.20.1
- Forge: 47.4.10+ (tested on 47.4.20)
- Java: 17
- Mappings: official
- Mod id: ldlib2
- Mod name: LowDragLib2
- Port version: 2.2.28-forge-1.20.1

## Validation

The port has been checked with:

```powershell
.\gradlew.bat --no-daemon --console=plain compileJava
.\gradlew.bat --no-daemon --console=plain build
.\gradlew.bat --no-daemon --console=plain runClient
.\gradlew.bat --no-daemon --console=plain compileJava reobfJar reobfJarJar sourcesJar
```

The generated all-in-one jar has also been tested in a normal Forge 47.4.20 launcher profile and reaches the game with LDLib2 loaded.

## Attribution

All original LDLib2 work belongs to its original copyright holders.
This repository contains compatibility changes for Minecraft 1.20.1 Forge and should not be confused with the official upstream LDLib2 repository.
