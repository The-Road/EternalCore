package com.road.eternalcore.common.block.pipe;

import com.road.eternalcore.common.item.tool.ModToolType;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class PipeBlock extends AbstractPipeBlock{
    // 管道半径：微型=2，小型=3，中型=4，大型=6，巨型=8
    public PipeBlock(int pipeRadius) {
        super(1.0F, 1.0F, ModToolType.WRENCH, pipeRadius);
    }

    protected boolean canConnectTo(BlockState blockState, Direction direction, BlockState facingState, IWorld world, BlockPos blockPos, BlockPos facingPos) {
        return false;
    }
}
