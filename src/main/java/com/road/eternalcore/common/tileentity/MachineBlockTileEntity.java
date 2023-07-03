package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.api.energy.CapEnergy;
import com.road.eternalcore.api.energy.DisposableBattery;
import com.road.eternalcore.api.energy.EnergyUtils;
import com.road.eternalcore.common.inventory.IMachineBlockInventory;
import com.road.eternalcore.common.inventory.container.MachineBlockContainer;
import com.road.eternalcore.common.tileentity.data.EnergyMachineGUIData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;

public class MachineBlockTileEntity extends EnergyMachineTileEntity implements IMachineBlockInventory, ITickableTileEntity{
    private static final ITextComponent TITLE = new TranslationTextComponent("container.eternalcore.machine_block");

    protected NonNullList<ItemStack> items;
    protected final EnergyMachineGUIData guiData = new EnergyMachineGUIData(this);
    public MachineBlockTileEntity(int containerSize){
        super(ModTileEntityType.machineBlock);
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    }
    public MachineBlockTileEntity(){
        this(1);
    }
    // IMachineBlockInventory接口
    public int getInputSize() {
        return 1;
    }
    public int getResultSize(){
        return 0;
    }
    public int getBatterySize(){
        return 1;
    }
    // 保存/读取物品栏
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        if (!trySaveLootTable(nbt)) {
            ItemStackHelper.saveAllItems(nbt, this.items);
        }
        return nbt;
    }
    public void load(BlockState blockState, CompoundNBT nbt) {
        super.load(blockState, nbt);
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!tryLoadLootTable(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.items);
        }
    }
    // IInventory接口
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }
    protected ITextComponent getDefaultName(){
        return TITLE;
    }
    protected Container createMenu(int containerId, PlayerInventory playerInventory) {
        return new MachineBlockContainer(containerId, playerInventory, this, this.guiData);
    }
    //Energy接口
    public boolean canExtract() {
        return true;
    }
    public boolean canReceive() {
        return true;
    }
    // ITickableTileEntity接口
    public void tick() {
        // 充电检查
        tickEnergyCharge();
    }
    protected void tickEnergyCharge(){
        // 检查电池格中的电池
        Arrays.stream(getBatteryRange()).forEach(i -> {
            ItemStack itemStack = getItem(i);
            if (!itemStack.isEmpty()) {
                // 如果是电池，则输电
                itemStack.getCapability(CapEnergy.EU).ifPresent(storage -> {
                    // 如果自身电量未满且电池中有电，则试图从电池中充电
                    if (!energyIsFull() && !storage.energyIsEmpty()) {
                        EnergyUtils.energyExchange(storage, this);
                    }
                });
                // 如果是一次性电池，则试图消耗物品充电
                DisposableBattery db = DisposableBattery.get(itemStack.getItem());
                if (db != null &&
                        !db.getTier().higherThan(this.getTier()) &&
                        (getMaxEnergyStored() - getEnergyStored() >= db.getEnergy())){
                    itemStack.shrink(1);
                    this.receiveEnergy(db.getEnergy(), false);
                }
            }
        });
        // 检查外部输电
        // 如果自身电量未满，则给自己充电
        // 如果自身电量已满，则给电池充电
    }
}
