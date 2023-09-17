package com.road.eternalcore.compat.jei.category;

import com.road.eternalcore.common.util.ModResourceLocation;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class CategoryConstant {
    private final ResourceLocation uid;
    private final TranslationTextComponent title;
    private final ResourceLocation backgroundLocation;
    private final int bgWidth;
    private final int bgHeight;
    private final IItemProvider iconItem;

    public CategoryConstant(String categoryName, int bgWidth, int bgHeight, IItemProvider iconItem) {
        this.uid = new ModResourceLocation(categoryName);
        this.title = new TranslationTextComponent("gui.eternalcore.jei.category." + categoryName);
        this.backgroundLocation = new ModResourceLocation("textures/gui/jei/category/" + categoryName + ".png");
        this.bgWidth = bgWidth;
        this.bgHeight = bgHeight;
        this.iconItem = iconItem;
    }
    public ResourceLocation getUid() {
        return uid;
    }

    public TranslationTextComponent getTitle() {
        return title;
    }

    public IDrawable getBackground(IGuiHelper guiHelper) {
        return guiHelper.createDrawable(backgroundLocation, 0, 0, bgWidth, bgHeight);
    }

    public IDrawable getIcon(IGuiHelper guiHelper) {
        return guiHelper.createDrawableIngredient(getIconItemStack());
    }

    public ItemStack getIconItemStack() {
        return new ItemStack(iconItem);
    }
}
