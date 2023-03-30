package com.road.eternalcore.api.energy.eu;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemEUStorage implements IEnergyStorage {
    // 在物品中存储能量的NBT标签
    private final String StorageNBTName = "EU_energy";
    private ItemStack itemStack;
    private boolean canExtract = true;
    private boolean canReceive = true;
    private int maxStorage = 0;
    private int voltage = 32;

    public ItemEUStorage(ItemStack itemStack){
        this.itemStack = itemStack;
    }
    public ItemEUStorage set(int maxStorage, int voltage){
        this.maxStorage = maxStorage;
        this.voltage = voltage;
        return this;
    }
    public ItemEUStorage inputOnly(){
        this.canExtract = false;
        this.canReceive = true;
        return this;
    }
    public ItemEUStorage outputOnly(){
        this.canExtract = true;
        this.canReceive = false;
        return this;
    }
    // 设置能量
    public int setEnergy(int energy){
        return Math.max(0, Math.min(energy, this.maxStorage));
    }
    public int addEnergy(int energyBefore, int eu){
        return setEnergy(energyBefore + eu);
    }
    // 保存到NBT中
    public void saveEnergy(int energy){
        itemStack.getOrCreateTag().putInt(StorageNBTName, energy);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energy = getEnergyStored();
        int energyNew = addEnergy(energy, maxReceive);
        if (!simulate){
            saveEnergy(energyNew);
        }
        return energyNew - energy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = getEnergyStored();
        int energyNew = addEnergy(energy, -maxExtract);
        if (!simulate){
            saveEnergy(energyNew);
        }
        return energyNew - energy;
    }

    @Override
    public int getEnergyStored() {
        if (itemStack.hasTag()){
            int energy = itemStack.getTag().getInt(StorageNBTName);
            return setEnergy(energy);
        }
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.maxStorage;
    }

    @Override
    public boolean canExtract() {
        return this.canExtract;
    }

    @Override
    public boolean canReceive() {
        return this.canReceive;
    }
}
