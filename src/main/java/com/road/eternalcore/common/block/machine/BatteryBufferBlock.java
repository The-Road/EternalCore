package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.api.block.MaterialBlockProperty;
import com.road.eternalcore.api.block.ModBlockStateProperties;
import com.road.eternalcore.common.tileentity.BatteryBufferTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

public class BatteryBufferBlock extends MachineBlock{
    public BatteryBufferBlock() {
        super();
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ModBlockStateProperties.MachineMaterial, MaterialBlockProperty.DefaultValue)
                .setValue(facingType(), Direction.NORTH)
        );
    }
    protected boolean tileEntityMatch(TileEntity tileEntity) {
        return tileEntity instanceof BatteryBufferTileEntity;
    }

    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BatteryBufferTileEntity();
    }

    protected DirectionProperty facingType() {
        return BlockStateProperties.FACING;
    }
}
