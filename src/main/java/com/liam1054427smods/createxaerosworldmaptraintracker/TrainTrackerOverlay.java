package com.liam1054427smods.createxaerosworldmaptraintracker;

import java.util.Comparator;
import java.util.List;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

public final class TrainTrackerOverlay {
    private static final int MAX_VISIBLE_TRAINS = 5;
    private static final ResourceLocation OVERLAY_ID = ResourceLocation.fromNamespaceAndPath(
            CreateXaerosWorldMapTrainTracker.MODID,
            "train_tracker"
    );

    private TrainTrackerOverlay() {
    }

    public static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAboveAll(OVERLAY_ID, TrainTrackerOverlay::render);
    }

    private static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        ResourceLocation currentDimension = minecraft.player.level().dimension().location();
        List<TrainMarker> trains = ClientTrainData.markers().stream()
                .filter(marker -> marker.dimension().equals(currentDimension))
                .sorted(Comparator.comparingDouble(marker ->
                        minecraft.player.distanceToSqr(marker.x(), marker.y(), marker.z())))
                .limit(MAX_VISIBLE_TRAINS)
                .toList();
        if (trains.isEmpty()) {
            return;
        }

        int panelBottom = 8 + (trains.size() + 1) * 11;
        graphics.fill(4, 4, 220, panelBottom, 0x90000000);
        graphics.drawString(
                minecraft.font,
                Component.translatable("gui.createxaerosworldmaptraintracker.train_count", trains.size()),
                8,
                8,
                0xFFFFFF,
                true
        );

        for (int index = 0; index < trains.size(); index++) {
            TrainMarker marker = trains.get(index);
            String coordinates = "%s  %d, %d, %d".formatted(
                    marker.name(),
                    (int) Math.floor(marker.x()),
                    (int) Math.floor(marker.y()),
                    (int) Math.floor(marker.z())
            );
            graphics.drawString(minecraft.font, coordinates, 8, 19 + index * 11, 0xFFFFFF, true);
        }
    }
}
