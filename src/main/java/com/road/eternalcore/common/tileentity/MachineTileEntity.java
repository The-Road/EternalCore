package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.tileentity.data.MachineCover;
import com.road.eternalcore.common.tileentity.data.TileEntityCovers;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public abstract class MachineTileEntity extends LockableLootTileEntity {
    protected TileEntityCovers covers = new TileEntityCovers(this);
    protected MaterialBlockData blockData = MaterialBlockData.NULL;
    public MachineTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.put("covers", covers.save());
        nbt.putString("material", getMaterial().getName());
        return nbt;
    }
    public void load(BlockState blockState, CompoundNBT nbt){
        super.load(blockState, nbt);
        loadCoversNBT(nbt);
        loadMaterialNBT(nbt);
    }
    protected void loadCoversNBT(CompoundNBT nbt){
        if (nbt.contains("covers")){
            covers.load(nbt.getList("covers", 8));
        }
    }
    protected void loadMaterialNBT(CompoundNBT nbt){
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
    // 判断接触面是否空闲（非正面且无覆盖板）
    public boolean sideFree(Direction side){
        // 四面朝向的机器正面非空闲（六面朝向的机器正面是输出口）
        return covers.getCover(side) == MachineCover.NULL;
    }

}
