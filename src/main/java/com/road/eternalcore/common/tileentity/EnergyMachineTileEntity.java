package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.ModConstant;
import com.road.eternalcore.api.energy.CapEnergy;
import com.road.eternalcore.api.energy.DisposableBattery;
import com.road.eternalcore.api.energy.EnergyUtils;
import com.road.eternalcore.api.energy.FEtoEUStorage;
import com.road.eternalcore.api.energy.eu.EUTier;
import com.road.eternalcore.api.energy.eu.IEUStorage;
import com.road.eternalcore.common.tileentity.data.EnergyMachineGUIData;
import com.road.eternalcore.common.tileentity.wrapper.EnergyMachineWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public abstract class EnergyMachineTileEntity extends MachineTileEntity implements IEUStorage, ITickableTileEntity{
    // 通常的工作机器类，拥有输入槽，输出槽，电池槽，流体槽（还没写）
    protected int energy = 0;
    protected int maxEnergy = 1000;
    protected EUTier euTier = EUTier.LV;
    protected final LazyOptional<IEUStorage> storage = LazyOptional.of(() -> this);
    // 设置输入槽、输出槽、电池槽的大小
    protected int inputSize;
    protected int resultSize;
    protected int batterySize;
    protected int[] inputRange;
    protected int[] resultRange;
    protected int[] batteryRange;
    protected final EnergyMachineGUIData guiData = new EnergyMachineGUIData(this);
    public EnergyMachineTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    protected void initContainerRange(int inputSize, int resultSize, int batterySize){
        this.inputSize = inputSize;
        this.resultSize = resultSize;
        this.batterySize = batterySize;
        this.inputRange = IntStream.range(0, inputSize).toArray();
        this.resultRange = IntStream.range(inputSize, inputSize + resultSize).toArray();
        this.batteryRange = IntStream.range(inputSize + resultSize, inputSize + resultSize + batterySize).toArray();
        // 防止出bug
        if (this.items == null || this.items.size() < getContainerSize()){
            this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        }
    }

    public int getContainerSize() {
        return inputSize + resultSize + batterySize;
    }

    protected IItemHandler createUnSidedHandler() {
        return new EnergyMachineWrapper(this);
    }
    // 用于某些需要和指定槽位进行物品交互的操作，例如漏斗
    public boolean isInputSlot(int slotId){
        return Arrays.stream(inputRange).anyMatch(slot -> slot == slotId);
    }
    public boolean isResultSlot(int slotId){
        return Arrays.stream(resultRange).anyMatch(slot -> slot == slotId);
    }
    public boolean isBatterySlot(int slotId){
        return Arrays.stream(batteryRange).anyMatch(slot -> slot == slotId);
    }
    public boolean canPlaceItem(int slotId, ItemStack itemStack){
        return isInputSlot(slotId);
    }
    public boolean canTakeItem(int slotId, ItemStack itemStack){
        return isResultSlot(slotId);
    }
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt(ModConstant.Machine_energy, energy);
        nbt.putInt(ModConstant.Machine_euTier, euTier.getLevel());
        return nbt;
    }
    public void load(BlockState blockState, CompoundNBT nbt){
        super.load(blockState, nbt);
        loadEnergyNBT(nbt);
        loadEUTierNBT(nbt);
    }
    protected void loadEnergyNBT(CompoundNBT nbt){
        if (nbt.contains(ModConstant.Machine_energy, 3)){
            saveEnergy(nbt.getInt(ModConstant.Machine_energy));
        }
    }
    protected void loadEUTierNBT(CompoundNBT nbt){
        if (nbt.contains(ModConstant.Machine_euTier, 3)){
            setTier(EUTier.tier(nbt.getInt(ModConstant.Machine_euTier)));
        }
        // 机器的默认最大储电量等于电压等级的40倍（两秒充满）
        setMaxEnergyStored(getTier().getMaxVoltage() * 40);
    }
    protected void setTier(EUTier euTier){
        this.euTier = euTier;
    }
    // IEUStorage接口
    public EUTier getTier(){
        return euTier;
    }
    public void saveEnergy(int energy){
        this.energy = energy;
    }
    public int getEnergyStored(){
        return this.energy;
    }
    protected void setMaxEnergyStored(int maxEnergy){
        this.maxEnergy = maxEnergy;
    }
    public int getMaxEnergyStored(){
        return maxEnergy;
    };

    protected <T> LazyOptional<T> getMachineCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (Objects.equals(cap, CapEnergy.EU)){
            return storage.cast();
        }else if (Objects.equals(cap, CapEnergy.FE)){
            return storage.lazyMap(FEtoEUStorage::new).cast();
        }
        return super.getMachineCapability(cap, side);
    }
    //Energy接口，一般都是true
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
    // 机器的充电处理，正常情况下每帧都调用
    protected void tickEnergyCharge(){
        // 检查电池格中的电池
        Arrays.stream(this.batteryRange).forEach(i -> {
            ItemStack itemStack = getItem(i);
            if (!itemStack.isEmpty()) {
                // 如果是电池，则输电
                itemStack.getCapability(CapEnergy.EU).ifPresent(storage -> {
                    // 如果自身电量未满且电池中有电，则试图从电池中充电
                    if (!isEnergyFull() && !storage.isEnergyEmpty()) {
                        EnergyUtils.energyExchange(storage, this);
                    }
                });
                // 如果是一次性电池，则试图消耗物品充电
                DisposableBattery db = DisposableBattery.get(itemStack.getItem());
                if (db != null &&
                        !db.tierHigherThan(this) &&
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
