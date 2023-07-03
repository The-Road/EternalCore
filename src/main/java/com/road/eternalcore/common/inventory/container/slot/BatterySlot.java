package com.road.eternalcore.common.inventory.container.slot;

import com.road.eternalcore.api.energy.CapEnergy;
import com.road.eternalcore.api.energy.DisposableBattery;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class BatterySlot extends Slot {
    public BatterySlot(IInventory inventory, int index, int posX, int posY) {
        super(inventory, index, posX, posY);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        if (itemStack.getCapability(CapEnergy.EU).isPresent()) {
            return true;
        }
        DisposableBattery battery = DisposableBattery.get(itemStack.getItem());
        return battery != null;
    }
}
