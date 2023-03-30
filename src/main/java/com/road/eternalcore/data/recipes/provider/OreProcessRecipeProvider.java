package com.road.eternalcore.data.recipes.provider;

import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.MaterialSmeltData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.api.ore.OreShape;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.common.item.material.MaterialItems;
import com.road.eternalcore.common.util.ModResourceLocation;
import com.road.eternalcore.data.tags.ModTags;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class OreProcessRecipeProvider extends ModRecipeProvider {
    // 用于自动生成矿物处理的配方
    public OreProcessRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }
    public static void addRecipes(Consumer<IFinishedRecipe> consumer){
        // 添加在熔炉里冶炼的配方
        for (Ores ore : Ores.getAllOres()){
            if (ore.getSmeltResult() != null){
                addSmeltRecipe(consumer, ore, ore.getSmeltResult());
            }
        }
    }
    private static void addSmeltRecipe(Consumer<IFinishedRecipe> consumer, Ores ore, Materials material){
        // 添加矿石中间产物直接烧成锭的配方
        for (OreShape shape : OreShape.getAllShapes()){
            Item smeltProduct = MaterialItems.get(MaterialShape.INGOT, material);
            if (smeltProduct != null){
                Tags.IOptionalNamedTag<Item> tag = ModTags.Items.getOreTag(shape, ore);
                String name = Ores.getRegisterName(shape, ore);
                Float exp = MaterialSmeltData.get(material).getSmeltExp();
                CookingRecipeBuilder.smelting(Ingredient.of(tag), smeltProduct, exp, 200)
                        .unlockedBy("has_" + name, has(tag))
                        .save(consumer, new ModResourceLocation("smelt_" + name));
                CookingRecipeBuilder.blasting(Ingredient.of(tag), smeltProduct, exp, 100)
                        .unlockedBy("has_" + name, has(tag))
                        .save(consumer, new ModResourceLocation("blasting_smelt_" + name));
            }
        }
    }
}
