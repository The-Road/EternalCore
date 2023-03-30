package com.road.eternalcore.common.item.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.road.eternalcore.common.block.ModBlocks;
import com.road.eternalcore.common.inventory.SmithingTableInventory;
import com.road.eternalcore.common.item.crafting.IModRecipeSerializer;
import com.road.eternalcore.common.item.crafting.IModRecipeType;
import com.road.eternalcore.common.item.tool.CraftToolType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SmithingRecipe implements IRecipe<SmithingTableInventory>, IToolUsedRecipe {
    private final ResourceLocation id;
    private final String group;
    private final int smithingLevel;
    private final NonNullList<Ingredient> recipeItems;
    private final Pair<CraftToolType, Integer> toolItemUse;
    private final ItemStack result;
    private final boolean isSimple; // 参考ShapelessRecipe，如果合成材料有Damageable则为false

    public SmithingRecipe(ResourceLocation id, String group, int smithingLevel, NonNullList<Ingredient> recipeItems, Pair<CraftToolType, Integer> toolItemUse, ItemStack result){
        this.id = id;
        this.group = group;
        this.smithingLevel = smithingLevel;
        this.recipeItems = recipeItems;
        this.toolItemUse = toolItemUse;
        this.result = result;
        this.isSimple = recipeItems.stream().allMatch(Ingredient::isSimple);
    }

    public boolean matches(SmithingTableInventory inventory, World world) {
        // 判断当前合成站点的锻造等级是否满足配方
        if (inventory.getSmithingLevel() < smithingLevel){
            return false;
        }
        // 0号位为工具栏位，判断工具和工具的锻造等级是否满足配方
        ItemStack toolItem = inventory.getItem(0);
        if (!toolMatch(toolItemUse, toolItem)) {
            return false;
        }
        if (toolSmithLevel(toolItem) < smithingLevel){
            return false;
        }
        // 剩下的格子参考ShapelessRecipe的规则进行匹配
        RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
        List<ItemStack> inputItems = new ArrayList<>();
        int slotItemCount = 0;
        for (int i = 1; i < inventory.getContainerSize(); i++){
            ItemStack item = inventory.getItem(i);
            if (!item.isEmpty()){
                slotItemCount++;
                if (isSimple){
                    recipeItemHelper.accountStack(item, 1);
                }else{
                    inputItems.add(item);
                }
            }
        }
        return slotItemCount == recipeItems.size() &&
                (isSimple ? recipeItemHelper.canCraft(this, null) :
                        RecipeMatcher.findMatches(inputItems, recipeItems) != null);
    }

    public ItemStack assemble(SmithingTableInventory inventory) {
        ItemStack resultItem = getResultItem().copy();
        return resultItem;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= recipeItems.size() + 1;
    }

    public ItemStack getResultItem() {
        return result;
    }
    // 仅保留使用的工具的数据，剩下的材料直接-1
    public NonNullList<ItemStack> getRemainingItems(SmithingTableInventory inputSlots) {
        ItemStack toolRemainItem = getToolRemainItem(toolItemUse.getSecond(), inputSlots.getItem(0));
        NonNullList<ItemStack> remainItems = NonNullList.withSize(inputSlots.getContainerSize(), ItemStack.EMPTY);
        if (toolRemainItem != null && !toolRemainItem.isEmpty()){
            remainItems.set(0, toolRemainItem);
        }
        return remainItems;
    }

    public NonNullList<Ingredient> getIngredients() {
        return IRecipe.super.getIngredients();
    }

    public String getGroup() {
        return IRecipe.super.getGroup();
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.smithingTable);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public IRecipeSerializer<?> getSerializer() {
        return IModRecipeSerializer.smithingRecipe;
    }

    public IRecipeType<?> getType() {
        return IModRecipeType.SMITHING;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SmithingRecipe> {

        public SmithingRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = JSONUtils.getAsString(json, "group", "");
            int level = JSONUtils.getAsInt(json, "level");
            NonNullList<Ingredient> recipeItems = itemsFromJson(JSONUtils.getAsJsonArray(json, "ingredients"));
            Pair<CraftToolType, Integer> toolItemUse = IToolUsedRecipe.toolUseFromJson(JSONUtils.getAsJsonObject(json, "toolUse"));
            ItemStack result = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "result"), true);
            return new SmithingRecipe(id, group, level, recipeItems, toolItemUse, result);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> items = NonNullList.create();
            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (!ingredient.isEmpty()) {
                    items.add(ingredient);
                }
            }
            return items;
        }

        @Nullable
        public SmithingRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
            String group = buffer.readUtf();
            int level = buffer.readVarInt();
            int size = buffer.readVarInt();
            NonNullList<Ingredient> recipeItems = NonNullList.withSize(size, Ingredient.EMPTY);
            for (int i = 0; i < size; i++){
                recipeItems.set(i, Ingredient.fromNetwork(buffer));
            }
            CraftToolType tool = CraftToolType.get(buffer.readUtf());
            int use = buffer.readVarInt();
            Pair<CraftToolType, Integer> toolUse = new Pair<>(tool, use);
            ItemStack result = buffer.readItem();
            return new SmithingRecipe(id, group, level, recipeItems, toolUse, result);
        }

        public void toNetwork(PacketBuffer buffer, SmithingRecipe recipe) {
            // 写入配方数据
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(recipe.smithingLevel);
            buffer.writeVarInt(recipe.recipeItems.size());
            for (Ingredient ingredient : recipe.recipeItems){
                ingredient.toNetwork(buffer);
            }
            // 写入工具数据
            Pair<CraftToolType, Integer> pair = recipe.toolItemUse;
            buffer.writeUtf(pair.getFirst().getName());
            buffer.writeInt(pair.getSecond());
            // 写入产物数据
            buffer.writeItem(recipe.result);
        }
    }
}
