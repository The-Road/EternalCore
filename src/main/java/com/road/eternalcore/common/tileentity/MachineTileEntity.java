package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.ModConstant;
import com.road.eternalcore.api.block.ModBlockStateProperties;
import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.api.material.Materials;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MachineTileEntity extends LockableLootTileEntity {
    protected MaterialBlockData blockData = MaterialBlockData.NULL;
    protected NonNullList<ItemStack> items;
    public MachineTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        initItems();
    }
    protected void initItems(){
        if (this.items == null || this.items.size() < getContainerSize()){
            this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        }
    }
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        if (blockData != MaterialBlockData.NULL) {
            nbt.putString(ModConstant.Material, getMaterial().getName());
        }
        if (!trySaveLootTable(nbt)) {
            ItemStackHelper.saveAllItems(nbt, this.items);
        }
        return nbt;
    }
    public void load(BlockState blockState, CompoundNBT nbt){
        super.load(blockState, nbt);
        loadMaterialNBT(nbt);
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!tryLoadLootTable(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.items);
        }
    }
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }
    protected void loadMaterialNBT(CompoundNBT nbt){
        if (nbt.contains(ModConstant.Material, 8)){
            Materials material = Materials.get(nbt.getString(ModConstant.Material));
            blockData = MaterialBlockData.get(material);
        }
    }

    // 更改材料，同时更新方块状态
    public void setMaterial(Materials material){
        blockData = MaterialBlockData.get(material);
        BlockState blockState = getBlockState();
        if (blockState.hasProperty(ModBlockStateProperties.MATERIAL)) {
            if (Materials.get(blockState.getValue(ModBlockStateProperties.MATERIAL).value()) != material) {
                this.level.setBlock(getBlockPos(), ModBlockStateProperties.MATERIAL.setBlockStateProperty(blockState, material), 3);
            }
        }
    }
    public Materials getMaterial(){
        return blockData.getMaterial();
    }
    public MaterialBlockData getMaterialBlockData(){
        return blockData;
    }

    // 统一将判断!remove放在开头，子类只需改写getMachineCapability
    public final <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove){
            return getMachineCapability(cap, side);
        }
        return super.getCapability(cap, side);
    }
    protected <T> LazyOptional<T> getMachineCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return super.getCapability(cap, side);
    }
}
