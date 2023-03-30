package com.road.eternalcore.data.recipes.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.road.eternalcore.common.item.crafting.IModRecipeSerializer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ToolShapedRecipeBuilder extends NBTShapedRecipeBuilder {
    protected final NonNullList<Pair<String, Integer>> toolUses = NonNullList.create();
    public ToolShapedRecipeBuilder(IItemProvider item, int count) {
        super(item, count);
    }
    public static IShapedRecipeBuilder<ToolShapedRecipeBuilder> toolShaped(IItemProvider item){
        return toolShaped(item, 1);
    }
    public static IShapedRecipeBuilder<ToolShapedRecipeBuilder> toolShaped(IItemProvider item, int count){
        return new IShapedRecipeBuilder<>(new ToolShapedRecipeBuilder(item, count));
    }
    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        this.ensureValid(id);
        this.advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(IRequirementsStrategy.OR);
        consumer.accept(new Result(
                id,
                this.result,
                this.nbt,
                this.toolUses,
                this.count,
                this.group == null ? "" : this.group,
                this.rows,
                this.key,
                this.advancement,
                new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + id.getPath())
        ));
    }
    protected class Result extends NBTShapedRecipeBuilder.Result{
        protected final NonNullList<Pair<String, Integer>> toolUses;
        public Result(ResourceLocation id, Item result, CompoundNBT nbt, NonNullList<Pair<String, Integer>> toolUses, int count, String group, List<String> pattern, Map<Character, Ingredient> key, Advancement.Builder advancement, ResourceLocation advancementId) {
            super(id, result, nbt, count, group, pattern, key, advancement, advancementId);
            this.toolUses = toolUses;
        }
        protected void serializeRecipeItems(JsonObject json){
            super.serializeRecipeItems(json);
            JsonArray toolUseJson = new JsonArray();
            for(Pair<String, Integer> toolUse : this.toolUses){
                JsonObject elementJson = new JsonObject();
                elementJson.addProperty("tool", toolUse.getFirst());
                elementJson.addProperty("use", toolUse.getSecond());
                toolUseJson.add(elementJson);
            }
            json.add("toolUse", toolUseJson);
        }
        public IRecipeSerializer<?> getType() {
            return IModRecipeSerializer.toolShapedRecipe;
        }
    }
}
