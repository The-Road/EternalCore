package com.road.eternalcore.data.recipes.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.road.eternalcore.common.item.crafting.IModRecipeSerializer;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class SmithingRecipeBuilder extends MultiResultRecipeBuilder{
    protected int smithingLevel;
    protected Pair<String, Integer> toolUse;
    protected final List<Ingredient> ingredients = Lists.newArrayList();

    public SmithingRecipeBuilder(IItemProvider item, int count, Consumer<CompoundNBT> nbtConsumer){
        super(item, count, nbtConsumer);
        this.smithingLevel = 0;
    }
    public static SmithingRecipeMaker<SmithingRecipeBuilder> smith(IItemProvider item){
        return smith(item, 1);
    }
    public static SmithingRecipeMaker<SmithingRecipeBuilder> smith(IItemProvider item, int count){
        return smith(item, count, (nbt) -> {});
    }
    public static SmithingRecipeMaker<SmithingRecipeBuilder> smith(IItemProvider item, int count, Consumer<CompoundNBT> nbtConsumer){
        return new SmithingRecipeMaker<>(new SmithingRecipeBuilder(item, count, nbtConsumer));
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        saveId(id);
        consumer.accept(new Result(
                id,
                this.results,
                this.smithingLevel,
                this.toolUse,
                this.group == null ? "" : this.group,
                this.ingredients,
                this.advancement,
                new ResourceLocation(id.getNamespace(), "recipes/" + this.results.get(0).item.getItemCategory().getRecipeFolderName() + "/" + id.getPath())
        ));
    }
    protected static class Result extends MultiResultRecipeBuilder.Result{
        protected int smithingLevel;
        protected final List<Ingredient> ingredients;
        protected final Pair<String, Integer> toolUse;
        public Result(ResourceLocation id, List<ResultData> results, int smithingLevel, Pair<String, Integer> toolUse, String group, List<Ingredient> ingredients, Advancement.Builder advancement, ResourceLocation advancementId) {
            super(id, results, group, advancement, advancementId);
            this.smithingLevel = smithingLevel;
            this.ingredients = ingredients;
            this.toolUse = toolUse;
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
