package com.road.eternalcore.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.road.eternalcore.common.tileentity.MachineTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class MachineTER extends TileEntityRenderer<MachineTileEntity> {
    // 弃用，留作参考
    public MachineTER(TileEntityRendererDispatcher dispatcher){
        super(dispatcher);
    }

    @Override
    public void render(MachineTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverLay) {
        matrixStack.pushPose();
        // BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        // BlockState blockState = tileEntity.getBlockState();;
        matrixStack.popPose();
    }
}
