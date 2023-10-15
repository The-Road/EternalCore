package com.road.eternalcore.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItem extends Item implements IModItem {
    public ModItem(Properties properties) {
        super(properties);
    }
    public boolean isBookEnchantable(ItemStack stack, ItemStack book){
        return false;
    }
}
