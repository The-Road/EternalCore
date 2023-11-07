package com.road.eternalcore.common.inventory;

import net.minecraft.inventory.IClearable;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidInventory extends IClearable {
    int getFluidContainerSize();
    boolean isFluidEmpty();
    FluidStack getFluid(int i);
    FluidStack removeFluid(int i, int amount);
}
