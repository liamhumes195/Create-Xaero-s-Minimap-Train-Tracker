package com.liam1054427smods.createxaerosworldmaptraintracker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public record TrainUpdatePayload(List<TrainMarker> upserts, List<UUID> removals) implements CustomPacketPayload {
    public static final Type<TrainUpdatePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(CreateXaerosWorldMapTrainTracker.MODID, "train_update")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TrainUpdatePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, TrainMarker.STREAM_CODEC), TrainUpdatePayload::upserts,
            ByteBufCodecs.collection(ArrayList::new, UUIDUtil.STREAM_CODEC), TrainUpdatePayload::removals,
            TrainUpdatePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(TYPE, STREAM_CODEC, TrainUpdatePayload::handle);
    }

    private static void handle(TrainUpdatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> ClientTrainData.apply(payload));
    }
}
