package com.road.eternalcore.data.recipes.provider;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.road.eternalcore.ModConstant;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.item.ModItems;
import com.road.eternalcore.common.item.crafting.ingredient.NBTIngredient;
import com.road.eternalcore.data.recipes.builder.DisabledRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn){
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer){
        adjustVanillaRecipes(consumer);
        CraftingRecipeProvider.addRecipes(consumer);
        PartCraftingRecipeProvider.addRecipes(consumer);
        OreProcessRecipeProvider.addRecipes(consumer);
    }
    private void adjustVanillaRecipes(Consumer<IFinishedRecipe> consumer){
        Consumer<String> remove = (id) -> DisabledRecipeBuilder.disabled(consumer, id);
        // 修改原版的工具配方
        List<String> remove_tools = Lists.newArrayList("pickaxe", "axe", "shovel", "hoe", "sword");
        for (String tool : remove_tools){
            remove.accept("netherite_" + tool + "_smithing");
        }
        Map<Item, Pair<Item, Materials>> toolReference = new HashMap<>();
        toolReference.put(Items.WOODEN_PICKAXE, Pair.of(ModItems.pickaxe, Materials.WOOD));
        toolReference.put(Items.STONE_PICKAXE, Pair.of(ModItems.pickaxe, Materials.STONE));
        toolReference.put(Items.IRON_PICKAXE, Pair.of(ModItems.pickaxe, Materials.IRON));
        toolReference.put(Items.GOLDEN_PICKAXE, Pair.of(ModItems.pickaxe, Materials.GOLD));
        toolReference.put(Items.DIAMOND_PICKAXE, Pair.of(ModItems.pickaxe, Materials.DIAMOND));
        toolReference.put(Items.NETHERITE_PICKAXE, Pair.of(ModItems.pickaxe, Materials.NETHERITE));
        toolReference.put(Items.WOODEN_AXE, Pair.of(ModItems.axe, Materials.WOOD));
        toolReference.put(Items.STONE_AXE, Pair.of(ModItems.axe, Materials.STONE));
        toolReference.put(Items.IRON_AXE, Pair.of(ModItems.axe, Materials.IRON));
        toolReference.put(Items.GOLDEN_AXE, Pair.of(ModItems.axe, Materials.GOLD));
        toolReference.put(Items.DIAMOND_AXE, Pair.of(ModItems.axe, Materials.DIAMOND));
        toolReference.put(Items.NETHERITE_AXE, Pair.of(ModItems.axe, Materials.NETHERITE));
        toolReference.put(Items.WOODEN_SHOVEL, Pair.of(ModItems.shovel, Materials.WOOD));
        toolReference.put(Items.STONE_SHOVEL, Pair.of(ModItems.shovel, Materials.STONE));
        toolReference.put(Items.IRON_SHOVEL, Pair.of(ModItems.shovel, Materials.IRON));
        toolReference.put(Items.GOLDEN_SHOVEL, Pair.of(ModItems.shovel, Materials.GOLD));
        toolReference.put(Items.DIAMOND_SHOVEL, Pair.of(ModItems.shovel, Materials.DIAMOND));
        toolReference.put(Items.NETHERITE_SHOVEL, Pair.of(ModItems.shovel, Materials.NETHERITE));
        toolReference.put(Items.WOODEN_HOE, Pair.of(ModItems.hoe, Materials.WOOD));
        toolReference.put(Items.STONE_HOE, Pair.of(ModItems.hoe, Materials.STONE));
        toolReference.put(Items.IRON_HOE, Pair.of(ModItems.hoe, Materials.IRON));
        toolReference.put(Items.GOLDEN_HOE, Pair.of(ModItems.hoe, Materials.GOLD));
        toolReference.put(Items.DIAMOND_HOE, Pair.of(ModItems.hoe, Materials.DIAMOND));
        toolReference.put(Items.NETHERITE_HOE, Pair.of(ModItems.hoe, Materials.NETHERITE));
        toolReference.put(Items.WOODEN_SWORD, Pair.of(ModItems.sword, Materials.WOOD));
        toolReference.put(Items.STONE_SWORD, Pair.of(ModItems.sword, Materials.STONE));
        toolReference.put(Items.IRON_SWORD, Pair.of(ModItems.sword, Materials.IRON));
        toolReference.put(Items.GOLDEN_SWORD, Pair.of(ModItems.sword, Materials.GOLD));
        toolReference.put(Items.DIAMOND_SWORD, Pair.of(ModItems.sword, Materials.DIAMOND));
        toolReference.put(Items.NETHERITE_SWORD, Pair.of(ModItems.sword, Materials.NETHERITE));
        toolReference.forEach((vanillaTool, pair) -> {
            ItemStack modTool = new ItemStack(pair.getFirst());
            modTool.getOrCreateTag().putString(ModConstant.Material, pair.getSecond().getName());
            ShapelessRecipeBuilder.shapeless(vanillaTool)
                    .requires(NBTIngredient.of(modTool))
                    .unlockedBy("has_material", has(pair.getSecond().getIngredientTag()))
                    .save(consumer);
        });
        // 移除原版的熔炼矿石配方（因为和OreProcessRecipeProvider添加的配方重复了）
        List<String> remove_from_ = Lists.newArrayList(
                "iron_ingot", "gold_ingot", "quartz", "netherite_scrap");
        remove_from_.forEach(str -> {
            remove.accept(str);
            remove.accept(str + "_from_blasting");
        });
        List<String> remove_from_smelt = Lists.newArrayList(
                "coal", "lapis", "redstone", "diamond", "emerald");
        remove_from_smelt.forEach(str -> {
            remove.accept(str + "_from_smelting");
            remove.accept(str + "_from_blasting");
        });
        remove.accept("netherite_ingot");
    }
}
