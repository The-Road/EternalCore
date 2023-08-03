package com.road.eternalcore.common.item.crafting.recipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.road.eternalcore.common.block.ModBlocks;
import com.road.eternalcore.common.inventory.ToolCraftingInventory;
import com.road.eternalcore.common.item.crafting.IModRecipeSerializer;
import com.road.eternalcore.api.tool.CraftToolType;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ToolShapedRecipe implements IToolCraftingRecipe, IToolUsedRecipe {
    private final ResourceLocation id;
    private final String group;
    private final int width;
    private final int height;
    private final NonNullList<Ingredient> recipeItems;
    private final NonNullList<Pair<CraftToolType, Integer>> toolItemUses;
    private final ItemStack result;

    public ToolShapedRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> recipeItems, NonNullList<Pair<CraftToolType, Integer>> toolItemUses, ItemStack result) {
        this.id = id;
        this.group = group;
        this.width = width;
        this.height = height;
        this.recipeItems = recipeItems;
        this.toolItemUses = toolItemUses;
        this.result = result;
    }

    public boolean matches(ToolCraftingInventory craftAndToolSlots, World world) {
        return toolSlotsMatch(craftAndToolSlots.getToolSlots()) != null && craftSlotsMatch(craftAndToolSlots.getCraftSlots());
    }

    private List<Integer> toolSlotsMatch(CraftingInventory toolSlots){
        return toolMatch(toolItemUses, toolSlots);
    }
    private boolean craftSlotsMatch(CraftingInventory craftSlots) {
        // 判断是否满足合成表（这部分和有序合成的代码相同）
        for(int i = 0; i <= craftSlots.getWidth() - this.width; ++i) {
            for(int j = 0; j <= craftSlots.getHeight() - this.height; ++j) {
                if (craftSlotsMatch(craftSlots, i, j, true)) {
                    return true;
                }
                if (craftSlotsMatch(craftSlots, i, j, false)) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean craftSlotsMatch(CraftingInventory craftSlots, int lefttopX, int lefttopY, boolean reverse) {
        for(int i = 0; i < craftSlots.getWidth(); ++i) {
            for(int j = 0; j < craftSlots.getHeight(); ++j) {
                int k = i - lefttopX;
                int l = j - lefttopY;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (reverse) {
                        ingredient = this.recipeItems.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.recipeItems.get(k + l * this.width);
                    }
                }

                if (!ingredient.test(craftSlots.getItem(i + j * craftSlots.getWidth()))) {
                    return false;
                }
            }
        }

        return true;
    }

    public ItemStack assemble(ToolCraftingInventory craftAndToolSlots) {
        return this.getResultItem().copy();
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    public ItemStack getResultItem() {
        return this.result;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public IRecipeSerializer<?> getSerializer() {
        return IModRecipeSerializer.toolShapedRecipe;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(ToolCraftingInventory craftAndToolSlots) {
        NonNullList<ItemStack> remainItems = NonNullList.withSize(craftAndToolSlots.getContainerSize(), ItemStack.EMPTY);
        // 合成的材料按照正常的合成逻辑消耗并返还
        CraftingInventory craftSlots = craftAndToolSlots.getCraftSlots();
        int craftSlotsSize = craftSlots.getContainerSize();
        Consumer<Integer> normalRemainingItem = i -> {
            ItemStack craftItem = craftAndToolSlots.getItem(i);
            if (craftItem.hasContainerItem()) {
                remainItems.set(i, craftItem.getContainerItem());
            }
        };
        for(int i = 0; i < craftSlotsSize; ++i) {
            normalRemainingItem.accept(i);
        }
        // 合成的工具则会按照该配方消耗工具的耐久度给工具的durabilityDecimal加上对应的数值
        // 之后在ToolCraftingResultSlot中进行工具耐久的更新
        // 如果工具栏里对应的东西没有耐久度，则会像合成材料一样的逻辑消耗（固定消耗一个）
        CraftingInventory toolSlots = craftAndToolSlots.getToolSlots();
        List<ItemStack> toolRemainItems = getToolRemainItems(toolItemUses, toolSlots);
        for (int i = 0; i < toolRemainItems.size(); i++){
            ItemStack toolRemainItem = toolRemainItems.get(i);
            if (toolRemainItem != null){
                if (toolRemainItem.isEmpty()){
                    normalRemainingItem.accept(i + craftSlotsSize);
                }else{
                    remainItems.set(i + craftSlotsSize, toolRemainItem);
                }
            }
        }
        return remainItems;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }
    public String getGroup() {
        return this.group;
    }
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.handcraftAssemblyTable);
    }
    // =======================
    // 以下为Serializer用到的函数
    // =======================
    protected static Map<String, Ingredient> keyFromJson(JsonObject json){
        Map<String, Ingredient> map = Maps.newHashMap();
        for(Map.Entry<String, JsonElement> entry : json.entrySet()){
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }
            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }
            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
        }
        map.put(" ", Ingredient.EMPTY);
        return map;
    }
    protected static String[] patternFromJson(JsonArray json) {
        String[] astring = new String[json.size()];
        if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < astring.length; ++i) {
                String s = JSONUtils.convertToString(json.get(i), "pattern[" + i + "]");
                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }
                astring[i] = s;
            }
            return astring;
        }
    }
    protected static NonNullList<Ingredient> dissolvePattern(String[] pattern, Map<String, Ingredient> keyMap, int width, int height) {
        NonNullList<Ingredient> dPattern = NonNullList.withSize(width * height, Ingredient.EMPTY);
        Set<String> check = Sets.newHashSet(keyMap.keySet());
        check.remove(" ");
        for(int i = 0; i < pattern.length; ++i) {
            for(int j = 0; j < pattern[i].length(); ++j) {
                String s = pattern[i].substring(j, j + 1);
                Ingredient ingredient = keyMap.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }
                check.remove(s);
                dPattern.set(j + width * i, ingredient);
            }
        }
        if (!check.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + check);
        } else {
            return dPattern;
        }
    }
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ToolShapedRecipe> {

        public ToolShapedRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = JSONUtils.getAsString(json, "group", "");
            Map<String, Ingredient> keyMap = ToolShapedRecipe.keyFromJson(JSONUtils.getAsJsonObject(json, "key"));
            String[] pattern = ToolShapedRecipe.patternFromJson(JSONUtils.getAsJsonArray(json, "pattern"));
            NonNullList<Pair<CraftToolType, Integer>> toolUses = IToolUsedRecipe.toolUseFromJson(JSONUtils.getAsJsonArray(json, "toolUse"));
            int width = pattern[0].length();
            int height = pattern.length;
            NonNullList<Ingredient> recipeItems = ToolShapedRecipe.dissolvePattern(pattern, keyMap, width, height);
            ItemStack result = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "result"), true);
            return new ToolShapedRecipe(id, group, width, height, recipeItems, toolUses, result);
        }
        public ToolShapedRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
            // 读取配方数据
            int width = buffer.readVarInt();
            int height = buffer.readVarInt();
            String group = buffer.readUtf();
            NonNullList<Ingredient> recipeItems = NonNullList.withSize(width * height, Ingredient.EMPTY);
            for(int i = 0; i < recipeItems.size(); i++){
                recipeItems.set(i, Ingredient.fromNetwork(buffer));
            }
            // 读取工具数据
            int toolNum = buffer.readVarInt();
            NonNullList<Pair<CraftToolType, Integer>> toolUses = NonNullList.create();
            for(int i = 0; i < toolNum; i++){
                CraftToolType tool = CraftToolType.get(buffer.readUtf());
                int use = buffer.readVarInt();
                toolUses.add(Pair.of(tool, use));
            }
            // 读取产物数据
            ItemStack result = buffer.readItem();
            return new ToolShapedRecipe(id, group, width, height, recipeItems, toolUses, result);
        }

        public void toNetwork(PacketBuffer buffer, ToolShapedRecipe recipe) {
            // 写入配方数据
            buffer.writeInt(recipe.width);
            buffer.writeInt(recipe.height);
            buffer.writeUtf(recipe.group);
            for(Ingredient ingredient : recipe.recipeItems){
                ingredient.toNetwork(buffer);
            }
            // 写入工具数据
            buffer.writeInt(recipe.toolItemUses.size());
            for(Pair<CraftToolType, Integer> pair : recipe.toolItemUses){
                buffer.writeUtf(pair.getFirst().getName());
                buffer.writeInt(pair.getSecond());
            }
            // 写入产物数据
            buffer.writeItem(recipe.result);
        }
    }
}
