package com.liam1054427smods.createxaerosworldmaptraintracker;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = CreateXaerosWorldMapTrainTracker.MODID, value = Dist.CLIENT)
public final class TrainTrackerClientEvents {
    private TrainTrackerClientEvents() {
    }

    @SubscribeEvent
    public static void onDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientTrainData.clear();
        XaeroWaypointIntegration.clear();
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        XaeroWaypointIntegration.refresh();
    }
}
