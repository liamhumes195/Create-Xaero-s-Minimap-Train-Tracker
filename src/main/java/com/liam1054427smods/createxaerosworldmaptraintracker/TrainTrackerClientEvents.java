package com.liam1054427smods.createxaerosworldmaptraintracker;

import com.simibubi.create.Create;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = CreateXaerosWorldMapTrainTracker.MODID, value = Dist.CLIENT)
public final class TrainTrackerClientEvents {
    private static final int UPDATE_INTERVAL_TICKS = 10;
    private static int ticksUntilUpdate;

    private TrainTrackerClientEvents() {
    }

    @SubscribeEvent
    public static void onDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientTrainData.clear();
        XaeroWaypointIntegration.clear();
        ticksUntilUpdate = 0;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (++ticksUntilUpdate >= UPDATE_INTERVAL_TICKS) {
            ticksUntilUpdate = 0;
            updateLocalSnapshot();
        }
        XaeroWaypointIntegration.refresh();
    }

    private static void updateLocalSnapshot() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            ClientTrainData.setLocalMarkers(java.util.Map.of());
            return;
        }

        if (!TrackerSideConfig.TRACKING_SIDE.get().tracksClient()) {
            ClientTrainData.setLocalMarkers(java.util.Map.of());
            return;
        }

        ClientTrainData.setLocalMarkers(CreateTrainReader.snapshot(
                Create.RAILWAYS.sided(minecraft.level)
        ));
    }
}
