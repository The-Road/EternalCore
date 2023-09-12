package com.road.eternalcore.api.energy.eu;

import net.minecraft.util.Direction;

public class SidedEUStorage implements IEUStorage{
    // 在机器中实现，用于在不同方向上区分输入和输出端
    private final ISidedEUStorage storage;
    private final Direction side;
    public SidedEUStorage(ISidedEUStorage storage, Direction side){
        this.storage = storage;
        this.side = side;
    }
    public void saveEnergy(int energy) {
        storage.saveEnergy(energy);
    }

    public EUTier getTier() {
        return storage.getTier();
    }

    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }

    public boolean canExtract() {
        return storage.canExtract(side);
    }

    public boolean canReceive() {
        return storage.canReceive(side);
    }
}
