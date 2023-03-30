package com.road.eternalcore.common.item.crafting.recipe;

import com.road.eternalcore.common.inventory.ToolCraftingInventory;
import com.road.eternalcore.common.item.crafting.IModRecipeType;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface IToolCraftingRecipe extends IRecipe<ToolCraftingInventory> {
    default IRecipeType<?> getType() {
        return IModRecipeType.TOOL_CRAFTING;
    }
}
