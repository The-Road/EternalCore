package com.road.eternalcore.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.road.eternalcore.common.inventory.container.HandcraftAssemblyContainer;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class HandcraftAssemblyScreen extends ContainerScreen<HandcraftAssemblyContainer> implements IRecipeShownListener {
    private static final ResourceLocation CRAFTING_TABLE_LOCATION = new ModResourceLocation("textures/gui/container/handcraft_assembly_table.png");
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    private final RecipeBookGui recipeBookComponent = new RecipeBookGui();
    private boolean widthTooNarrow;

    public HandcraftAssemblyScreen(HandcraftAssemblyContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
        this.children.add(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
        this.addButton(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (p_214076_1_) -> {
            this.recipeBookComponent.initVisuals(this.widthTooNarrow);
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
            ((ImageButton)p_214076_1_).setPosition(this.leftPos + 5, this.height / 2 - 49);
        }));
        this.titleLabelX = 29;
    }

    public void tick() {
        super.tick();
        this.recipeBookComponent.tick();
    }

    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(p_230430_1_, p_230430_4_, p_230430_2_, p_230430_3_);
            this.recipeBookComponent.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        } else {
            this.recipeBookComponent.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
            super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
            this.recipeBookComponent.renderGhostRecipe(p_230430_1_, this.leftPos, this.topPos, true, p_230430_4_);
        }

        this.renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
        this.recipeBookComponent.renderTooltip(p_230430_1_, this.leftPos, this.topPos, p_230430_2_, p_230430_3_);
    }

    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(CRAFTING_TABLE_LOCATION);
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(p_230450_1_, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected boolean isHovering(int p_195359_1_, int p_195359_2_, int p_195359_3_, int p_195359_4_, double p_195359_5_, double p_195359_7_) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(p_195359_1_, p_195359_2_, p_195359_3_, p_195359_4_, p_195359_5_, p_195359_7_);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int mouseKey) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, mouseKey)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(mouseX, mouseY, mouseKey);
        }
    }

    protected boolean hasClickedOutside(double mouseX, double mouseY, int leftPos, int rightPos, int mouseKey) {
        boolean flag = mouseX < (double)leftPos || mouseY < (double)rightPos || mouseX >= (double)(leftPos + this.imageWidth) || mouseY >= (double)(rightPos + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, mouseKey) && flag;
    }

    protected void slotClicked(Slot slot, int slotNum, int mouseKey, ClickType clickType) {
        super.slotClicked(slot, slotNum, mouseKey, clickType);
        this.recipeBookComponent.slotClicked(slot);
    }

    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    public void removed() {
        this.recipeBookComponent.removed();
        super.removed();
    }

    public RecipeBookGui getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}
