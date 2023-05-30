package com.road.eternalcore.common.inventory.container;

import com.road.eternalcore.common.inventory.container.slot.BatterySlot;
import com.road.eternalcore.common.tileentity.MachineBlockTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class MachineBlockContainer extends Container {
    protected final PlayerEntity player;
    protected final MachineBlockTileEntity container;
    protected final IIntArray guiData;
    public MachineBlockContainer(int containerId, PlayerInventory inventory) {
        this(containerId, inventory, new MachineBlockTileEntity(), new IntArray(2));
    }
    public MachineBlockContainer(int containerId, PlayerInventory inventory, MachineBlockTileEntity container, IIntArray guiData) {
        super(ModContainerType.machineBlock, containerId);
        this.player = inventory.player;
        this.container = container;
        this.guiData = guiData;

        addInputSlots();
        addResultSlots();
        addBatterySlots();
        addPlayerSlots(inventory);
        addDataSlots(guiData);
    }
    protected void addInputSlots(){
        addSlot(new Slot(container, container.getInputRange()[0], 80, 36));
    }
    protected void addResultSlots(){
        // 机器方块本身没有输出格
    }
    protected void addBatterySlots(){
        addSlot(new BatterySlot(container, container.getBatteryRange()[0], 80, 72));
    }
    protected void addPlayerSlots(PlayerInventory inventory){
        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                addSlot(new Slot(inventory, i1 + k * 9 + 9, 8 + i1 * 18, 102 + k * 18));
            }
        }
        for(int l = 0; l < 9; ++l) {
            addSlot(new Slot(inventory, l, 8 + l * 18, 160));
        }
    }

    public boolean stillValid(PlayerEntity player) {
        return container.stillValid(player);
    }

    public ItemStack quickMoveStack(PlayerEntity player, int slotId) {
        ItemStack itemStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack = slot.getItem();
            itemStackCopy = itemStack.copy();
        }
        return ItemStack.EMPTY;
    }

    // 用于GUI描绘的数据
    public int getEnergy(){
        return guiData.get(0);
    }
    public int getMaxEnergy(){
        return guiData.get(1);
    }
}
