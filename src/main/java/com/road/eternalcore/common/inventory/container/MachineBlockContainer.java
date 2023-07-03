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
    protected final int inputSize = 1;
    protected final int resultSize = 0;
    protected final int batterySize = 1;

    protected int batteryStartIndex;
    protected int playerStartIndex;
    public MachineBlockContainer(int containerId, PlayerInventory inventory) {
        this(containerId, inventory, new MachineBlockTileEntity(), new IntArray(3));
    }
    public MachineBlockContainer(int containerId, PlayerInventory inventory, MachineBlockTileEntity container, IIntArray guiData) {
        super(ModContainerType.machineBlock, containerId);
        this.player = inventory.player;
        this.container = container;
        this.guiData = guiData;

        calculateSlotIndex();

        addInputSlots();
        addResultSlots();
        addBatterySlots();
        addPlayerSlots(inventory);
        addDataSlots(guiData);
    }

    protected void calculateSlotIndex(){
        this.batteryStartIndex = inputSize + resultSize;
        this.playerStartIndex = batteryStartIndex + batterySize;
    }
    protected void addInputSlots(){
        for (int i=0; i < inputSize; i++) {
            addSlot(new Slot(container, i, 80, 36));
        }
    }
    protected void addResultSlots(){
        // 机器方块本身没有输出格
    }
    protected void addBatterySlots(){
        for (int i=batteryStartIndex; i < playerStartIndex; i++) {
            addSlot(new BatterySlot(container, i, 80, 72));
        }
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
            if (slotId >= inputSize && slotId < batteryStartIndex){
                // 输出槽的物品，移动到背包
                if (!this.moveItemStackTo(itemStack, playerStartIndex, playerStartIndex + 36, true)){
                    return ItemStack.EMPTY;
                }
            } else if (slotId >= playerStartIndex && slotId < playerStartIndex + 36){
                // 背包里的物品，如果是电池，那么将电池移动到电池槽
                if ((checkBatteryExtra(itemStack) && !this.moveItemStackTo(itemStack, batteryStartIndex, batteryStartIndex + batterySize, false))
                        // 不是电池则移动到输入槽
                        && (!this.moveItemStackTo(itemStack, 0, inputSize, false))
                ){
                    // 无法移动到机器内则在背包和快捷栏中切换
                    if (slotId < playerStartIndex + 27){
                        if (!this.moveItemStackTo(itemStack, playerStartIndex + 27, playerStartIndex + 36, false)){
                            return ItemStack.EMPTY;
                        }
                    } else {
                        if (!this.moveItemStackTo(itemStack, playerStartIndex, playerStartIndex + 27, false)){
                            return ItemStack.EMPTY;
                        }
                    }
                }
            } else if (!this.moveItemStackTo(itemStack, playerStartIndex, playerStartIndex + 36, false)){
                // 输入槽或电池槽的物品，移动到背包
                return ItemStack.EMPTY;
            }

            if (itemStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemStack.getCount() == itemStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemStack);
        }
        return itemStackCopy;
    }

    protected boolean checkBatteryExtra(ItemStack itemStack){
        // 特殊电池检测，例如合金炉可以把红石粉作为材料，所以快捷移动的时候会把红石移到输入栏而不是电池栏
        return true;
    }

    // 用于GUI描绘的数据
    public int getEnergy(){
        return guiData.get(0);
    }
    public int getMaxEnergy(){
        return guiData.get(1);
    }
    public int getTierLevel(){
        return guiData.get(2);
    }
}
