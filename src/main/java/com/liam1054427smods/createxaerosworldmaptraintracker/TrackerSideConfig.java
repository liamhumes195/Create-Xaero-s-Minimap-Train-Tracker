package com.liam1054427smods.createxaerosworldmaptraintracker;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class TrackerSideConfig {
    public static final ModConfigSpec.EnumValue<TrackerSide> TRACKING_SIDE;
    public static final ModConfigSpec SPEC;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        TRACKING_SIDE = builder
                .comment(
                        "Select where Create trains are read:",
                        "SERVER uses authoritative server data and synchronizes it to clients.",
                        "CLIENT reads the local client's Create railway mirror without server tracking.",
                        "BOTH reads on both sides; matching trains are deduplicated by UUID."
                )
                .translation("config.createxaerosworldmaptraintracker.tracking_side")
                .defineEnum("tracking_side", TrackerSide.SERVER);
        SPEC = builder.build();
    }

    private TrackerSideConfig() {
    }
}
