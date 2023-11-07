package com.road.eternalcore.common.block;

import net.minecraft.block.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ICasingRenderBlock {
    // 该接口用于在MachineModel中获取方块的外壳方块
    @OnlyIn(Dist.CLIENT)
    BlockState getRenderCasing(BlockState blockState);
}
