package com.road.eternalcore.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ModItem extends Item implements IModItem {
    public ModItem(Properties properties) {
        super(properties);
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public final void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag tooltipFlag) {
        addItemDescription(itemStack, world, list, tooltipFlag);
        addOtherHoverText(itemStack, world, list, tooltipFlag);
    }
}
