package com.road.eternalcore.data.recipes.provider;

import com.google.common.collect.Sets;
import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.MaterialTierData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.block.machine.MachineBlocks;
import com.road.eternalcore.common.item.ModItems;
import com.road.eternalcore.common.item.block.BlockItems;
import com.road.eternalcore.common.item.tool.CraftToolType;
import com.road.eternalcore.data.recipes.builder.NBTShapedRecipeBuilder;
import com.road.eternalcore.data.recipes.builder.ToolShapedRecipeBuilder;
import com.road.eternalcore.data.tags.ModTags;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.Tags;

import java.util.Set;
import java.util.function.Consumer;

public class CraftingRecipeProvider extends ModRecipeProvider {
    // 工作台合成的配方
    public CraftingRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }
    public static void addRecipes(Consumer<IFinishedRecipe> consumer){
        addMaterialRecipes(consumer);
        addMachineRecipes(consumer);
        addToolRecipes(consumer);
    }
    private static void addMaterialRecipes(Consumer<IFinishedRecipe> consumer){

    }
    private static void addMachineRecipes(Consumer<IFinishedRecipe> consumer){
        addMachineCasingRecipes(consumer); // 机器外壳的合成配方
        addLockerRecipes(consumer);
    }
    private static void addMachineCasingRecipes(Consumer<IFinishedRecipe> consumer){
        MachineBlocks.machine_casing.forEach((material, blockRegistry) -> {
            Block casing = blockRegistry.get();
            Item casingItem = BlockItems.get(casing);
            if (casingItem != null) {
                System.out.println("craft_" + casingItem);
                Tags.IOptionalNamedTag<Item> plateTag = ModTags.Items.getMaterialTag(MaterialShape.PLATE, material);
                ToolShapedRecipeBuilder.toolShaped(casingItem)
                        .toolUse(CraftToolType.WRENCH)
                        .pattern("XXX")
                        .pattern("X X")
                        .pattern("XXX")
                        .define('X', plateTag)
                        .group("machine_casing")
                        .unlockedBy("has_material", has(plateTag))
                        .save(consumer, "craft_" + casingItem);
            }
        });
    }
    private static void addLockerRecipes(Consumer<IFinishedRecipe> consumer){
        MaterialBlockData.getData().forEach((material, blockData) -> {
            Tags.IOptionalNamedTag<Item> plateTag = ModTags.Items.getMaterialTag(MaterialShape.PLATE, material);
            ToolShapedRecipeBuilder.toolShaped(MachineBlocks.locker.get())
                    .nbt((tag) -> {
                        CompoundNBT blockTag = new CompoundNBT();
                        blockTag.putString("material", material.getName());
                        tag.put("BlockEntityTag", blockTag);
                    })
                    .toolUse(CraftToolType.WRENCH)
                    .pattern("XXX")
                    .pattern("XCX")
                    .pattern("XXX")
                    .define('X', plateTag)
                    .define('C', Tags.Items.CHESTS_WOODEN)
                    .group("machine_locker")
                    .unlockedBy("has_material", has(plateTag))
                    .save(consumer, "craft_locker_" + material);
        });
    }
    private static void addToolRecipes(Consumer<IFinishedRecipe> consumer){
        addHammerRecipes(consumer);
        addWrenchRecipes(consumer);
    }
    private static void addHammerRecipes(Consumer<IFinishedRecipe> consumer){
        // 除了金属材质以外还可以做锤子的材料
        Set<Materials> list = Sets.newHashSet(
                Materials.WOOD,
                Materials.STONE
        );
        MaterialTierData.getData().forEach((material, tierData) -> {
            if(tierData.isSoft()){
                // 添加软锤
                if (tierData.isByHand()){
                    NBTShapedRecipeBuilder.shaped(ModItems.softHammer)
                            .nbt((tag) -> tag.putString("material", material.getName()))
                            .pattern("XX ")
                            .pattern("XX#")
                            .pattern("XX ")
                            .define('X', tierData.getItemTier().getRepairIngredient())
                            .define('#', tierData.getItemTier().getRodIngredient())
                            .group("soft_hammer_by_hand")
                            .unlockedBy("has_material", has(material.getIngredientTag()))
                            .save(consumer, "craft_soft_hammer_" + material);
                }else{
                    ToolShapedRecipeBuilder.toolShaped(ModItems.softHammer)
                            .nbt((tag) -> tag.putString("material", material.getName()))
                            .toolUse(CraftToolType.HAMMER)
                            .pattern("XX ")
                            .pattern("XX#")
                            .pattern("XX ")
                            .define('X', tierData.getItemTier().getRepairIngredient())
                            .define('#', tierData.getItemTier().getRodIngredient())
                            .group("soft_hammer")
                            .unlockedBy("has_material", has(material.getIngredientTag()))
                            .save(consumer, "craft_soft_hammer_" + material);
                }
            }else if(material.getType() != Materials.Type.OTHER || list.contains(material)){
                // 添加锤子配方
                if (tierData.isByHand()){
                    NBTShapedRecipeBuilder.shaped(ModItems.hammer)
                            .nbt((tag) -> tag.putString("material", material.getName()))
                            .pattern("XX ")
                            .pattern("XX#")
                            .pattern("XX ")
                            .define('X', tierData.getItemTier().getRepairIngredient())
                            .define('#', tierData.getItemTier().getRodIngredient())
                            .group("hammer_by_hand")
                            .unlockedBy("has_material", has(material.getIngredientTag()))
                            .save(consumer, "craft_hammer_" + material);
                }else{
                    ToolShapedRecipeBuilder.toolShaped(ModItems.hammer)
                            .nbt((tag) -> tag.putString("material", material.getName()))
                            .toolUse(CraftToolType.HAMMER)
                            .pattern("XX ")
                            .pattern("XX#")
                            .pattern("XX ")
                            .define('X', tierData.getItemTier().getRepairIngredient())
                            .define('#', tierData.getItemTier().getRodIngredient())
                            .group("hammer")
                            .unlockedBy("has_material", has(material.getIngredientTag()))
                            .save(consumer, "craft_hammer_" + material);
                }
            }
        });
    }
    private static void addWrenchRecipes(Consumer<IFinishedRecipe> consumer){
        MaterialTierData.getData().forEach((material, tierData) -> {
            if (!tierData.isSoft() && !tierData.isByHand() && material.getType() == Materials.Type.SOLID) {
                // 添加扳手配方
                ToolShapedRecipeBuilder.toolShaped(ModItems.wrench)
                        .nbt((tag) -> tag.putString("material", material.getName()))
                        .toolUse(CraftToolType.HAMMER)
                        .pattern("X X")
                        .pattern("XXX")
                        .pattern(" X ")
                        .define('X', tierData.getItemTier().getRepairIngredient())
                        .group("wrench")
                        .unlockedBy("has_material", has(material.getIngredientTag()))
                        .save(consumer, "craft_wrench_" + material);
            }
        });
    }
}
