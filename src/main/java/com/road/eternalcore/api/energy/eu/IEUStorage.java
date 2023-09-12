package com.road.eternalcore.api.energy.eu;

import net.minecraftforge.energy.IEnergyStorage;

public interface IEUStorage extends IEnergyStorage, IEUTier{
    // EU能量接口
    void saveEnergy(int energy);

    default boolean isEnergyFull(){
        return getEnergyStored() >= getMaxEnergyStored();
    }
    default boolean isEnergyEmpty(){
        return getEnergyStored() <= 0;
    }
    default int setEnergy(int energy){
        return Math.max(0, Math.min(energy, getMaxEnergyStored()));
    }
    default int forceSetEnergy(int energy){
        return Math.max(0, energy);
    }
    default int addEnergy(int energyBefore, int eu){
        return setEnergy(energyBefore + eu);
    }
    default int forceAddEnergy(int energyBefore, int eu){
        return forceSetEnergy(energyBefore + eu);
    }

    @Override
    default int receiveEnergy(int maxReceive, boolean simulate){
        return receiveEnergy(maxReceive, simulate, false);
    }
    default int receiveEnergy(int maxReceive, boolean simulate, boolean force) {
        if (!canReceive() || (!force && isEnergyFull()))
            return 0;
        int energy = getEnergyStored();
        int energyNew = force ? forceAddEnergy(energy, maxReceive) : addEnergy(energy, maxReceive);
        if (!simulate)
            saveEnergy(energyNew);
        return energyNew - energy;
    }

    @Override
    default int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract() || isEnergyEmpty())
            return 0;
        int energy = getEnergyStored();
        int energyNew = forceAddEnergy(energy, -maxExtract);
        if (!simulate)
            saveEnergy(energyNew);
        return energyNew - energy;
    }
}
