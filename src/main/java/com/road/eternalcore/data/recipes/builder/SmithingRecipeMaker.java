package com.road.eternalcore.data.recipes.builder;

import com.mojang.datafixers.util.Pair;
import com.road.eternalcore.api.tool.CraftToolType;
import com.road.eternalcore.common.item.tool.CustomTierItem;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;

import java.util.function.Consumer;

public class SmithingRecipeMaker<T extends SmithingRecipeBuilder> extends RecipeMaker<T> {

    public SmithingRecipeMaker(T builder) {
        super(builder);
    }
    public SmithingRecipeMaker<T> byProduct(IItemProvider item){
        return byProduct(item, 1);
    }
    public SmithingRecipeMaker<T> byProduct(IItemProvider item, int count){
        return byProduct(item, count, (nbt) -> {});
    }
    public SmithingRecipeMaker<T> byProduct(IItemProvider item, int count, Consumer<CompoundNBT> nbtConsumer){
        builder.addResult(item, count, nbtConsumer);
        return this;
    }
    public SmithingRecipeMaker<T> level(int smithingLevel){
        builder.smithingLevel = smithingLevel;
        return this;
    }
    public SmithingRecipeMaker<T> toolUse(CraftToolType tool){
        return toolUse(tool, CustomTierItem.DEFAULT_DURABILITY_SUBDIVIDE);
    }
    public SmithingRecipeMaker<T> toolUse(CraftToolType tool, int use){
        builder.toolUse = Pair.of(tool.getName(), use);
        return this;
    }
    public SmithingRecipeMaker<T> requires(ITag<Item> tag) {
        return this.requires(tag, 1);
    }
    public SmithingRecipeMaker<T> requires(ITag<Item> tag, int count) {
        return this.requires(Ingredient.of(tag), count);
    }
    public SmithingRecipeMaker<T> requires(IItemProvider item) {
        return this.requires(item, 1);
    }
    public SmithingRecipeMaker<T> requires(IItemProvider item, int count) {
        return this.requires(Ingredient.of(item), count);
    }
    public SmithingRecipeMaker<T> requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }
    public SmithingRecipeMaker<T> requires(Ingredient ingredient, int count) {
        for(int i = 0; i < count; ++i) {
            builder.ingredients.add(ingredient);
        }
        return this;
    }
    public SmithingRecipeMaker<T> unlockedBy(String criterionId, ICriterionInstance criterion){
        builder.advancement.addCriterion(criterionId, criterion);
        return this;
    }
    public SmithingRecipeMaker<T> group(String groupId){
        builder.group = groupId;
        return this;
    }
}
