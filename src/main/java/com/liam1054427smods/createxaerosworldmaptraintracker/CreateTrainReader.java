package com.liam1054427smods.createxaerosworldmaptraintracker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.GlobalRailwayManager;
import com.simibubi.create.content.trains.entity.Train;

import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;

final class CreateTrainReader {
    private CreateTrainReader() {
    }

    static Map<UUID, TrainMarker> snapshot(GlobalRailwayManager railwayManager) {
        if (!ModList.get().isLoaded(CreateXaerosWorldMapTrainTracker.CREATE_MODID)) {
            return Map.of();
        }

        Map<UUID, TrainMarker> markers = new HashMap<>();
        for (Train train : railwayManager.trains.values()) {
            if (train.invalid || train.graph == null || train.carriages.isEmpty()) {
                continue;
            }

            var leadingPoint = train.carriages.getFirst().getLeadingPoint();
            if (leadingPoint.node1 == null || leadingPoint.edge == null) {
                continue;
            }

            Vec3 position = leadingPoint.getPosition(train.graph);
            if (!Double.isFinite(position.x) || !Double.isFinite(position.y) || !Double.isFinite(position.z)) {
                continue;
            }

            String name = train.name.getString();
            if (name.isBlank()) {
                name = "Unnamed train";
            }

            markers.put(train.id, new TrainMarker(
                    train.id,
                    leadingPoint.node1.getLocation().dimension.location(),
                    name,
                    position.x,
                    position.y,
                    position.z
            ));
        }
        return markers;
    }
}
