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

public class ShapedRecipeMaker<T extends NBTShapedRecipeBuilder> extends RecipeMaker<T> {
    public ShapedRecipeMaker(T builder) {
        super(builder);
    }
    public ShapedRecipeMaker<T> nbt(Consumer<CompoundNBT> consumer){
        // 通过Consumer为产物添加NBT标签
        consumer.accept(builder.nbt);
        return this;
    }
    public ShapedRecipeMaker<T> toolUse(CraftToolType tool){
        return toolUse(tool, CustomTierItem.DEFAULT_DURABILITY_SUBDIVIDE);
    }
    public ShapedRecipeMaker<T> toolUse(CraftToolType tool, int use){
        ((ToolShapedRecipeBuilder) builder).toolUses.add(Pair.of(tool.getName(), use));
        return this;
    }
    public ShapedRecipeMaker<T> smithLevel(int smithLevel){
        ((ToolShapedRecipeBuilder) builder).smithLevel = smithLevel;
        return this;
    }
    public ShapedRecipeMaker<T> define(Character character, ITag<Item> tag){
        return define(character, Ingredient.of(tag));
    }
    public ShapedRecipeMaker<T> define(Character character, IItemProvider item){
        return define(character, Ingredient.of(item));
    }
    public ShapedRecipeMaker<T> define(Character character, Ingredient ingredient) {
        if (builder.key.containsKey(character)) {
            throw new IllegalArgumentException("Symbol '" + character + "' is already defined!");
        } else if (character == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            builder.key.put(character, ingredient);
            return this;
        }
    }
    public ShapedRecipeMaker<T> pattern(String s) {
        if (!builder.rows.isEmpty() && s.length() != builder.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            builder.rows.add(s);
            return this;
        }
    }
    public ShapedRecipeMaker<T> unlockedBy(String criterionId, ICriterionInstance criterion){
        builder.advancement.addCriterion(criterionId, criterion);
        return this;
    }
    public ShapedRecipeMaker<T> group(String groupId){
        builder.group = groupId;
        return this;
    }
}
