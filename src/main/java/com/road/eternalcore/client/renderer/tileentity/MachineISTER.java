package com.road.eternalcore.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.road.eternalcore.client.renderer.model.data.MachineModelData;
import com.road.eternalcore.common.item.block.MachineBlockItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.data.IModelData;

public class MachineISTER extends ItemStackTileEntityRenderer {
    public static final MachineISTER instance = new MachineISTER();

    public void renderByItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        Item item = itemStack.getItem();
        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BlockState blockState = ((BlockItem) item).getBlock().defaultBlockState();
        IModelData modelData = new MachineModelData(
                MachineBlockItem.getMaterialBlockData(itemStack).getMaterial());
        blockRenderer.renderBlock(blockState, matrixStack, buffer, combinedLight, combinedOverlay, modelData);
        matrixStack.popPose();
    }
}
