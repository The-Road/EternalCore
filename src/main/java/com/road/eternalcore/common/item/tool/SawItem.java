package com.road.eternalcore.common.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;

public class SawItem extends CustomToolItem{
    public SawItem(){
        super(1.0F, 1.2F, new Properties().addToolType(ModToolType.SAW, 0));
    }
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        Block block = blockState.getBlock();
        if (block.is(BlockTags.LOGS) || block.is(BlockTags.ICE)){
            return getMineSpeed(itemStack);
        } else {
            return super.getDestroySpeed(itemStack, blockState);
        }
    }
}
