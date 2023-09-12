package com.road.eternalcore.api.energy.eu;

import net.minecraft.item.ItemStack;

public class ItemEUStorage implements IEUStorage {
    // 在物品中存储能量的NBT标签
    private final String StorageNBTName = "EU_energy";
    private ItemStack itemStack;
    private boolean canExtract = true;
    private boolean canReceive = true;
    private int maxStorage = 0;
    private EUTier euTier = EUTier.LV;

    public ItemEUStorage(ItemStack itemStack){
        this.itemStack = itemStack;
    }
    public ItemEUStorage set(int maxStorage, EUTier euTier){
        this.maxStorage = maxStorage;
        this.euTier = euTier;
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

    public EUTier getTier() {
        return euTier;
    }

    public void saveEnergy(int energy){
        itemStack.getOrCreateTag().putInt(StorageNBTName, setEnergy(energy));
        System.out.println("set energy "+energy);
    }

    public int getEnergyStored() {
        if (itemStack.hasTag()){
            int energy = itemStack.getTag().getInt(StorageNBTName);
            return setEnergy(energy);
        }
        return 0;
    }

    public int getMaxEnergyStored() {
        return this.maxStorage;
    }

    public boolean canExtract() {
        return this.canExtract;
    }

    public boolean canReceive() {
        return this.canReceive;
    }
}
