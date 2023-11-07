package com.road.eternalcore.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.road.eternalcore.client.renderer.FluidRenderer;
import com.road.eternalcore.common.inventory.container.FluidContainer;
import com.road.eternalcore.common.inventory.container.slot.FluidSlot;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public abstract class FluidContainerScreen<T extends FluidContainer> extends ContainerScreen<T> {
    // 添加流体的渲染功能
    protected FluidRenderer fluidRenderer;
    public FluidContainerScreen(T container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.fluidRenderer = new FluidRenderer();
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    protected void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderTooltip(matrixStack, mouseX, mouseY);
        if (this.minecraft.player.inventory.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot instanceof FluidSlot){

        }
    }

}
