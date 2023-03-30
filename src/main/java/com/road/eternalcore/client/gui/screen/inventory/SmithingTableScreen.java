package com.road.eternalcore.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.road.eternalcore.common.inventory.container.SmithingTableContainer;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SmithingTableScreen extends ContainerScreen<SmithingTableContainer> {
    private static final ResourceLocation SMITHING_TABLE_LOCATION = new ModResourceLocation("textures/gui/container/smithing_table.png");
    public SmithingTableScreen(SmithingTableContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(SMITHING_TABLE_LOCATION);
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(p_230450_1_, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
