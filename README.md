# 🚂 Create Xaero Train Tracker

A Minecraft mod that tracks Create trains and displays them in a client HUD and
as Xaero waypoint markers.

## ✨ Features

- Reads train positions from Create railway data.
- Sends changed train positions from server to connected clients.
- Displays train names and coordinates in the client HUD.
- Adds live third-party train waypoints to Xaero's Minimap. Xaero's World Map
  displays the waypoints through its Minimap compatibility integration.
- Supports `SERVER`, `CLIENT`, and `BOTH` train collection modes.
- Keeps Xaero integrations optional; the HUD remains available without Xaero.

Train collection and the map display are implemented. Live in-game behavior
must be tested with the mod combination described below; a successful build
does not verify gameplay behavior.

## 📋 Compatibility and requirements

- **Minecraft:** Java Edition 1.21.1
- **Java:** 21
- **Mod loader:** NeoForge 21.1.257
- **Required:** Create 6.0.10 for Minecraft 1.21.1
- **Optional:** Xaero's Minimap 26.6.0 and Xaero's World Map 1.47.0 for
  NeoForge 1.21.1

Create is required for train data. Xaero's Minimap and World Map are optional.
Train markers require Minimap; World Map markers require both Xaero mods.
World Map by itself does not provide the waypoint manager used by this
integration.

## ⚙️ Tracking-side configuration

The server-synced `tracking_side` setting is in
`config/createxaerosworldmaptraintracker-server.toml` (or the world's
`serverconfig` directory):

- `SERVER` (default) reads Create's authoritative server railway data and
  synchronizes changed train positions to clients.
- `CLIENT` reads the local Create client railway mirror without server tracking
  or server train-update packets.
- `BOTH` reads on the server and client. Client displays merge entries by train
  UUID and prefer authoritative server updates when present.

The HUD and Xaero waypoint rendering are client-side in every mode. Client
collection uses Create's `GlobalRailwayManager.sided(LevelAccessor)` API.

## 📥 Installation

1. Install Java 21 and NeoForge 21.1.257 for Minecraft 1.21.1.
2. Install Create 6.0.10 for Minecraft 1.21.1 on the server and every client.
3. Optionally install Xaero's Minimap 26.6.0 to show train waypoints. Add
   Xaero's World Map 1.47.0 as well to show them on the fullscreen map.
4. Copy the mod JAR into the `mods` folder on the server and each client.
5. Start the game/server. Create train data is collected and synchronized
   according to the configured tracking side.

## 🛠️ Building from source

This project uses Java 21 and Gradle. From the project folder on Windows, run:

```powershell
.\gradlew.bat clean build
```

The compiled JAR is written to:

```text
build/libs/
```

The Gradle client development run resolves Create and both Xaero mods using
`localRuntime`; do not duplicate them in `run/mods`.

## 🧪 In-game test

1. Launch Minecraft 1.21.1 with NeoForge 21.1.257, Create, and this mod.
2. Build, name, and assemble a Create train on connected tracks.
3. Check the HUD for the train name and coordinates. Confirm that movement
   updates the position and removed trains disappear.
4. Test multiple trains, dimensions, and the `SERVER`, `CLIENT`, and `BOTH`
   configuration values.
5. With Minimap installed, check for gold `T` waypoints. With both Xaero mods,
   check that the waypoints also appear on World Map.
6. Repeat without Xaero to confirm the HUD still works.

No gameplay test is implied by compilation alone.

## 🐛 Bug reports

If you find a bug, open a [GitHub Issue](../../issues) and include the Minecraft,
NeoForge, Create, and Xaero versions, steps to reproduce, and a crash report if
applicable.

## 📜 Disclaimer

This is an independent community project. It is not affiliated with or endorsed
by the developers of Create or Xaero's mods.
