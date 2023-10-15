package com.road.eternalcore.common.item.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.road.eternalcore.api.tool.CraftToolType;
import com.road.eternalcore.common.item.tool.CustomTierItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface IToolUsedRecipe {
    // 提供判断工具类型和耐久消耗相关的接口
    Pair<CraftToolType, Integer> getToolUse(int index);

    default List<Integer> getToolDamage(List<Pair<CraftToolType, Integer>> toolUses, IInventory slots) {
        // 如果满足了所有的工具需求，则返回每个格子的工具对应的耐久消耗，否则返回null
        int toolSize = slots.getContainerSize();
        int[] array = new int[toolSize];
        boolean matches = true;
        for (Pair<CraftToolType, Integer> pair : toolUses){
            boolean match = false;
            for (int i = 0; i < toolSize; i++) {
                if (isToolMatch(pair, slots.getItem(i))) {
                    array[i] += pair.getSecond();
                    match = true;
                    break;
                }
            }
            if (!match){
                matches = false;
            }
        }
        return matches ? Arrays.stream(array).boxed().collect(Collectors.toList()) : null;
    }
    default boolean isToolMatch(Pair<CraftToolType, Integer> toolUse, ItemStack item){
        // 对于特定格子是否为对应工具的判断
        return toolUse.getFirst().match(item);
    }

    default List<ItemStack> getToolRemainItems(List<Pair<CraftToolType, Integer>> toolUses, IInventory slots){
        // 返回每个格子对应的耐久损耗后的物品（如果不为Damageable则返回ItemStack.EMPTY）
        // 为null则表示该格不消耗物品
        List<Integer> toolDamages = getToolDamage(toolUses, slots);
        if (toolDamages != null){
            List<ItemStack> remainItems = new ArrayList<>(toolDamages.size());
            for (int i = 0; i < toolDamages.size(); i++) {
                if (toolDamages.get(i) != null) {
                    ItemStack toolItem = slots.getItem(i);
                    remainItems.add(getToolRemainItem(toolDamages.get(i), toolItem));
                }else{
                    remainItems.add(null);
                }
            }
            return remainItems;
        }else{
            throw new RuntimeException(this + " try to get tool remain items but tools don't match");
        }
    }
    default ItemStack getToolRemainItem(int toolDamage, ItemStack toolItem){
        // 返回消耗耐久后的物品
        if (toolItem.isDamageableItem()){
            ItemStack toolItemCopy = toolItem.copy();
            CustomTierItem.addItemDamageNoUpdate(toolItemCopy, toolDamage);
            return toolItemCopy;
        }else{
            return ItemStack.EMPTY;
        }
    }

    static NonNullList<Pair<CraftToolType, Integer>> toolUseFromJson(JsonArray json){
        NonNullList<Pair<CraftToolType, Integer>> toolItemUses = NonNullList.create();
        for(JsonElement jsonElement : json){
            toolItemUses.add(toolUseFromJson(jsonElement));
        }
        return toolItemUses;
    }
    static Pair<CraftToolType, Integer> toolUseFromJson(JsonElement jsonElement){
        try {
            JsonObject toolUse = jsonElement.getAsJsonObject();
            CraftToolType tool = CraftToolType.get(JSONUtils.getAsString(toolUse, "tool"));
            int use = CustomTierItem.DEFAULT_DURABILITY_SUBDIVIDE;
            if (toolUse.has("use")) {
                use = JSONUtils.getAsInt(toolUse, "use");
            }
            return Pair.of(tool, use);
        } catch (Exception e){
            throw new JsonSyntaxException("toolUse elements must be {\"tool\": xxx, \"use\": xxx}");
        }
    }
}
