package com.road.eternalcore.common.item;

import com.road.eternalcore.TranslationUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public interface IModItem {
    // ModItem接口
    default void addItemDescription(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag tooltipFlag){
        ITextComponent itemDescription = TranslationUtils.getItemDescription(itemStack.getItem());
        if (itemDescription != null){
            list.add(itemDescription);
        }
    }

    default void addOtherHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag tooltipFlag){
    }
}
