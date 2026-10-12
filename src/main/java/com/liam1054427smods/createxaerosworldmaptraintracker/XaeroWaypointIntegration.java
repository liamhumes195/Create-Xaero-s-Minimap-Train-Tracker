package com.liam1054427smods.createxaerosworldmaptraintracker;

import net.neoforged.fml.ModList;

final class XaeroWaypointIntegration {
    private XaeroWaypointIntegration() {
    }

    static void refresh() {
        if (ModList.get().isLoaded("xaerominimap")) {
            XaeroBridge.refresh();
        }
    }

    static void clear() {
        if (ModList.get().isLoaded("xaerominimap")) {
            XaeroBridge.clear();
        }
    }

    private static final class XaeroBridge {
        private static final net.minecraft.resources.ResourceLocation ORIGIN =
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                        CreateXaerosWorldMapTrainTracker.MODID, "trains"
                );
        private static xaero.hud.minimap.waypoint.thirdparty.ThirdPartyWaypointManager lastManager;
        private static net.minecraft.resources.ResourceLocation lastDimension;
        private static boolean hadWaypoints;
        private static long lastRevision = -1;

        private XaeroBridge() {
        }

        private static void refresh() {
            net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
            if (minecraft.player == null || minecraft.level == null) {
                clear();
                return;
            }

            xaero.hud.minimap.module.MinimapSession session =
                    xaero.hud.minimap.BuiltInHudModules.MINIMAP.getCurrentSession();
            xaero.hud.minimap.world.MinimapWorld world = session == null
                    ? null
                    : session.getWorldManager().getCurrentWorld();
            if (world == null) {
                clear();
                return;
            }

            net.minecraft.resources.ResourceLocation dimension = minecraft.level.dimension().location();
            xaero.hud.minimap.waypoint.thirdparty.ThirdPartyWaypointManager manager =
                    world.getContainer().getThirdPartyWaypointManager();
            if (manager != lastManager || !dimension.equals(lastDimension)) {
                clearPrevious();
                lastManager = manager;
                lastDimension = dimension;
            }

            long revision = ClientTrainData.revision();
            if (revision == lastRevision) {
                return;
            }

            java.util.Map<String, TrainMarker> trains = new java.util.HashMap<>();
            for (TrainMarker marker : ClientTrainData.markers()) {
                if (marker.dimension().equals(dimension)) {
                    trains.put(marker.id().toString(), marker);
                }
            }

            if (trains.isEmpty()) {
                if (hadWaypoints) {
                    manager.clearOrigin(ORIGIN);
                    hadWaypoints = false;
                }
                lastRevision = revision;
                return;
            }

            xaero.hud.minimap.waypoint.thirdparty.ThirdPartyWaypoints waypoints = manager.get(ORIGIN);
            java.util.Map<String, xaero.common.minimap.waypoints.Waypoint> existing = waypoints.getWaypoints();
            for (java.util.Map.Entry<String, TrainMarker> entry : trains.entrySet()) {
                TrainMarker marker = entry.getValue();
                int x = net.minecraft.util.Mth.floor(marker.x());
                int y = net.minecraft.util.Mth.floor(marker.y());
                int z = net.minecraft.util.Mth.floor(marker.z());
                String name = marker.name().isBlank() ? "Create Train" : marker.name();
                xaero.common.minimap.waypoints.Waypoint waypoint = existing.get(entry.getKey());
                if (waypoint == null) {
                    waypoint = new xaero.common.minimap.waypoints.Waypoint(
                            x, y, z, name, "T",
                            xaero.hud.minimap.waypoint.WaypointColor.GOLD,
                            xaero.hud.minimap.waypoint.WaypointPurpose.NORMAL
                    );
                    waypoint.setYIncluded(true);
                    waypoints.add(entry.getKey(), waypoint);
                } else {
                    if (waypoint.getX() != x) waypoint.setX(x);
                    if (waypoint.getY() != y) waypoint.setY(y);
                    if (waypoint.getZ() != z) waypoint.setZ(z);
                    if (!waypoint.getName().equals(name)) waypoint.setName(name);
                }
            }

            for (String existingId : java.util.List.copyOf(existing.keySet())) {
                if (!trains.containsKey(existingId)) {
                    waypoints.remove(existingId);
                }
            }
            hadWaypoints = true;
            lastRevision = revision;
        }

        private static void clear() {
            clearPrevious();
        }

        private static void clearPrevious() {
            if (lastManager != null && hadWaypoints) {
                lastManager.clearOrigin(ORIGIN);
            }
            lastManager = null;
            lastDimension = null;
            hadWaypoints = false;
            lastRevision = -1;
        }
    }
}
