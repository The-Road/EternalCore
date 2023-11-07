package com.road.eternalcore.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.road.eternalcore.api.block.ModBlockStateProperties;
import com.road.eternalcore.client.renderer.model.data.MachineModelData;
import com.road.eternalcore.common.block.ICasingRenderBlock;
import com.road.eternalcore.common.item.block.MachineBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
    // 渲染机器类方块的ISTER
    public static final MachineISTER INSTANCE = new MachineISTER();

    public void renderByItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        Item item = itemStack.getItem();
        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        Block block = ((BlockItem) item).getBlock();
        BlockState blockState = block.defaultBlockState();
        if (blockState.hasProperty(ModBlockStateProperties.MATERIAL)){
            blockState = ModBlockStateProperties.MATERIAL.setBlockStateProperty(
                    blockState, MachineBlockItem.getMaterialBlockData(itemStack).getMaterial());
        }
        IModelData modelData = (block instanceof ICasingRenderBlock) ?
                new MachineModelData(((ICasingRenderBlock) block).getRenderCasing(blockState)) :
                new MachineModelData(Blocks.STONE);
        blockRenderer.renderBlock(blockState, matrixStack, buffer, combinedLight, combinedOverlay, modelData);
        matrixStack.popPose();
    }
}
