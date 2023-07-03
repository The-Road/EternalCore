package com.road.eternalcore.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class MachineISTER extends ItemStackTileEntityRenderer {
    @Override
    public void renderByItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_239207_5_, int p_239207_6_) {
        super.renderByItem(itemStack, transformType, matrixStack, buffer, p_239207_5_, p_239207_6_);
    }
}
