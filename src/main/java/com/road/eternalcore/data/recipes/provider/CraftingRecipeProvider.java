package com.road.eternalcore.data.recipes.provider;

import com.google.common.collect.Sets;
import com.road.eternalcore.ModConstant;
import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.MaterialTierData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.api.tool.CraftToolType;
import com.road.eternalcore.common.block.machine.MachineBlocks;
import com.road.eternalcore.common.item.ModItems;
import com.road.eternalcore.data.recipes.builder.NBTShapedRecipeBuilder;
import com.road.eternalcore.data.recipes.builder.ShapedRecipeMaker;
import com.road.eternalcore.data.recipes.builder.ToolShapedRecipeBuilder;
import com.road.eternalcore.data.tags.ModTags;
import net.minecraft.block.Blocks;
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
        addBrickedCasingRecipes(consumer);
        addLockerRecipes(consumer);
        addMachineBlockRecipes(consumer);
        // 单独的合成配方

        // 合金炉
        ToolShapedRecipeBuilder.toolShaped(MachineBlocks.alloyFurnace.get())
                .toolUse(CraftToolType.WRENCH)
                .pattern("FXF")
                .define('F', Blocks.FURNACE)
                .define('X', MachineBlocks.getBrickedCasing(Materials.STONE))
                .group("alloy_furnace")
                .unlockedBy("has_material", has(MachineBlocks.getBrickedCasing(Materials.STONE)))
                .save(consumer, "toolcraft_alloy_furnace");
    }
    private static void addMachineCasingRecipes(Consumer<IFinishedRecipe> consumer){
        MachineBlocks.machine_casing.forEach((material, blockRegistry) -> {
            Item casing = blockRegistry.get().asItem();
            Tags.IOptionalNamedTag<Item> plateTag = ModTags.Items.getMaterialTag(MaterialShape.PLATE, material);
            ToolShapedRecipeBuilder.toolShaped(casing)
                    .toolUse(CraftToolType.WRENCH)
                    .smithLevel(MaterialTierData.get(material).getSmithLevel())
                    .pattern("XXX")
                    .pattern("X X")
                    .pattern("XXX")
                    .define('X', plateTag)
                    .group("machine_casing")
                    .unlockedBy("has_material", has(plateTag))
                    .save(consumer, "toolcraft_" + casing);
        });
    }
    private static void addBrickedCasingRecipes(Consumer<IFinishedRecipe> consumer){
        MachineBlocks.bricked_casing.forEach((material, blockRegistry) -> {
            Item casing = blockRegistry.get().asItem();
            MaterialBlockData blockData = MaterialBlockData.get(material);
            if (blockData.isStone()){
                ToolShapedRecipeBuilder.toolShaped(casing)
                        .toolUse(CraftToolType.WRENCH)
                        .pattern("XXX")
                        .pattern("X X")
                        .pattern("YYY")
                        .define('X', material.getIngredientTag())
                        .define('Y', Blocks.SMOOTH_STONE)
                        .group("bricked_casing")
                        .unlockedBy("has_material", has(material.getIngredientTag()))
                        .save(consumer, "toolcraft_" + casing);
            } else {
                MaterialTierData tierData = MaterialTierData.get(material);
                Tags.IOptionalNamedTag<Item> plateTag = ModTags.Items.getMaterialTag(MaterialShape.PLATE, material);
                ToolShapedRecipeBuilder.toolShaped(casing)
                        .toolUse(CraftToolType.WRENCH)
                        .smithLevel(tierData.getSmithLevel())
                        .pattern("XXX")
                        .pattern("X X")
                        .pattern("YYY")
                        .define('X', plateTag)
                        .define('Y', Blocks.BRICKS)
                        .group("bricked_casing")
                        .unlockedBy("has_material", has(material.getIngredientTag()))
                        .save(consumer, "toolcraft_" + casing);
            }
        });
    }
    private static void addLockerRecipes(Consumer<IFinishedRecipe> consumer){
        MaterialBlockData.getValidCasingData().forEach((blockData) -> {
            Materials material = blockData.getMaterial();
            Tags.IOptionalNamedTag<Item> plateTag = ModTags.Items.getMaterialTag(MaterialShape.PLATE, material);
            ToolShapedRecipeBuilder.toolShaped(MachineBlocks.locker.get())
                    .nbt((tag) -> {
                        CompoundNBT blockTag = new CompoundNBT();
                        blockTag.putString(ModConstant.Material, material.getName());
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
                    .save(consumer, "toolcraft_locker_" + material);
        });
    }
    private static void addMachineBlockRecipes(Consumer<IFinishedRecipe> consumer){
        MachineBlocks.machine_casing.forEach((material, blockRegistry) -> {
            Item casing = blockRegistry.get().asItem();
            ToolShapedRecipeBuilder.toolShaped(MachineBlocks.machineBlock.get())
                    .nbt((tag) -> {
                        CompoundNBT blockTag = new CompoundNBT();
                        blockTag.putString(ModConstant.Material, material.getName());
                        tag.put("BlockEntityTag", blockTag);
                    })
                    .toolUse(CraftToolType.WRENCH)
                    .pattern("X")
                    .define('X', casing)
                    .group("machine_machine_block")
                    .unlockedBy("has_machine_casing", has(casing))
                    .save(consumer, "toolcraft_machine_block_" + material);
        });
    }
    private static void addToolRecipes(Consumer<IFinishedRecipe> consumer){
        addPickaxeRecipes(consumer);
        addAxeRecipes(consumer);
        addShovelRecipes(consumer);
        addHoeRecipes(consumer);
        addSwordRecipes(consumer);
        addKnifeRecipes(consumer);
        addHammerRecipes(consumer);
        addWrenchRecipes(consumer);
        addFileRecipes(consumer);
        addWireCutterRecipes(consumer);
        addScrewdriverRecipes(consumer);
        addCrowbarRecipes(consumer);
        addSawRecipes(consumer);
    }
    private static void generateToolRecipe(Consumer<IFinishedRecipe> consumer, String name, Item tool, MaterialTierData tierData, String pattern1, String pattern2, String pattern3){
        Materials material = tierData.getMaterial();
        ShapedRecipeMaker<NBTShapedRecipeBuilder> maker = NBTShapedRecipeBuilder.shaped(tool)
                .nbt((tag) -> tag.putString(ModConstant.Material, material.getName()));
        generateToolRecipe(maker, consumer, name, tierData, pattern1, pattern2, pattern3, false);
    }
    private static void generateToolRecipe(Consumer<IFinishedRecipe> consumer, String name, Item tool, MaterialTierData tierData, String pattern1, String pattern2, String pattern3, CraftToolType... toolTypes){
        Materials material = tierData.getMaterial();
        ShapedRecipeMaker<ToolShapedRecipeBuilder> maker = ToolShapedRecipeBuilder.toolShaped(tool)
                .nbt((tag) -> tag.putString(ModConstant.Material, material.getName()));
        maker.smithLevel(tierData.getSmithLevel());
        for (CraftToolType toolType : toolTypes){
            maker.toolUse(toolType);
        }
        generateToolRecipe(maker, consumer, name, tierData, pattern1, pattern2, pattern3, true);
    }
    private static void generateToolRecipe(ShapedRecipeMaker<?> maker, Consumer<IFinishedRecipe> consumer, String name, MaterialTierData tierData, String pattern1, String pattern2, String pattern3, boolean useTool){
        Materials material = tierData.getMaterial();
        checkRecipeIngredients(maker, tierData, pattern1, pattern2, pattern3);
        maker.group(name)
                .unlockedBy("has_material", has(material.getIngredientTag()))
                .save(consumer, (useTool ? "toolcraft_" : "craft_") + name + "_" + material);
    }
    private static void checkRecipeIngredients(ShapedRecipeMaker<?> maker, MaterialTierData tierData, String pattern1, String pattern2, String pattern3){
        for (String pattern : new String[]{pattern1, pattern2, pattern3}){
            if (!pattern.isEmpty()){
                maker.pattern(pattern);
            }
        }
        String check = pattern1 + pattern2 + pattern3;
        if (check.contains("X")){
            maker.define('X', tierData.getItemTier().getRepairIngredient());
        }
        if (check.contains("#")){
            maker.define('#', tierData.getItemTier().getRodIngredient());
        }
        if (check.contains("P")){
            maker.define('P', ModTags.Items.getMaterialTag(MaterialShape.PLATE, tierData.getMaterial()));
        }
        if (check.contains("R")){
            maker.define('R', ModTags.Items.getMaterialTag(MaterialShape.ROD, tierData.getMaterial()));
        }
    }
    private static void addPickaxeRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if (tierData.isByHand()){
                generateToolRecipe(consumer, "pickaxe", ModItems.pickaxe, tierData,
                        "XXX",
                        " # ",
                        " # "
                );
            } else if (material.getType() == Materials.Type.SOLID) {
                generateToolRecipe(consumer, "pickaxe", ModItems.pickaxe, tierData,
                        "PXP",
                        " # ",
                        " # ",
                        CraftToolType.HAMMER,
                        CraftToolType.FILE
                );
            } else {
                generateToolRecipe(consumer, "pickaxe", ModItems.pickaxe, tierData,
                        "XXX",
                        " # ",
                        " # ",
                        CraftToolType.FILE
                );
            }
        }
    }

    private static void addAxeRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if (tierData.isByHand()){
                generateToolRecipe(consumer, "axe", ModItems.axe, tierData,
                        "XX",
                        "X#",
                        " #"
                );
            } else if (material.getType() == Materials.Type.SOLID) {
                generateToolRecipe(consumer, "axe", ModItems.axe, tierData,
                        "PX",
                        "P#",
                        " #",
                        CraftToolType.HAMMER,
                        CraftToolType.FILE
                );
            } else {
                generateToolRecipe(consumer, "axe", ModItems.axe, tierData,
                        "XX",
                        "X#",
                        " #",
                        CraftToolType.FILE
                );
            }
        }
    }
    private static void addShovelRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if (tierData.isByHand()){
                generateToolRecipe(consumer, "shovel", ModItems.shovel, tierData,
                        "X",
                        "#",
                        "#"
                );
            } else if (material.getType() == Materials.Type.SOLID) {
                generateToolRecipe(consumer, "shovel", ModItems.shovel, tierData,
                        "P",
                        "#",
                        "#",
                        CraftToolType.HAMMER,
                        CraftToolType.FILE
                );
            } else {
                generateToolRecipe(consumer, "shovel", ModItems.shovel, tierData,
                        "X",
                        "#",
                        "#",
                        CraftToolType.FILE
                );
            }
        }
    }

    private static void addHoeRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if (tierData.isByHand()){
                generateToolRecipe(consumer, "hoe", ModItems.hoe, tierData,
                        "XX",
                        " #",
                        " #"
                );
            } else if (material.getType() == Materials.Type.SOLID) {
                generateToolRecipe(consumer, "hoe", ModItems.hoe, tierData,
                        "PP",
                        " #",
                        " #",
                        CraftToolType.HAMMER,
                        CraftToolType.FILE
                );
            } else {
                generateToolRecipe(consumer, "hoe", ModItems.hoe, tierData,
                        "XX",
                        " #",
                        " #",
                        CraftToolType.FILE
                );
            }
        }
    }
    private static void addSwordRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if (tierData.isByHand()){
                generateToolRecipe(consumer, "sword", ModItems.sword, tierData,
                        "X",
                        "X",
                        "#"
                );
            } else if (material.getType() == Materials.Type.SOLID) {
                generateToolRecipe(consumer, "sword", ModItems.sword, tierData,
                        "P",
                        "P",
                        "#",
                        CraftToolType.HAMMER,
                        CraftToolType.FILE
                );
            } else {
                generateToolRecipe(consumer, "sword", ModItems.sword, tierData,
                        "X",
                        "X",
                        "#",
                        CraftToolType.FILE
                );
            }
        }
    }
    private static void addKnifeRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if (tierData.isByHand()){
                generateToolRecipe(consumer, "knife", ModItems.knife, tierData,
                        "X",
                        "#",
                        ""
                );
            } else if (material.getType() == Materials.Type.SOLID) {
                generateToolRecipe(consumer, "knife", ModItems.knife, tierData,
                        "P",
                        "R",
                        "",
                        CraftToolType.HAMMER,
                        CraftToolType.FILE
                );
            } else if (material.getType() == Materials.Type.GEM) {
                generateToolRecipe(consumer, "knife", ModItems.knife, tierData,
                        "X",
                        "R",
                        "",
                        CraftToolType.FILE
                );
            }
        }
    }
    private static void addHammerRecipes(Consumer<IFinishedRecipe> consumer){
        // OTHER类里可以做锤子的材料
        Set<Materials> list = Sets.newHashSet(
                Materials.WOOD,
                Materials.STONE
        );
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if(tierData.isSoft()){
                // 添加软锤
                generateToolRecipe(consumer, "soft_mallet", ModItems.softMallet, tierData,
                        "XX ",
                        "XX#",
                        "XX "
                );
            }else if(material.getType() != Materials.Type.OTHER || list.contains(material)){
                // 添加锤子配方
                generateToolRecipe(consumer, "hammer", ModItems.hammer, tierData,
                        "XX ",
                        "XX#",
                        "XX "
                );
            }
        }
    }
    private static void addWrenchRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if (!tierData.isSoft() && !tierData.isByHand() && material.getType() == Materials.Type.SOLID) {
                // 添加扳手配方
                generateToolRecipe(consumer, "wrench", ModItems.wrench, tierData,
                        "X X",
                        "XXX",
                        " X ",
                        CraftToolType.HAMMER
                );
            }
        }
    }
    private static void addFileRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if (!tierData.isSoft() && !tierData.isByHand() && material.getType() == Materials.Type.SOLID) {
                // 添加锉刀配方
                generateToolRecipe(consumer, "file", ModItems.file, tierData,
                        "P  ",
                        " P ",
                        "  #"
                );
            }
        }
    }
    private static void addWireCutterRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if (!tierData.isSoft() && !tierData.isByHand() && material.getType() == Materials.Type.SOLID) {
                // 添加剪线钳配方
                generateToolRecipe(consumer, "wire_cutter", ModItems.wireCutter, tierData,
                        "P P",
                        " P ",
                        "R R",
                        CraftToolType.HAMMER,
                        CraftToolType.FILE,
                        CraftToolType.SCREWDRIVER
                );
            }
        }
    }
    private static void addScrewdriverRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if (!tierData.isSoft() && !tierData.isByHand() && material.getType() == Materials.Type.SOLID) {
                // 添加螺丝刀配方
                generateToolRecipe(consumer, "screwdriver", ModItems.screwdriver, tierData,
                        "  R",
                        " R ",
                        "#  ",
                        CraftToolType.HAMMER,
                        CraftToolType.FILE
                );
            }
        }
    }

    private static void addCrowbarRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if (!tierData.isSoft() && !tierData.isByHand() && material.getType() == Materials.Type.SOLID) {
                // 添加撬棍配方
                generateToolRecipe(consumer, "crowbar", ModItems.crowbar, tierData,
                        "  R",
                        " R ",
                        "R  ",
                        CraftToolType.HAMMER,
                        CraftToolType.FILE
                );
            }
        }
    }
    private static void addSawRecipes(Consumer<IFinishedRecipe> consumer){
        for(MaterialTierData tierData : MaterialTierData.getValidTierData()){
            Materials material = tierData.getMaterial();
            if (!tierData.isSoft() && !tierData.isByHand() && material.getType() == Materials.Type.SOLID) {
                // 添加锯子配方
                generateToolRecipe(consumer, "saw", ModItems.saw, tierData,
                        "###",
                        "PP#",
                        "",
                        CraftToolType.HAMMER,
                        CraftToolType.FILE
                );
            }
        }
    }
}
