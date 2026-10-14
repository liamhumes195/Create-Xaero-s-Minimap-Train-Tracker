package com.liam1054427smods.createxaerosworldmaptraintracker;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ClientTrainData {
    private static final Map<UUID, TrainMarker> SERVER_MARKERS = new HashMap<>();
    private static final Map<UUID, TrainMarker> LOCAL_MARKERS = new HashMap<>();
    private static List<TrainMarker> combinedMarkers = List.of();
    private static long revision;

    private ClientTrainData() {
    }

    static void apply(TrainUpdatePayload payload) {
        boolean changed = false;
        for (UUID id : payload.removals()) {
            if (SERVER_MARKERS.remove(id) != null) {
                changed = true;
            }
        }
        for (TrainMarker marker : payload.upserts()) {
            if (!marker.equals(SERVER_MARKERS.put(marker.id(), marker))) {
                changed = true;
            }
        }
        if (changed) {
            refreshCombinedMarkers();
            revision++;
        }
        XaeroWaypointIntegration.refresh();
    }

    static void setLocalMarkers(Map<UUID, TrainMarker> markers) {
        if (!LOCAL_MARKERS.equals(markers)) {
            LOCAL_MARKERS.clear();
            LOCAL_MARKERS.putAll(markers);
            refreshCombinedMarkers();
            revision++;
        }
    }

    static long revision() {
        return revision;
    }

    static Collection<TrainMarker> markers() {
        return combinedMarkers;
    }

    static void clear() {
        SERVER_MARKERS.clear();
        LOCAL_MARKERS.clear();
        combinedMarkers = List.of();
        revision++;
    }

    private static void refreshCombinedMarkers() {
        Map<UUID, TrainMarker> combined = new HashMap<>(LOCAL_MARKERS);
        combined.putAll(SERVER_MARKERS);
        combinedMarkers = List.copyOf(combined.values());
    }
}
