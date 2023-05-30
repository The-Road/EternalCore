package com.road.eternalcore.api.energy;

import com.road.eternalcore.api.energy.eu.IEUStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class FEtoEUStorage implements IEnergyStorage {
    private final IEUStorage euStorage;
    public FEtoEUStorage(IEUStorage euStorage){
        this.euStorage = euStorage;
    }

    private int FEtoEU(int fe, boolean isReceive){
        int eu;
        if (isReceive){
            eu = fe >> 3;
        }else{
            eu = (fe - 1) >> 3 + 1;
        }
        return eu;
    }
    private int EUtoFE(int eu){
        int fe = eu << 3;
        return fe;
    }
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int eu = euStorage.receiveEnergy(FEtoEU(maxReceive, true), simulate);
        return EUtoFE(eu);
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        int eu = euStorage.extractEnergy(FEtoEU(maxExtract, false), simulate);
        return EUtoFE(eu);
    }

    public int getEnergyStored() {
        return EUtoFE(euStorage.getEnergyStored());
    }

    public int getMaxEnergyStored() {
        return EUtoFE(euStorage.getMaxEnergyStored());
    }

    public boolean canExtract() {
        return euStorage.canExtract();
    }

    public boolean canReceive() {
        return euStorage.canReceive();
    }
}
