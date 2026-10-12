package com.liam1054427smods.createxaerosworldmaptraintracker;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@Mod(value = CreateXaerosWorldMapTrainTracker.MODID, dist = Dist.CLIENT)
public class CreateXaerosWorldMapTrainTrackerClient {
    public CreateXaerosWorldMapTrainTrackerClient(IEventBus modEventBus) {
        modEventBus.addListener(this::registerGuiLayers);
    }

    private void registerGuiLayers(RegisterGuiLayersEvent event) {
        TrainTrackerOverlay.registerOverlay(event);
    }
}
