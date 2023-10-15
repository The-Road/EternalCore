package com.road.eternalcore.common.item.block;

import com.road.eternalcore.common.item.IModItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class ModBlockItem extends BlockItem implements IModItem {
    public ModBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
    public ITextComponent getName(ItemStack itemStack) {
        return getBlock().getName();
    }
}
