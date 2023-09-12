package com.road.eternalcore.api.energy.eu;

import net.minecraft.util.Direction;

public interface ISidedEUStorage extends IEUStorage{
    boolean canExtract(Direction side);
    boolean canReceive(Direction side);
    default boolean canExtract() {
        return true;
    }
    default boolean canReceive() {
        return true;
    }
}
