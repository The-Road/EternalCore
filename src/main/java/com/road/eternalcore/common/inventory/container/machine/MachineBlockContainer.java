package com.road.eternalcore.common.inventory.container.machine;

import com.road.eternalcore.common.inventory.container.ModContainerType;
import com.road.eternalcore.common.inventory.container.slot.BatterySlot;
import com.road.eternalcore.common.tileentity.MachineBlockTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class MachineBlockContainer extends MachineContainer {
    protected final PlayerEntity player;
    protected final MachineBlockTileEntity container;

    public MachineBlockContainer(int containerId, PlayerInventory inventory) {
        this(containerId, inventory, new MachineBlockTileEntity(), new IntArray(3));
    }
    public MachineBlockContainer(int containerId, PlayerInventory inventory, MachineBlockTileEntity container, IIntArray guiData) {
        super(ModContainerType.machineBlock, containerId, guiData);
        this.player = inventory.player;
        this.container = container;

        initContainerRange(1, 0, 1);

        addInputSlots();
        addResultSlots();
        addBatterySlots();
        addPlayerSlots(inventory, 8, 102);
        addDataSlots(guiData);
    }
    protected void addInputSlots(){
        for (int i = 0; i < inputSize; i++) {
            addSlot(new Slot(container, i, 80, 36));
        }
    }
    protected void addResultSlots(){
        // 机器方块本身没有输出格
    }
    protected void addBatterySlots(){
        for (int i = batteryStartIndex; i < playerStartIndex; i++) {
            addSlot(new BatterySlot(this, container, i, 80, 72));
        }
    }

    public boolean stillValid(PlayerEntity player) {
        return container.stillValid(player);
    }
}
