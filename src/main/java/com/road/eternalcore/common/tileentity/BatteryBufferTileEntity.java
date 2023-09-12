package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.api.energy.CapEnergy;
import com.road.eternalcore.api.energy.EnergyUtils;
import com.road.eternalcore.api.energy.eu.IEUStorage;
import com.road.eternalcore.common.inventory.container.machine.BatteryBufferContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class BatteryBufferTileEntity extends EnergyMachineTileEntity implements IEnergyProviderTileEntity, IEnergyReceiverTileEntity{
    private static final ITextComponent TITLE = new TranslationTextComponent("container.eternalcore.battery_buffer");
    protected int providerBatteryCount = 0;
    protected int receiverBatteryCount = 0;
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

    public void machineTickWork(){
        // 获取电池的状况决定最大输入/输出电流，同时将溢出的能量平均分配给所有未满的电池
        providerBatteryCount = 0;
        receiverBatteryCount = 0;
        List<IEUStorage> chargeList = new ArrayList<>();
        for (ItemStack itemStack : items){
            IEUStorage storage = itemStack.getCapability(CapEnergy.EU).orElse(null);
            if (storage != null){
                if (storage.canExtract()){
                    providerBatteryCount++;
                }
                if (storage.canReceive() && !storage.isEnergyFull()){
                    receiverBatteryCount++;
                    chargeList.add(storage);
                }
            }
        }
        int remainEnergy = getEnergyStored() - getMaxEnergyStored();
        if (remainEnergy > 0 && !chargeList.isEmpty()) {
            remainEnergy /= chargeList.size();
            for (IEUStorage storage : chargeList) {
                EnergyUtils.energyExchange(this, storage, remainEnergy, false);
            }
        }
    }
    public boolean canExtract(Direction side) {
        return covers.getFace() == side;
    }
    public boolean canReceive(Direction side) {
        return covers.getFace() != side;
    }
    public int maxProvideCurrent() {
        return Math.max(providerBatteryCount, 1);
    }
    public int maxReceiveCurrent() {
        return Math.max(receiverBatteryCount, 1);
    }

    public boolean canReceiveEnergyFromNetwork(){
        return !isEnergyFull() || receiverBatteryCount > 0;
    }
}
