package com.road.eternalcore.common.item.tool;

import com.road.eternalcore.common.item.group.ModGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class CustomToolItem extends CustomTierItem{
    private final Set<Block> diggableBlocks;
    protected float mineSpeedRate = 1.0F;
    public CustomToolItem(Set<Block> diggableBlocks, float atk_damage, float atk_speed, Item.Properties properties){
        super(properties.tab(ModGroup.toolGroup), atk_damage, atk_speed);
        this.diggableBlocks = diggableBlocks;
    }
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        if (getToolTypes(itemStack).stream().anyMatch(blockState::isToolEffective)){
            return getMineSpeed(itemStack);
        }
        if (this.diggableBlocks != null && this.diggableBlocks.contains(blockState.getBlock())){
            return getMineSpeed(itemStack);
        }
        return 1.0F;
    }
    public float getMineSpeedRate(){
        return mineSpeedRate;
    }

}
