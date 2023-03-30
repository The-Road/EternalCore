package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.api.material.Materials;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class MachineTileEntity extends LockableLootTileEntity {
    protected MaterialBlockData blockData = MaterialBlockData.NULL;
    public MachineTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putString("material", getMaterial().getName());
        return nbt;
    }
    public void load(BlockState blockState, CompoundNBT nbt){
        super.load(blockState, nbt);
        loadMaterial(nbt);
    }
    protected void loadMaterial(CompoundNBT nbt){
        if (nbt.contains("material", 8)){
            Materials material = Materials.get(nbt.getString("material"));
            blockData = MaterialBlockData.get(material);
        }
    }
    public Materials getMaterial(){
        return blockData.getMaterial();
    }
    public MaterialBlockData getMaterialBlockData(){
        return blockData;
    }
}
