package com.road.eternalcore.data.recipes.provider;

import com.road.eternalcore.common.item.material.MaterialItems;
import com.road.eternalcore.common.item.tool.CraftToolType;
import com.road.eternalcore.data.recipes.builder.SmithingRecipeBuilder;
import com.road.eternalcore.data.tags.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import java.util.function.Consumer;

import static com.road.eternalcore.api.material.MaterialShape.INGOT;
import static com.road.eternalcore.api.material.MaterialShape.PLATE;
import static com.road.eternalcore.api.material.Materials.IRON;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn){
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer){
        removeVanillaRecipes(consumer);
        CraftingRecipeProvider.addRecipes(consumer);
        addSmithingRecipes(consumer);
        OreProcessRecipeProvider.addRecipes(consumer);
    }
    private void removeVanillaRecipes(Consumer<IFinishedRecipe> consumer){

    }

    private void addSmithingRecipes(Consumer<IFinishedRecipe> consumer){
        SmithingRecipeBuilder.smith(MaterialItems.get(PLATE, IRON)).level(0)
                .toolUse(CraftToolType.HAMMER)
                .requires(ModTags.Items.getMaterialTag(INGOT, IRON))
                .group("iron_plate")
                .unlockedBy("has_iron_ingot", has(ModTags.Items.getMaterialTag(INGOT, IRON)))
                .save(consumer, "smith_iron_plate");
    }
}
