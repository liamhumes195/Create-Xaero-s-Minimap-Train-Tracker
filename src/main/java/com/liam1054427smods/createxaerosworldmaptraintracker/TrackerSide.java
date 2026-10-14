package com.liam1054427smods.createxaerosworldmaptraintracker;

public enum TrackerSide {
    SERVER,
    CLIENT,
    BOTH;

    public boolean tracksServer() {
        return this != CLIENT;
    }

    public boolean tracksClient() {
        return this != SERVER;
    }
}
