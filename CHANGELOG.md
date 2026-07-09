## v2.2.28
* Fixed fbo clear color
* Fixed shader defines injection
* Improved ngt qol

## 2.2.28-forge-1.20.1
* Unofficial Forge 1.20.1 port of Low-Drag-MC/LDLib2.
* Preserves the original LDLib2 project name, mod id, author metadata, and LGPL-3.0 license.
* Bundles required runtime dependencies for the Forge jarJar release.
* Stops bundling Minecraft-provided `fastutil` through the Taffy jarJar dependency.
* Fixes the production `ScreenMixin` shadow lookup for normal Forge launcher profiles.
* Fixes production Forge startup issues around refmaps, MixinExtras, shader mixins, and unstable model bakery hooks.
* Tested with Minecraft 1.20.1 / Forge 47.4.20 in a normal launcher profile.
