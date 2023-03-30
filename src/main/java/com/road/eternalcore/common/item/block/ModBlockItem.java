package com.road.eternalcore.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class ModBlockItem extends BlockItem {
    public ModBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
    public ITextComponent getName(ItemStack itemStack) {
        return getBlock().getName();
    }
}
