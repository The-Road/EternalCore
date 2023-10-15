package com.road.eternalcore.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.road.eternalcore.common.inventory.container.PartCraftTableContainer;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class PartCraftTableScreen extends ContainerScreen<PartCraftTableContainer> {
    private static final ResourceLocation PART_CRAFT_TABLE_LOCATION = new ModResourceLocation("textures/gui/container/part_craft_table.png");
    public PartCraftTableScreen(PartCraftTableContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(PART_CRAFT_TABLE_LOCATION);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        renderSmithingLevel(matrixStack);
    }

    protected void renderSmithingLevel(MatrixStack matrixStack){
        int x = leftPos + 143;
        int y = topPos + 32;
        TranslationTextComponent text = new TranslationTextComponent("gui.eternalcore.text.smithingLevel", getMenu().getSmithingLevel());
        int textWidth = font.width(text);
        font.draw(matrixStack, text, x - textWidth, y, 0xFF808080);
    }
}
