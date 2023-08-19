package com.road.eternalcore.common.inventory.container.slot;

import com.road.eternalcore.api.energy.EnergyUtils;
import com.road.eternalcore.common.inventory.container.machine.MachineContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class BatteryBufferSlot extends BatterySlot{
    // 电池箱的电池槽，不支持消耗类的一次性电池
    public BatteryBufferSlot(MachineContainer container, IInventory inventory, int index, int posX, int posY) {
        super(container, inventory, index, posX, posY);
    }

    public boolean mayPlace(ItemStack itemStack) {
        return EnergyUtils.checkChargeableBatteryValid(itemStack, container.getTierLevel());
    }
}
