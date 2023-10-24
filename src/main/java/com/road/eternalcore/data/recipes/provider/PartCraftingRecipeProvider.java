package com.road.eternalcore.data.recipes.provider;

import com.road.eternalcore.api.material.MaterialTierData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.api.tool.CraftToolType;
import com.road.eternalcore.data.recipes.builder.PartCraftingRecipeBuilder;
import com.road.eternalcore.data.tags.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

import static com.road.eternalcore.api.material.MaterialShape.*;

public class PartCraftingRecipeProvider extends ModRecipeProvider{

    public PartCraftingRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    public static void addRecipes(Consumer<IFinishedRecipe> consumer){
        addPlateRecipes(consumer);
        addRodRecipes(consumer);
    }

    private static void addPlateRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidSmithingData()){
            Materials material = tierData.getMaterial();
            PartCraftingRecipeBuilder.smith(Materials.getItem(PLATE, material))
                    .byProduct(Materials.getItem(SMALL_DUST, material), 2)
                    .smithLevel(tierData.getSmithLevel())
                    .toolUse(CraftToolType.HAMMER, 1 << (tierData.getSmithLevel() - 1))
                    .requires(ModTags.Items.getMaterialTag(INGOT, material), 2)
                    .group("plate")
                    .unlockedBy("has_material", has(ModTags.Items.getMaterialTag(INGOT, material)))
                    .save(consumer, "partcraft_" + Materials.getRegisterName(PLATE, material));
        }
    }
    private static void addRodRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidSmithingData()){
            Materials material = tierData.getMaterial();
            PartCraftingRecipeBuilder.smith(Materials.getItem(ROD, material))
                    .byProduct(Materials.getItem(SMALL_DUST, material), 1)
                    .smithLevel(tierData.getSmithLevel())
                    .toolUse(CraftToolType.FILE, 1 << (tierData.getSmithLevel() - 1))
                    .requires(ModTags.Items.getMaterialTag(INGOT, material))
                    .group("rod")
                    .unlockedBy("has_material", has(ModTags.Items.getMaterialTag(INGOT, material)))
                    .save(consumer, "partcraft_" + Materials.getRegisterName(ROD, material));
        }
    }
}
