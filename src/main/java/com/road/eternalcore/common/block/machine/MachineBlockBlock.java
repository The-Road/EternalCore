package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.common.tileentity.MachineBlockTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class MachineBlockBlock extends MachineBlock{

    protected boolean TileEntityMatch(TileEntity tileEntity) {
        return tileEntity instanceof MachineBlockTileEntity;
    }

    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MachineBlockTileEntity();
    }

    protected DirectionProperty facingType() {
        return BlockStateProperties.FACING;
    }
}
