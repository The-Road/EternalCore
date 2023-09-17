package com.road.eternalcore.common.item.crafting.recipe;

import com.road.eternalcore.common.inventory.ToolCraftingInventory;
import com.road.eternalcore.common.item.crafting.ModRecipeType;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface IToolCraftingRecipe extends IRecipe<ToolCraftingInventory>, IToolUsedRecipe{
    default IRecipeType<?> getType() {
        return ModRecipeType.TOOL_CRAFTING;
    }
}
