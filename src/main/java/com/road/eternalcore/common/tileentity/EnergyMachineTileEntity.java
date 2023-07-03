package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.Constant;
import com.road.eternalcore.api.energy.CapEnergy;
import com.road.eternalcore.api.energy.FEtoEUStorage;
import com.road.eternalcore.api.energy.eu.EUTier;
import com.road.eternalcore.api.energy.eu.IEUStorage;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public abstract class EnergyMachineTileEntity extends MachineTileEntity implements IEUStorage {
    protected int energy = 0;
    protected int maxEnergy = 1000;
    protected EUTier euTier = EUTier.LV;
    protected final LazyOptional<IEUStorage> storage = LazyOptional.of(() -> this);
    public EnergyMachineTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt(Constant.Machine_energy, energy);
        nbt.putInt(Constant.Machine_euTier, euTier.getLevel());
        return nbt;
    }
    public void load(BlockState blockState, CompoundNBT nbt){
        super.load(blockState, nbt);
        loadEnergyNBT(nbt);
        loadEUTierNBT(nbt);
    }
    protected void loadEnergyNBT(CompoundNBT nbt){
        if (nbt.contains(Constant.Machine_energy, 3)){
            saveEnergy(nbt.getInt(Constant.Machine_energy));
        }
    }
    protected void loadEUTierNBT(CompoundNBT nbt){
        if (nbt.contains(Constant.Machine_euTier, 3)){
            setTier(EUTier.tier(nbt.getInt(Constant.Machine_euTier)));
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

    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!sideFree(side)){
            return super.getCapability(cap, side);
        }
        if (Objects.equals(cap, CapEnergy.EU)){
            return storage.cast();
        }else if (Objects.equals(cap, CapEnergy.FE)){
            return storage.lazyMap(FEtoEUStorage::new).cast();
        }else{
            return super.getCapability(cap, side);
        }
    }
}
