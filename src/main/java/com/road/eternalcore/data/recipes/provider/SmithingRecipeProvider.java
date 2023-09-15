package com.road.eternalcore.data.recipes.provider;

import com.road.eternalcore.api.material.MaterialTierData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.api.tool.CraftToolType;
import com.road.eternalcore.common.item.material.MaterialItems;
import com.road.eternalcore.data.recipes.builder.SmithingRecipeBuilder;
import com.road.eternalcore.data.tags.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

import static com.road.eternalcore.api.material.MaterialShape.*;

public class SmithingRecipeProvider extends ModRecipeProvider{

    public SmithingRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    public static void addRecipes(Consumer<IFinishedRecipe> consumer){
        addPlateRecipes(consumer);
    }

    private static void addPlateRecipes(Consumer<IFinishedRecipe> consumer){
        MaterialTierData.getData().forEach((material, tierData) -> {
            if (material.getType() == Materials.Type.SOLID && tierData.getSmithLevel() > 0){
                SmithingRecipeBuilder.smith(MaterialItems.get(PLATE, material))
                        .byProduct(MaterialItems.get(SMALL_DUST, material), 2)
                        .level(tierData.getSmithLevel())
                        .toolUse(CraftToolType.HAMMER)
                        .requires(ModTags.Items.getMaterialTag(INGOT, material), 2)
                        .group("plate")
                        .unlockedBy("has_material", has(ModTags.Items.getMaterialTag(INGOT, material)))
                        .save(consumer, "smith_" + Materials.getRegisterName(PLATE, material));
            }
        });
    }
}
