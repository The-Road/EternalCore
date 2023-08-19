package com.road.eternalcore.common.inventory.container.machine;

import com.road.eternalcore.common.inventory.container.ModContainerType;
import com.road.eternalcore.common.inventory.container.slot.BatteryBufferSlot;
import com.road.eternalcore.common.tileentity.BatteryBufferTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class BatteryBufferContainer extends MachineContainer {
    protected final PlayerEntity player;
    protected final BatteryBufferTileEntity container;

    public BatteryBufferContainer(int containerId, PlayerInventory inventory) {
        this(containerId, inventory, new BatteryBufferTileEntity(), new IntArray(3));
    }
    public BatteryBufferContainer(int containerId, PlayerInventory inventory, BatteryBufferTileEntity container, IIntArray guiData) {
        super(ModContainerType.batteryBuffer, containerId, guiData);
        this.player = inventory.player;
        this.container = container;

        initContainerRange(0, 0, 8);

        addInputSlots();
        addResultSlots();
        addBatterySlots();
        addPlayerSlots(inventory, 8, 102);
        addDataSlots(guiData);
    }

    protected void addInputSlots() {}

    protected void addResultSlots() {}

    protected void addBatterySlots() {
        for (int index = batteryStartIndex; index < playerStartIndex; index++) {
            int i = index % 4;
            int j = index / 4;
            addSlot(new BatteryBufferSlot(this, container, index, 53 + i * 18, 54 + j * 18));
        }
    }

    public boolean stillValid(PlayerEntity player) {
        return container.stillValid(player);
    }
}
