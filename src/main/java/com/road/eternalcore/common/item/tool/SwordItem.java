package com.road.eternalcore.common.item.tool;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;

public class SwordItem extends CustomWeaponItem{
    public SwordItem(){
        super(3.0F, 1.6F, new Properties());
    }
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        Block block = blockState.getBlock();
        if (block instanceof BambooBlock || block instanceof BambooSaplingBlock){
            return 65535.0F;
        }
        if (blockState.is(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            Material material = blockState.getMaterial();
            return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.CORAL &&
                    !blockState.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
        }
    }
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return blockState.is(Blocks.COBWEB);
    }
}
