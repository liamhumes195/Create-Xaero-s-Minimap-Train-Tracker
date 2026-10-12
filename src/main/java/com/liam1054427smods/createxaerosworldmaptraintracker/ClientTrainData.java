package com.liam1054427smods.createxaerosworldmaptraintracker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ClientTrainData {
    private static final Map<UUID, TrainMarker> MARKERS = new HashMap<>();
    private static long revision;

    private ClientTrainData() {
    }

    static void apply(TrainUpdatePayload payload) {
        payload.removals().forEach(MARKERS::remove);
        payload.upserts().forEach(marker -> MARKERS.put(marker.id(), marker));
        revision++;
        XaeroWaypointIntegration.refresh();
    }

    static long revision() {
        return revision;
    }

    static Collection<TrainMarker> markers() {
        return MARKERS.values();
    }

    static void clear() {
        MARKERS.clear();
        revision++;
    }
}
