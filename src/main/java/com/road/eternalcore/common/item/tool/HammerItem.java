package com.road.eternalcore.common.item.tool;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;
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
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag iTooltipFlag) {
        super.appendHoverText(itemStack, world, list, iTooltipFlag);
        // 添加锻造等级
        DecimalFormat format = ItemStack.ATTRIBUTE_MODIFIER_FORMAT;
        list.add(new TranslationTextComponent("eternalcore.toolTip.tool.smithingLevel", getSmithingLevel(itemStack)).withStyle(TextFormatting.BLUE));
    }
}
