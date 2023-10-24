package com.road.eternalcore.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.road.eternalcore.common.inventory.container.HandcraftAssemblyContainer;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class HandcraftAssemblyScreen extends SmithLevelContainerScreen<HandcraftAssemblyContainer> implements IRecipeShownListener {
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
    }

    public void tick() {
        super.tick();
        this.recipeBookComponent.tick();
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(matrixStack, partialTicks, mouseX, mouseY);
            this.recipeBookComponent.render(matrixStack, mouseX, mouseY, partialTicks);
        } else {
            this.recipeBookComponent.render(matrixStack, mouseX, mouseY, partialTicks);
            super.render(matrixStack, mouseX, mouseY, partialTicks);
            this.recipeBookComponent.renderGhostRecipe(matrixStack, this.leftPos, this.topPos, true, partialTicks);
        }

        this.renderTooltip(matrixStack, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(matrixStack, this.leftPos, this.topPos, mouseX, mouseY);
    }

    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(CRAFTING_TABLE_LOCATION);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        renderSmithLevel(matrixStack, this.menu.getSmithLevel(), 119, 17, mouseX, mouseY);
        this.minecraft.getTextureManager().bind(CRAFTING_TABLE_LOCATION);
    }

    protected boolean isHovering(int slotX, int slotY, int slotWidth, int slotHeight, double mouseX, double mouseY) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(slotX, slotY, slotWidth, slotHeight, mouseX, mouseY);
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
