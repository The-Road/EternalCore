package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.common.block.BlockMaterial;
import com.road.eternalcore.common.block.ModBlock;
import com.road.eternalcore.common.item.tool.ModToolType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public abstract class AbstractMachineBlock extends ModBlock {
    // 机器方块类
    public AbstractMachineBlock(Properties properties){
        super(properties);
    }
    public static Properties machineProperties(float destroyTime, float explosionResistance){
        return Properties.of(BlockMaterial.MACHINE)
                .requiresCorrectToolForDrops().harvestTool(ModToolType.WRENCH)
                .strength(destroyTime, explosionResistance)
                .sound(SoundType.METAL);
    }
    public static Properties stoneProperties(float destroyTime, float explosionResistance){
        return Properties.of(Material.STONE)
                .requiresCorrectToolForDrops().harvestTool(ModToolType.PICKAXE)
                .strength(destroyTime, explosionResistance)
                .sound(SoundType.STONE);
    }
    // 禁止生物生成
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, EntityType<?> entityType) {
        return false;
    }
}
