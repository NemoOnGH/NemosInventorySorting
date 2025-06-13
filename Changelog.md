# Changelog v1.11

## Additions
- Added inventory sorting buttons to most container UIs
  - **Exceptions:** Loom, Cartography Table, Smithing Table, and Beacon
- Added configuration options for container-specific inventory buttons
- **Mod Compatibility**
  - Added support for **Iron Chests** (NeoForge only)
    - A separate config file for Iron Chest components will be generated if the mod is installed

## Changes
- **Config Updates**
  - Renamed config fields to reflect new features
  - Removed default `xOffset` for the filter and persistence toggle buttons
    - The offset is now dynamically calculated based on the container `imageWidth`
  - Slightly adjusted the position of the filter persistence toggle

  ⚠️ **Note:** These changes may result in incorrectly positioned buttons.  
  To resolve this, either:
  - Delete your current config file and restart Minecraft, or
  - Update your config using the values provided on the [wiki](https://github.com/NemoNotFound/NemosInventorySorting/wiki)