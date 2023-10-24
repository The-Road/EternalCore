package com.road.eternalcore.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.road.eternalcore.TranslationUtils;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class SmithLevelContainerScreen<T extends Container> extends ContainerScreen<T> {

    public static final ResourceLocation ICON_LOCATION = new ModResourceLocation("textures/gui/smith_level.png");

    public SmithLevelContainerScreen(T container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }
    // 描绘锻造等级图标
    protected void renderSmithLevel(MatrixStack matrixStack, int smithLevel, int x, int y, int mouseX, int mouseY){
        if (smithLevel < 1) return;
        this.minecraft.getTextureManager().bind(ICON_LOCATION);
        int u0 = smithLevel % 8 * 16;
        int v0 = smithLevel / 8 * 16;
        blit(matrixStack, this.leftPos + x, this.topPos + y, u0, v0, 16, 16);
        Rectangle2d rect = new Rectangle2d(x, y, 16, 16);
        if (this.minecraft.player.inventory.getCarried().isEmpty() && rect.contains(mouseX - this.leftPos, mouseY - this.topPos)){
            renderTooltip(matrixStack, TranslationUtils.guiSmithLevel(smithLevel), mouseX, mouseY);
        }
    }
}
