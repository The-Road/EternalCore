package com.road.eternalcore.common.item.tool;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;


public class HammerItem extends CustomToolItem {
    protected static final Set<Block> DIGGABLE_BLOCKS = ImmutableSet.of(
            Blocks.STONE,
            Blocks.COBBLESTONE
    );

    public HammerItem(){
        super(DIGGABLE_BLOCKS, 3.0F, 1.0F, new Properties().addToolType(ModToolType.HAMMER, 0));
        this.mineSpeedRate = 0.75F;
    }

    public boolean canHarvestBlock(ItemStack itemStack, BlockState blockState){
        Material material = blockState.getMaterial();
        return material == Material.STONE;
    }

    public void addOtherHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag tooltipFlag) {
        addSmithLevelText(itemStack, list);
        super.addOtherHoverText(itemStack, world, list, tooltipFlag);
    }
}
