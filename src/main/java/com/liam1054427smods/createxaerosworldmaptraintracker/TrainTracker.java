package com.liam1054427smods.createxaerosworldmaptraintracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = CreateXaerosWorldMapTrainTracker.MODID)
public final class TrainTracker {
    private static final int UPDATE_INTERVAL_TICKS = 10;
    private static final double MINIMUM_MOVEMENT_SQUARED = 0.75 * 0.75;
    private static final Map<UUID, TrainMarker> LAST_SENT = new HashMap<>();
    private static int ticksUntilUpdate;

    private TrainTracker() {
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (++ticksUntilUpdate < UPDATE_INTERVAL_TICKS) {
            return;
        }
        ticksUntilUpdate = 0;
        updateAndBroadcast();
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new TrainUpdatePayload(
                    new ArrayList<>(LAST_SENT.values()),
                    List.of()
            ));
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        LAST_SENT.clear();
        ticksUntilUpdate = 0;
    }

    private static void updateAndBroadcast() {
        Map<UUID, TrainMarker> current = CreateTrainReader.snapshot();
        List<TrainMarker> upserts = new ArrayList<>();
        List<UUID> removals = new ArrayList<>();

        for (TrainMarker marker : current.values()) {
            TrainMarker previous = LAST_SENT.get(marker.id());
            if (!isUnchanged(previous, marker)) {
                upserts.add(marker);
                LAST_SENT.put(marker.id(), marker);
            }
        }

        for (UUID knownId : List.copyOf(LAST_SENT.keySet())) {
            if (!current.containsKey(knownId)) {
                removals.add(knownId);
                LAST_SENT.remove(knownId);
            }
        }

        if (!upserts.isEmpty() || !removals.isEmpty()) {
            PacketDistributor.sendToAllPlayers(new TrainUpdatePayload(upserts, removals));
        }
    }

    private static boolean isUnchanged(TrainMarker previous, TrainMarker current) {
        if (previous == null
                || !previous.dimension().equals(current.dimension())
                || !previous.name().equals(current.name())) {
            return false;
        }

        double dx = current.x() - previous.x();
        double dy = current.y() - previous.y();
        double dz = current.z() - previous.z();
        return dx * dx + dy * dy + dz * dz < MINIMUM_MOVEMENT_SQUARED;
    }
}
