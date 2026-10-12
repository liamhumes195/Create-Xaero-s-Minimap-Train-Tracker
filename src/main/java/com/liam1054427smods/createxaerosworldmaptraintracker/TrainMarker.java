package com.liam1054427smods.createxaerosworldmaptraintracker;

import java.util.UUID;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record TrainMarker(
        UUID id,
        ResourceLocation dimension,
        String name,
        double x,
        double y,
        double z
) {
    public static final StreamCodec<RegistryFriendlyByteBuf, TrainMarker> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, TrainMarker::id,
            ResourceLocation.STREAM_CODEC, TrainMarker::dimension,
            ByteBufCodecs.STRING_UTF8, TrainMarker::name,
            ByteBufCodecs.DOUBLE, TrainMarker::x,
            ByteBufCodecs.DOUBLE, TrainMarker::y,
            ByteBufCodecs.DOUBLE, TrainMarker::z,
            TrainMarker::new
    );
}
