package com.road.eternalcore.common.inventory.container.slot;

import com.road.eternalcore.api.energy.EnergyUtils;
import com.road.eternalcore.common.inventory.container.machine.MachineContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class BatterySlot extends Slot {
    protected MachineContainer container;
    public BatterySlot(MachineContainer container, IInventory inventory, int index, int posX, int posY) {
        super(inventory, index, posX, posY);
        this.container = container;
    }

    public boolean mayPlace(ItemStack itemStack) {
        return EnergyUtils.checkBatteryValid(itemStack, container.getTierLevel());
    }
}
