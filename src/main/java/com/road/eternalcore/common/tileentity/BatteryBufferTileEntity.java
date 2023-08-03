package com.road.eternalcore.common.tileentity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class BatteryBufferTileEntity extends EnergyMachineTileEntity{
    public BatteryBufferTileEntity() {
        super(ModTileEntityType.batteryBuffer);
    }

    protected NonNullList<ItemStack> getItems() {
        return null;
    }

    protected void setItems(NonNullList<ItemStack> p_199721_1_) {

    }

    protected ITextComponent getDefaultName() {
        return null;
    }

    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return null;
    }

    public int getContainerSize() {
        return 0;
    }

    public boolean canExtract() {
        return false;
    }

    public boolean canReceive() {
        return false;
    }
}
