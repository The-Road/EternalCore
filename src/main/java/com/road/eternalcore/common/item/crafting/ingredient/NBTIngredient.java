package com.road.eternalcore.common.item.crafting.ingredient;

import net.minecraft.item.ItemStack;

public class NBTIngredient extends net.minecraftforge.common.crafting.NBTIngredient {
    protected NBTIngredient(ItemStack stack) {
        super(stack);
    }
    public static NBTIngredient of(ItemStack stack){
        return new NBTIngredient(stack);
    }
}
