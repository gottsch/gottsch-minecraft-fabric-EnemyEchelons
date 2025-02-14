# Changelog for Enemy Echelons 1.20.1

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.4.0]  - 2025-02-14
Bringing to parity with Forge/Neoforge versions

### Added
- Config option to enable/disable custom HUD range (server-side). using a custom HUD range is more computationally expensive on the client side.
- Config option to display HUD on the client-side. This allows the player to turn off the HUD even if the server is setup to display it.

### Changed
- Added condition to check for the Custom HUD range config. If =false, then use vanilla.
- Now uses wispforest's owo-lib config library

- Moved hudRange config option to Server-side. Can give unfair advantages.
- Fixed mob level determination when mobs are blacklisted from all echelons.
- Fixed fabric.mod.json source url to point to the correct link.
- Reworked internal storage and references to echelons and histograms.
  -- works for a wider range of echelon configurations now.
- Reduced default HUD range to 3 (same as vanilla survival).
- Updated echelons toml file to v3.
- Using Changelog to log changes
- Port from 1.19.3





