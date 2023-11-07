package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.ModConstant;
import com.road.eternalcore.common.tileentity.data.MachineTileEntityCoverData;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public abstract class CoverMachineTileEntity extends MachineTileEntity {
    // 拥有覆盖板的机器
    protected MachineTileEntityCoverData covers = new MachineTileEntityCoverData(this);

    public CoverMachineTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.put(ModConstant.Machine_cover, covers.save());
        return nbt;
    }
    public void load(BlockState blockState, CompoundNBT nbt){
        super.load(blockState, nbt);
        loadCoversNBT(nbt);
    }
    protected void loadCoversNBT(CompoundNBT nbt){
        if (nbt.contains(ModConstant.Machine_cover)){
            covers.load(nbt.getList(ModConstant.Machine_cover, 8));
        }
    }
}
