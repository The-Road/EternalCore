package com.road.eternalcore.compat.jei.category;

import com.road.eternalcore.common.block.ModBlocks;
import com.road.eternalcore.common.item.crafting.recipe.IToolCraftingRecipe;
import com.road.eternalcore.common.item.crafting.recipe.ToolShapedRecipe;
import com.road.eternalcore.compat.jei.TranslationUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class ToolCraftingRecipeCategory extends ModRecipeCategory<IToolCraftingRecipe>{

    public static final CategoryConstant CONSTANT = new CategoryConstant("tool_crafting", 129, 54, ModBlocks.handcraftAssemblyTable);

    private final ICraftingGridHelper craftingGridHelper;
    public ToolCraftingRecipeCategory(IGuiHelper guiHelper){
        super(guiHelper, CONSTANT);
        craftingGridHelper = guiHelper.createCraftingGridHelper(4);
    }

    public Class<? extends IToolCraftingRecipe> getRecipeClass() {
        return IToolCraftingRecipe.class;
    }

    public void setIngredients(IToolCraftingRecipe recipe, IIngredients ingredients) {
        List<Ingredient> inputs = new ArrayList<>();
        for (int i=0; i<3; i++){
            if (recipe.getToolUse(i) != null){
                inputs.add(Ingredient.of(recipe.getToolUse(i).getFirst().getIconItem()));
            } else {
                inputs.add(Ingredient.EMPTY);
            }
        }
        inputs.addAll(recipe.getIngredients());
        ingredients.setInputIngredients(inputs);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    public void setRecipe(IRecipeLayout recipeLayout, IToolCraftingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, false, 107, 18);
        for (int y0 = 0; y0 < 3; y0++){
            guiItemStacks.init(1 + y0, true, 0, y0 * 18);
        }
        for (int x1 = 0; x1 < 3; x1++){
            for (int y1 = 0; y1 < 3; y1++){
                guiItemStacks.init(4 + y1 + x1 * 3, true, 26 + y1 * 18, x1 * 18);
            }
        }

        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        List<List<ItemStack>> tools = inputs.subList(0, 3);
        List<List<ItemStack>> recipeItems = inputs.subList(3, inputs.size());
        List<ItemStack> result = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

        guiItemStacks.set(0, result);
        for (int i = 0; i < 3; i++){
            guiItemStacks.set(1 + i, tools.get(i));
        }
        if (recipe instanceof ToolShapedRecipe){
            int width = ((ToolShapedRecipe) recipe).getRecipeWidth();
            int height = ((ToolShapedRecipe) recipe).getRecipeHeight();
            craftingGridHelper.setInputs(guiItemStacks, recipeItems, width, height);
        } else {
            craftingGridHelper.setInputs(guiItemStacks, recipeItems);
            recipeLayout.setShapeless();
        }

        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip)-> {
            if (slotIndex > 0 && slotIndex < 4 && !ingredient.isEmpty()){
                ITextComponent title = tooltip.get(0);
                tooltip.clear();
                tooltip.add(title);
                tooltip.add(TranslationUtils.toolUse(recipe.getToolUse(slotIndex - 1).getSecond()));
            }
        });
    }
}
