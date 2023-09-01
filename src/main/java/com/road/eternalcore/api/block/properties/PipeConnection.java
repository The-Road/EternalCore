package com.road.eternalcore.api.block.properties;

import net.minecraft.util.IStringSerializable;

public enum PipeConnection implements IStringSerializable {
    OFF("off"),
    ON("on"),
    CUT("cut");

    private final String name;

    PipeConnection(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        return this.name;
    }

    public boolean isConnected() {
        return this == ON;
    }

}
