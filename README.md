# Create: Xaero's World Map Train Tracker

A NeoForge 1.21.1 client/server mod that reads Create's server-side railway data
and displays live train names and coordinates in a small HUD panel and Xaero
waypoint markers.

## Compatibility checked

- Minecraft 1.21.1, Java 21, and the template's NeoForge 21.1.259.
- Create 6.0.10 for 1.21.1 NeoForge.
- Xaero's World Map 1.47.0 and Xaero's Minimap 26.6.0 for NeoForge 1.21.1.

Create is a required runtime dependency because train tracking reads Create's
railway data. Xaero's World Map and Minimap are optional. With Xaero's Minimap
installed, the mod registers dynamic third-party waypoints through its waypoint
manager. Xaero's World Map consumes those markers through its Minimap
compatibility integration when both Xaero mods are installed.

The Gradle `client` development run includes Create, Xaero's World Map, and
Xaero's Minimap through `localRuntime` dependencies. The server and GameTest
development runs also resolve those dependencies; Xaero declares itself
client-side/server-optional.

When manually assembling the development `run/mods` directory, use:

- `create-1.21.1-6.0.10.jar`
- `xaeroworldmap-neoforge-1.21.1-1.47.0.jar`
- `xaerominimap-neoforge-1.21.1-26.6.0.jar`

The Xaero JARs bundle XaeroLib. The Create JAR bundles its Ponder, Flywheel, and
Registrate runtime libraries. Do not copy duplicate versions of these JARs into
`run/mods` when launching with Gradle; Gradle already supplies them.

Dependency metadata only declares mod requirements. The train tracking and
waypoint integration are implemented in code.

## Xaero waypoint integration

The implementation uses the third-party waypoint manager present in the
checked Xaero Minimap release, rather than editing waypoint files or injecting
into map rendering internals. Markers are updated/removed by train UUID, include
the train name, and are scoped to the player's current dimension. The checked
World Map release consumes these third-party waypoints through its Minimap
compatibility integration.

World Map without Minimap has no shared third-party waypoint manager available
to this integration; the independent train HUD still works. The HUD is also
retained when neither Xaero mod is installed. Marker integration was verified
against the checked runtime API signatures and by compilation, but has not yet
been tested in a launched game.

## Build

From the project directory, run:

```powershell
.\gradlew.bat build
```

The mod JAR is written to `build\libs\createxaerosworldmaptraintracker-1.0.0.jar`.

## Install

1. Install Java 21 and NeoForge 21.1.x for Minecraft 1.21.1.
2. Install Create 6.x for Minecraft 1.21.1 on the server and every client.
3. Optionally install Xaero's Minimap for 1.21.1 to show train waypoints. Add
   Xaero's World Map as well to see them on the fullscreen map.
4. Copy the built mod JAR into the `mods` folder on the server and each client.
5. Start the game/server. The server reads trains from Create and synchronizes
   changed positions to connected clients.

## Test in game

1. Launch a NeoForge 1.21.1 world with Create and this mod installed.
2. Build, name, and assemble a Create train on a connected track.
3. Stand in the train's dimension. The upper-left HUD panel should show the
   train's name and block coordinates; movement should update its listed
   position, and removed trains should disappear.
4. Test with multiple trains and dimensions. The panel filters to the current
   dimension; the server tracker maintains UUID-keyed records to avoid duplicate
   entries.
5. With Xaero's Minimap installed, verify gold `T` waypoints appear for each
   train. With both Xaero mods installed, verify the same markers on World Map.
   Marker positions should follow trains and disappear when trains are removed.
   World Map without Minimap has no live waypoint integration.
6. Xaero is optional; repeat without either Xaero mod. Create is required.

The Gradle build verifies compilation and packages the JAR; it does not replace
an in-game runtime test with the listed mod combination.
