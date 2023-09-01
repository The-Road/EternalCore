package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.common.block.BlockMaterial;
import com.road.eternalcore.common.item.tool.ModToolType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public abstract class AbstractMachineBlock extends Block {
    // 机器方块类
    public AbstractMachineBlock(float destroyTime, float explosionResistance) {
        super(Properties.of(BlockMaterial.MACHINE)
                .requiresCorrectToolForDrops().harvestTool(ModToolType.WRENCH)
                .strength(destroyTime, explosionResistance)
                .sound(SoundType.METAL)
        );
    }
    // 禁止生物生成
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, EntityType<?> entityType) {
        return false;
    }
}
