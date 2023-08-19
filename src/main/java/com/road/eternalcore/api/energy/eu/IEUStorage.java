package com.road.eternalcore.api.energy.eu;

import net.minecraftforge.energy.IEnergyStorage;

public interface IEUStorage extends IEnergyStorage, IEUTier{
    // EU能量接口
    void saveEnergy(int energy);

    default boolean isEnergyFull(){
        return getEnergyStored() == getMaxEnergyStored();
    }
    default boolean isEnergyEmpty(){
        return getEnergyStored() == 0;
    }
    default int setEnergy(int energy){
        return Math.max(0, Math.min(energy, getMaxEnergyStored()));
    }
    default int addEnergy(int energyBefore, int eu){
        return setEnergy(energyBefore + eu);
    }

    @Override
    default int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;
        int energy = getEnergyStored();
        int energyNew = addEnergy(energy, maxReceive);
        if (!simulate)
            saveEnergy(energyNew);
        return energyNew - energy;
    }

    @Override
    default int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;
        int energy = getEnergyStored();
        int energyNew = addEnergy(energy, -maxExtract);
        if (!simulate)
            saveEnergy(energyNew);
        return energyNew - energy;
    }
}
