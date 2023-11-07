package com.road.eternalcore.common.inventory.container.machine;

import com.road.eternalcore.common.inventory.container.FluidContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;

import javax.annotation.Nullable;

public abstract class MachineContainer extends FluidContainer {
    protected int inputSize;
    protected int resultSize;
    protected int batterySize;

    protected int batteryStartIndex;
    protected int playerStartIndex;
    protected final IIntArray guiData;
    protected MachineContainer(@Nullable ContainerType<?> containerType, int containerId, IIntArray guiData) {
        super(containerType, containerId);
        this.guiData = guiData;
    }

    protected void initContainerRange(int inputSize, int resultSize, int batterySize){
        this.inputSize = inputSize;
        this.resultSize = resultSize;
        this.batterySize = batterySize;
        this.batteryStartIndex = inputSize + resultSize;
        this.playerStartIndex = batteryStartIndex + batterySize;
    }

    abstract protected void addInputSlots();
    abstract protected void addResultSlots();
    abstract protected void addBatterySlots();
    protected void addPlayerSlots(PlayerInventory inventory, int x0, int y0){
        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                addSlot(new Slot(inventory, i1 + k * 9 + 9, x0 + i1 * 18, y0 + k * 18));
            }
        }
        for(int l = 0; l < 9; ++l) {
            addSlot(new Slot(inventory, l, x0 + l * 18, y0 + 58));
        }
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
        // 特殊电池检测，如果这个物品在该机器中不视作电池则返回false
        // 例如合金炉可以把红石粉作为材料，所以快捷移动的时候应该红石移到输入栏而不是电池栏
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
