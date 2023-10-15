package com.road.eternalcore.common.item.tool;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;

import java.util.Set;

public class AxeItem extends CustomToolItem{
    private static final Set<Material> DIGGABLE_MATERIALS = Sets.newHashSet(
            Material.WOOD, Material.NETHER_WOOD, Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO,
            Material.VEGETABLE
    );

    private static final Set<Block> DIGGABLES = Sets.newHashSet(
            Blocks.LADDER, Blocks.SCAFFOLDING, Blocks.OAK_BUTTON, Blocks.SPRUCE_BUTTON, Blocks.BIRCH_BUTTON,
            Blocks.JUNGLE_BUTTON, Blocks.DARK_OAK_BUTTON, Blocks.ACACIA_BUTTON, Blocks.CRIMSON_BUTTON,
            Blocks.WARPED_BUTTON
    );

    private static final net.minecraft.item.AxeItem axeReference = (net.minecraft.item.AxeItem) Items.IRON_AXE;

    public AxeItem(){
        super(DIGGABLES, 5.0F, 1.0F, new Properties().addToolType(ModToolType.AXE, 0));
    }

    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        Material material = blockState.getMaterial();
        return DIGGABLE_MATERIALS.contains(material) ?  getMineSpeed(itemStack) : super.getDestroySpeed(itemStack, blockState);
    }
    public ActionResultType useOn(ItemUseContext useContext) {
        return axeReference.useOn(useContext);
    }
}
