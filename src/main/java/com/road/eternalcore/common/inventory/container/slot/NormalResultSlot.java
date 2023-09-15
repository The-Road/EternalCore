package com.road.eternalcore.common.inventory.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class NormalResultSlot extends Slot {
    public NormalResultSlot(IInventory inventory, int index, int posX, int posY) {
        super(inventory, index, posX, posY);
    }

    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }
}
