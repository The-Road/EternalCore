package com.road.eternalcore.common.item.crafting.recipe;

import com.road.eternalcore.common.item.crafting.ModRecipeSerializer;
import com.road.eternalcore.common.item.crafting.ModRecipeType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class DisabledRecipe implements IRecipe<IInventory> {
    private final ResourceLocation id;
    public DisabledRecipe(ResourceLocation id){
        this.id = id;
    }

    public boolean matches(IInventory inventory, World world) {
        return false;
    }

    public ItemStack assemble(IInventory inventory) {
        return ItemStack.EMPTY;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    public ResourceLocation getId() {
        return id;
    }

    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.disabledRecipe;
    }

    public IRecipeType<?> getType() {
        return ModRecipeType.DISABLED;
    }
}
