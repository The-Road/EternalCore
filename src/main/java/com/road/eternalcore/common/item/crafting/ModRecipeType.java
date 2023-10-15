package com.road.eternalcore.common.item.crafting;

import com.road.eternalcore.common.item.crafting.recipe.DisabledRecipe;
import com.road.eternalcore.common.item.crafting.recipe.IToolCraftingRecipe;
import com.road.eternalcore.common.item.crafting.recipe.PartCraftingRecipe;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;

public class ModRecipeType {
    public static IRecipeType<DisabledRecipe> DISABLED = register("disabled");
    public static IRecipeType<IToolCraftingRecipe> TOOL_CRAFTING = register("tool_crafting");
    public static IRecipeType<PartCraftingRecipe> PART_CRAFTING = register("part_crafting");

    static <T extends IRecipe<?>> IRecipeType<T> register(final String name) {
        return Registry.register(Registry.RECIPE_TYPE, new ModResourceLocation(name), new IRecipeType<T>() {
            public String toString() {
                return name;
            }
        });
    }
}
