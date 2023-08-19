package com.road.eternalcore.common.tileentity.wrapper;

import com.road.eternalcore.common.tileentity.EnergyMachineTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

public class EnergyMachineWrapper extends InvWrapper {
    protected final EnergyMachineTileEntity machine;
    public EnergyMachineWrapper(EnergyMachineTileEntity machine) {
        super(machine);
        this.machine = machine;
    }
    public ItemStack extractItem(int slot, int amount, boolean simulate){
        ItemStack stackInSlot = getInv().getItem(slot);
        if (!machine.canTakeItem(slot, stackInSlot)){
            return ItemStack.EMPTY;
        }
        return super.extractItem(slot, amount, simulate);
    }
}
