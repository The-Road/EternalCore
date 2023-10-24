package com.road.eternalcore.compat.jei.category;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.road.eternalcore.TranslationUtils;
import com.road.eternalcore.client.gui.screen.inventory.SmithLevelContainerScreen;
import com.road.eternalcore.common.item.crafting.recipe.IToolUsedRecipe;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class SmithLevelRecipeCategory<T extends IToolUsedRecipe> extends ModRecipeCategory<T> {
    protected int smithLevelIconX = 0;
    protected int smithLevelIconY = 0;
    public SmithLevelRecipeCategory(IGuiHelper guiHelper, CategoryConstant constant) {
        super(guiHelper, constant);
    }

    public void draw(T recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        int smithLevel = recipe.getSmithLevel();
        if (smithLevel > 0){
            int x = smithLevel % 8 * 16;
            int y = smithLevel / 8 * 16;
            IDrawable icon = guiHelper.createDrawable(SmithLevelContainerScreen.ICON_LOCATION, x, y, 16, 16);
            icon.draw(matrixStack, smithLevelIconX, smithLevelIconY);
        }
    }
    public List<ITextComponent> getTooltipStrings(T recipe, double mouseX, double mouseY) {
        int smithLevel = recipe.getSmithLevel();
        if (smithLevel > 0) {
            List<ITextComponent> list = new ArrayList<>();
            double dx = mouseX - smithLevelIconX;
            double dy = mouseY - smithLevelIconY;
            if (dx > 0 && dx < 16 && dy > 0 && dy < 16) {
                list.add(TranslationUtils.guiSmithLevel(smithLevel));
            }
            return list;
        } else {
            return super.getTooltipStrings(recipe, mouseX, mouseY);
        }
    }
}
