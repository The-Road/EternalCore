package com.road.eternalcore.common.item.crafting;

import com.road.eternalcore.common.item.crafting.recipe.DisabledRecipe;
import com.road.eternalcore.common.item.crafting.recipe.PartCraftingRecipe;
import com.road.eternalcore.common.item.crafting.recipe.ToolShapedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;

public class ModRecipeSerializer {
    public static final IRecipeSerializer<DisabledRecipe> disabledRecipe = ModRecipeSerializerRegistries.disabledRecipe.get();
    public static final IRecipeSerializer<ToolShapedRecipe> toolShapedRecipe = ModRecipeSerializerRegistries.toolShapedRecipe.get();
    public static final IRecipeSerializer<PartCraftingRecipe> smithingRecipe = ModRecipeSerializerRegistries.smithingRecipe.get();
}
