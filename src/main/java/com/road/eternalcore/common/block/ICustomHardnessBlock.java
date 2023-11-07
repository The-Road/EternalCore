package com.road.eternalcore.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public interface ICustomHardnessBlock {
    // 自定义硬度和爆炸抗性的方块
    // CustomTierItem损失工具耐久的时候会用到
    float customDestroySpeed(BlockState blockState, IBlockReader world, BlockPos pos);
    float customExplosionResistance(BlockState blockState, IBlockReader world, BlockPos pos);
}
