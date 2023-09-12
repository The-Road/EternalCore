package com.road.eternalcore.api.energy.network;

import com.road.eternalcore.api.energy.eu.IEUTier;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEnergyNetworkWire extends IEUTier {
    // 接入电网的电线方块需要在Block中实现IEnergyNetworkWire
    boolean isConnectedTo(BlockState blockState, Direction direction);
    int getMaxCurrent();
    int getLineLoss();
    // 电线烧毁
    default void burn(World world, BlockPos pos){
        world.removeBlock(pos, false);
        BlockState fireState = AbstractFireBlock.getState(world, pos);
        if (world.getBlockState(pos).isAir() && fireState.canSurvive(world, pos)){
            world.setBlockAndUpdate(pos, fireState);
        }
    }
}
