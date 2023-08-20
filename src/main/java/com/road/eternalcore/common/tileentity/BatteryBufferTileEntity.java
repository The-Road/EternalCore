package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.api.energy.CapEnergy;
import com.road.eternalcore.api.energy.EnergyUtils;
import com.road.eternalcore.api.energy.eu.IEUStorage;
import com.road.eternalcore.common.inventory.container.machine.BatteryBufferContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BatteryBufferTileEntity extends EnergyMachineTileEntity{
    private static final ITextComponent TITLE = new TranslationTextComponent("container.eternalcore.battery_buffer");
    public BatteryBufferTileEntity() {
        super(ModTileEntityType.batteryBuffer, 0, 0, 8);
    }

    protected ITextComponent getDefaultName() {
        return TITLE;
    }
    // 电池箱内不可堆叠
    public int getMaxStackSize() {
        return 1;
    }
    protected Container createMenu(int containerId, PlayerInventory playerInventory) {
        return new BatteryBufferContainer(containerId, playerInventory, this, this.guiData);
    }
    public boolean canPlaceItem(int slotId, ItemStack itemStack){
        return EnergyUtils.checkChargeableBatteryValid(itemStack, getTier().getLevel());
    }
    public boolean canTakeItem(int slotID, ItemStack itemStack){
        // 可以抽走空的不可充电电池，或者充满电的工具
        IEUStorage storage = itemStack.getCapability(CapEnergy.EU).orElse(null);
        if (storage != null) {
            return (!storage.canReceive() && storage.isEnergyEmpty()) ||
                    (!storage.canExtract() && storage.isEnergyFull());
        }
        return true;
    }
}
