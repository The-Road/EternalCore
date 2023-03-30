package com.road.eternalcore.data.recipes.builder;

import com.google.common.collect.Lists;
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
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class SmithingRecipeBuilder extends RecipeBuilder{
    protected int smithingLevel;
    protected Pair<String, Integer> toolUse;
    protected final List<Ingredient> ingredients = Lists.newArrayList();

    public SmithingRecipeBuilder(IItemProvider item, int count){
        super(item, count);
        this.smithingLevel = 0;
    }
    public static ISmithingRecipeBuilder<SmithingRecipeBuilder> smith(IItemProvider item){
        return smith(item, 1);
    }
    public static ISmithingRecipeBuilder<SmithingRecipeBuilder> smith(IItemProvider item, int count){
        return new ISmithingRecipeBuilder<>(new SmithingRecipeBuilder(item, count));
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
                this.smithingLevel,
                this.toolUse,
                this.count,
                this.group == null ? "" : this.group,
                this.ingredients,
                this.advancement,
                new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + id.getPath())
        ));
    }
    protected void ensureValid(ResourceLocation id){

    }
    protected class Result extends RecipeBuilder.Result implements IFinishedRecipe {
        protected int smithingLevel;
        protected final List<Ingredient> ingredients;
        protected final Pair<String, Integer> toolUse;
        public Result(ResourceLocation id, Item result, CompoundNBT nbt, int smithingLevel, Pair<String, Integer> toolUse, int count, String group, List<Ingredient> ingredients, Advancement.Builder advancement, ResourceLocation advancementId) {
            super(id, result, nbt, count, group, advancement, advancementId);
            this.smithingLevel = smithingLevel;
            this.ingredients = ingredients;
            this.toolUse = toolUse;
        }

        public void serializeRecipeData(JsonObject json) {
            serializeRecipeItems(json);
            serializeRecipeResult(json);
        }
        protected void serializeRecipeItems(JsonObject json){
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            json.addProperty("level", this.smithingLevel);
            JsonArray ingredients = new JsonArray();
            for(Ingredient ingredient : this.ingredients) {
                ingredients.add(ingredient.toJson());
            }
            json.add("ingredients", ingredients);

            JsonObject toolUseJson = new JsonObject();
            toolUseJson.addProperty("tool", toolUse.getFirst());
            toolUseJson.addProperty("use", toolUse.getSecond());
            json.add("toolUse", toolUseJson);
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public IRecipeSerializer<?> getType() {
            return IModRecipeSerializer.smithingRecipe;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }

}
