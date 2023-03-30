package com.road.eternalcore.common.item.tool;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;

import java.util.Set;


public class HammerItem extends CustomToolItem {
    protected static final Set<Block> DIGGABLE_BLOCKS = Sets.newHashSet(
            Blocks.STONE,
            Blocks.COBBLESTONE
    );

    public HammerItem(){
        super(DIGGABLE_BLOCKS, 3.0F, 1.0F, new Properties().addToolType(ModToolType.HAMMER, 0));
        this.mineSpeedRate = 0.75F;
    }

    public boolean isCorrectToolForDrops(BlockState blockState){
        Material material = blockState.getMaterial();
        return material == Material.STONE;
    }
    public boolean forCrafting() {
        return true;
    }
}
