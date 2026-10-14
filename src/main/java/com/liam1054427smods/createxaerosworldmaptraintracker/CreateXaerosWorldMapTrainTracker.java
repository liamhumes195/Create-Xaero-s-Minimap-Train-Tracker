package com.liam1054427smods.createxaerosworldmaptraintracker;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

@Mod(CreateXaerosWorldMapTrainTracker.MODID)
public class CreateXaerosWorldMapTrainTracker {
    public static final String MODID = "createxaerosworldmaptraintracker";
    public static final String CREATE_MODID = "create";

    public CreateXaerosWorldMapTrainTracker(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(TrainUpdatePayload::register);
        modContainer.registerConfig(ModConfig.Type.SERVER, TrackerSideConfig.SPEC);
    }
}
