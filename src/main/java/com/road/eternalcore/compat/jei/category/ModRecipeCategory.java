package com.road.eternalcore.compat.jei.category;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class ModRecipeCategory<T> implements IRecipeCategory<T> {
    private final CategoryConstant constant;
    protected final IGuiHelper guiHelper;

    protected final IDrawable background;
    protected final IDrawable icon;
    public ModRecipeCategory(IGuiHelper guiHelper, CategoryConstant constant){
        this.guiHelper = guiHelper;
        this.constant = constant;
        this.background = constant.getBackground(guiHelper);
        this.icon = constant.getIcon(guiHelper);
    }

    public ResourceLocation getUid() {
        return constant.getUid();
    }
    public String getTitle() {
        return getTitleAsTextComponent().getString();
    }
    public ITextComponent getTitleAsTextComponent() {
        return constant.getTitle();
    }
    public IDrawable getBackground() {
        return background;
    }
    public IDrawable getIcon() {
        return icon;
    }
}
