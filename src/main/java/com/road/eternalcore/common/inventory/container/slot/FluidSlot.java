package com.road.eternalcore.common.inventory.container.slot;

import com.road.eternalcore.common.inventory.IFluidInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FluidSlot extends Slot {
    public final IFluidInventory fluidContainer;
    public FluidSlot(IFluidInventory fluidContainer, int index, int posX, int posY) {
        super(new Inventory(1), index, posX, posY);
        this.fluidContainer = fluidContainer;
    }

    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }
    public boolean mayPickup(PlayerEntity player) {
        return false;
    }
    public ItemStack getItem() {
        return ItemStack.EMPTY;
    }
    public boolean hasItem() {
        return false;
    }
    public void set(ItemStack itemStack) {

    }
    public void setChanged() {

    }
    public int getMaxStackSize() {
        return 0;
    }
    public int getMaxStackSize(ItemStack itemStack) {
        return 0;
    }

    public FluidStack getFluid(){
        return this.fluidContainer.getFluid(this.getSlotIndex());
    }
}
