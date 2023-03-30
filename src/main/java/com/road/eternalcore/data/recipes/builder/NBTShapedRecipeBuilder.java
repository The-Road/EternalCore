package com.road.eternalcore.data.recipes.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class NBTShapedRecipeBuilder extends RecipeBuilder {
    // RecipeBuilder里全是private字段，太弱智了，只能整个复制一遍
    // 注意此处的save用的是ModResourceLocation
    protected static final Logger LOGGER = LogManager.getLogger(); // 不知道干啥的，总之先留着
    protected final List<String> rows = Lists.newArrayList();
    protected final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    public NBTShapedRecipeBuilder(IItemProvider item, int count) {
        super(item, count);
    }
    public static IShapedRecipeBuilder<NBTShapedRecipeBuilder> shaped(IItemProvider item){
        return shaped(item, 1);
    }
    public static IShapedRecipeBuilder<NBTShapedRecipeBuilder> shaped(IItemProvider item, int count){
        return new IShapedRecipeBuilder<>(new NBTShapedRecipeBuilder(item, count));
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
                this.count,
                this.group == null ? "" : this.group,
                this.rows,
                this.key,
                this.advancement,
                new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + id.getPath())
        ));
    }
    protected void ensureValid(ResourceLocation id) {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + id + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.key.keySet());
            set.remove(' ');
            for(String s : this.rows) {
                for(int i = 0; i < s.length(); ++i) {
                    char c0 = s.charAt(i);
                    if (!this.key.containsKey(c0) && c0 != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + id + " uses undefined symbol '" + c0 + "'");
                    }

                    set.remove(c0);
                }
            }

            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + id);
            } else if (this.rows.size() == 1 && this.rows.get(0).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + id + " only takes in a single item - should it be a shapeless recipe instead?");
            } else if (this.advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
    protected class Result extends RecipeBuilder.Result implements IFinishedRecipe {
        protected final List<String> pattern;
        protected final Map<Character, Ingredient> key;

        public Result(ResourceLocation id, Item result, CompoundNBT nbt, int count, String group, List<String> pattern, Map<Character, Ingredient> key, Advancement.Builder advancement, ResourceLocation advancementId) {
            super(id, result, nbt, count, group, advancement, advancementId);
            this.pattern = pattern;
            this.key = key;
        }

        public void serializeRecipeData(JsonObject json) {
            serializeRecipeItems(json);
            serializeRecipeResult(json);
        }
        protected void serializeRecipeItems(JsonObject json){
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            JsonArray patternJson = new JsonArray();
            for(String s : this.pattern) {
                patternJson.add(s);
            }
            json.add("pattern", patternJson);
            JsonObject keyJson = new JsonObject();
            for(Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
                keyJson.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
            }
            json.add("key", keyJson);
        }

        public IRecipeSerializer<?> getType() {
            return IRecipeSerializer.SHAPED_RECIPE;
        }

        public ResourceLocation getId() {
            return this.id;
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
