package com.road.eternalcore.common.item.block;

import com.road.eternalcore.common.item.IModItem;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ModBlockItem extends BlockItem implements IModItem {
    public ModBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
    public ITextComponent getName(ItemStack itemStack) {
        return getBlock().getName();
    }
    public final void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag tooltipFlag) {
        addItemDescription(itemStack, world, list, tooltipFlag);
        addOtherHoverText(itemStack, world, list, tooltipFlag);
    }
}
