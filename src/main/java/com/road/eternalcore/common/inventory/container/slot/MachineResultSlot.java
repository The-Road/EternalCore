package com.road.eternalcore.common.inventory.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class MachineResultSlot extends Slot {
    public MachineResultSlot(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }
}
