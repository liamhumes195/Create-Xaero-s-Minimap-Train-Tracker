package com.liam1054427smods.createxaerosworldmaptraintracker;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(CreateXaerosWorldMapTrainTracker.MODID)
public class CreateXaerosWorldMapTrainTracker {
    public static final String MODID = "createxaerosworldmaptraintracker";
    public static final String CREATE_MODID = "create";

    public CreateXaerosWorldMapTrainTracker(IEventBus modEventBus) {
        modEventBus.addListener(TrainUpdatePayload::register);
    }
}
