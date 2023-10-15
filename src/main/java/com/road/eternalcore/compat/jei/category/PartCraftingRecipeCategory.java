package com.road.eternalcore.compat.jei.category;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.road.eternalcore.common.block.ModBlocks;
import com.road.eternalcore.common.item.crafting.recipe.PartCraftingRecipe;
import com.road.eternalcore.compat.jei.TranslationUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class PartCraftingRecipeCategory extends ModRecipeCategory<PartCraftingRecipe> {
    // 每个锻造等级对应不同材质的锤子图标
    public static final CategoryConstant CONSTANT = new CategoryConstant("part_crafting", 108, 38, ModBlocks.partCraftTable);

    public PartCraftingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, CONSTANT);
    }

    public Class<? extends PartCraftingRecipe> getRecipeClass() {
        return PartCraftingRecipe.class;
    }

    public void setIngredients(PartCraftingRecipe recipe, IIngredients ingredients) {
        List<Ingredient> inputs = new ArrayList<>();
        inputs.add(Ingredient.of(recipe.getToolUse(0).getFirst().getIconItem()));
        inputs.addAll(recipe.getIngredients());
        ingredients.setInputIngredients(inputs);

        List<ItemStack> outputs = new ArrayList<>();
        outputs.add(recipe.getResultItem());
        outputs.addAll(recipe.getByProducts());
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    public void setRecipe(IRecipeLayout recipeLayout, PartCraftingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true,9, 0);
        guiItemStacks.init(1, true,0, 20);
        guiItemStacks.init(2, true,18, 20);
        guiItemStacks.init(3, false,72, 20);
        guiItemStacks.init(4, false,90, 20);
        guiItemStacks.set(ingredients);
        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip)-> {
            if (slotIndex == 0){
                ITextComponent title = tooltip.get(0);
                tooltip.clear();
                tooltip.add(title);
                tooltip.add(TranslationUtils.toolUse(recipe.getToolUse(0).getSecond()));
            }
        });
    }

    public void draw(PartCraftingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        ITextComponent text = TranslationUtils.smithingLevel(recipe.getSmithingLevel());
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        int textWidth = fontRenderer.width(text);
        fontRenderer.draw(matrixStack, text, 108 - textWidth, 4, 0xFF808080);
    }
}
