package com.road.eternalcore.data.recipes.provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn){
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer){
        removeVanillaRecipes(consumer);
        CraftingRecipeProvider.addRecipes(consumer);
        SmithingRecipeProvider.addRecipes(consumer);
        OreProcessRecipeProvider.addRecipes(consumer);
    }
    private void removeVanillaRecipes(Consumer<IFinishedRecipe> consumer){

    }
}
