package com.road.eternalcore.common.item.tool;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;

import java.util.Set;

public class HoeItem extends CustomToolItem{
    private static final Set<Block> DIGGABLES = ImmutableSet.of(
            Blocks.NETHER_WART_BLOCK, Blocks.WARPED_WART_BLOCK, Blocks.HAY_BLOCK, Blocks.DRIED_KELP_BLOCK,
            Blocks.TARGET, Blocks.SHROOMLIGHT, Blocks.SPONGE, Blocks.WET_SPONGE, Blocks.JUNGLE_LEAVES,
            Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES
    );
    private static final net.minecraft.item.HoeItem hoeReference = (net.minecraft.item.HoeItem) Items.IRON_HOE;
    public HoeItem(){
        super(DIGGABLES, 0.0F, 2.0F, new Properties().addToolType(ModToolType.HOE, 0));
    }
    protected double getBasicAttackDamage(ItemStack itemStack){
        // 锄头的攻击加成只有一半
        return super.getBasicAttackDamage(itemStack) * 0.5;
    }
    public ActionResultType useOn(ItemUseContext useContext) {
        return hoeReference.useOn(useContext);
    }
}
