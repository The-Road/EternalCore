package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.api.block.MaterialBlockProperty;
import com.road.eternalcore.api.block.ModBlockStateProperties;
import com.road.eternalcore.api.block.StringConstant;
import com.road.eternalcore.common.tileentity.LockerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class LockerBlock extends MachineBlock{

    public LockerBlock() {
        super();
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ModBlockStateProperties.MATERIAL, StringConstant.of(MaterialBlockProperty.DEFAULT))
                .setValue(facingType(), Direction.NORTH)
                .setValue(BlockStateProperties.OPEN, false)
        );
    }
    protected DirectionProperty facingType(){
        return BlockStateProperties.HORIZONTAL_FACING;
    };
    protected boolean tileEntityMatch(TileEntity tileEntity) {
        return tileEntity instanceof LockerTileEntity;
    }
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LockerTileEntity();
    }

    public void tick(BlockState blockState, ServerWorld world, BlockPos pos, Random random) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntityMatch(tileEntity)){
            ((LockerTileEntity)tileEntity).recheckOpenState();
        }
    }
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.OPEN);
    }
}
