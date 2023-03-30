package com.road.eternalcore.common.item.crafting;

import com.road.eternalcore.common.item.crafting.recipe.SmithingRecipe;
import com.road.eternalcore.common.item.crafting.recipe.ToolShapedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;

public interface IModRecipeSerializer extends IRecipeSerializer {
    IRecipeSerializer<ToolShapedRecipe> toolShapedRecipe = IModRecipeSerializerRegistries.toolShapedRecipe.get();
    IRecipeSerializer<SmithingRecipe> smithingRecipe = IModRecipeSerializerRegistries.smithingRecipe.get();
}
