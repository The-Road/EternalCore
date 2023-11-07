package com.road.eternalcore.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.road.eternalcore.ModConstant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class FluidRenderer {

    // 搬运JEI的FluidStackRenderer
    private static final int TEX_WIDTH = 16;
    private static final int TEX_HEIGHT = 16;
    public FluidRenderer(){}

    public void render(MatrixStack matrixStack, int x, int y, int width, int height, @Nullable FluidStack fluidStack) {
        if (fluidStack == null) {
            return;
        }
        Fluid fluid = fluidStack.getFluid();
        if (fluid == null) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        drawFluid(matrixStack, x, y, width, height, fluidStack);
        drawFluidAmount(matrixStack, x, y, width, height, fluidStack);
        RenderSystem.color4f(1, 1, 1, 1);
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
    }
    private void drawFluid(MatrixStack matrixStack, int x, int y, int width, int height, @Nullable FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        TextureAtlasSprite fluidStillSprite = getStillFluidSprite(fluidStack);
        FluidAttributes attributes = fluid.getAttributes();
        int fluidColor = attributes.getColor(fluidStack);
        drawTiledSprite(matrixStack, x, y, width, height, fluidColor, fluidStillSprite);
    }


    private void drawTiledSprite(MatrixStack matrixStack, int xPosition, int yPosition, int tiledWidth, int tiledHeight, int color, TextureAtlasSprite sprite) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(PlayerContainer.BLOCK_ATLAS);
        Matrix4f matrix = matrixStack.last().pose();
        setGLColorFromInt(color);

        final int xTileCount = tiledWidth / TEX_WIDTH;
        final int xRemainder = tiledWidth - (xTileCount * TEX_WIDTH);
        final int yTileCount = tiledHeight / TEX_HEIGHT;
        final int yRemainder = tiledHeight - (yTileCount * TEX_HEIGHT);

        final int yStart = yPosition + tiledHeight;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = (xTile == xTileCount) ? xRemainder : TEX_WIDTH;
                int height = (yTile == yTileCount) ? yRemainder : TEX_HEIGHT;
                int x = xPosition + (xTile * TEX_WIDTH);
                int y = yStart - ((yTile + 1) * TEX_HEIGHT);
                if (width > 0 && height > 0) {
                    int maskTop = TEX_HEIGHT - height;
                    int maskRight = TEX_WIDTH - width;

                    drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 100);
                }
            }
        }
    }
    private void drawFluidAmount(MatrixStack matrixStack, int x, int y, int width, int height, FluidStack fluidStack){
        matrixStack.pushPose();
        String str = getAmountText(fluidStack.getAmount());
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        matrixStack.translate(0, 0, 200);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        fontRenderer.drawInBatch(str, (float)(x + width + 1 - fontRenderer.width(str)), (float)(y + height - 7), 16777215, true, matrixStack.last().pose(), irendertypebuffer$impl, false, 0, 15728880);
        irendertypebuffer$impl.endBatch();
        matrixStack.popPose();
    }
    private static TextureAtlasSprite getStillFluidSprite(FluidStack fluidStack) {
        Minecraft minecraft = Minecraft.getInstance();
        Fluid fluid = fluidStack.getFluid();
        FluidAttributes attributes = fluid.getAttributes();
        ResourceLocation fluidStill = attributes.getStillTexture(fluidStack);
        return minecraft.getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(fluidStill);
    }
    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        RenderSystem.color4f(red, green, blue, alpha);
    }
    private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, float zLevel) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();
        uMax = uMax - (maskRight / 16F * (uMax - uMin));
        vMax = vMax - (maskTop / 16F * (vMax - vMin));

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.vertex(matrix, xCoord, yCoord + 16, zLevel).uv(uMin, vMax).endVertex();
        bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + 16, zLevel).uv(uMax, vMax).endVertex();
        bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).uv(uMax, vMin).endVertex();
        bufferBuilder.vertex(matrix, xCoord, yCoord + maskTop, zLevel).uv(uMin, vMin).endVertex();
        tessellator.end();
    }
    private static String getAmountText(int amount){
        if (amount < 10000){
            return String.valueOf(amount);
        } else {
            // 保留三位有效数字
            String[] digit = {"K", "M", "G"};
            int i = 0;
            int drawNum = amount;
            while (drawNum > 1000_000){
                drawNum /= 1000;
                i++;
            }
            if (drawNum >= 100_000){
                return drawNum / 1000 + digit[i];
            } else if (drawNum >= 10_000){
                return ModConstant.DECIMAL_FORMAT_1.format(drawNum / 100 / 10.0) + digit[i];
            } else {
                return ModConstant.DECIMAL_FORMAT_2.format(drawNum / 10 / 100.0) + digit[i];
            }
        }
    }

    public static List<ITextComponent> getFluidToolTips(FluidStack fluidStack){
        List<ITextComponent> tooltip = new ArrayList<>();
        Fluid fluid = fluidStack.getFluid();
        if (fluid == null) {
            return tooltip;
        }
        // 流体名称
        ITextComponent displayName = fluidStack.getDisplayName();
        tooltip.add(displayName);
        // 流体数量
        int amount = fluidStack.getAmount();
        TranslationTextComponent amountString = new TranslationTextComponent("eternalcore.toolTip.fluid.amount", NumberFormat.getIntegerInstance().format(amount));
        tooltip.add(amountString.withStyle(TextFormatting.GRAY));
        // 流体温度
        int temperature = fluid.getAttributes().getTemperature(fluidStack);
        TranslationTextComponent tempString = new TranslationTextComponent("eternalcore.toolTip.fluid.temperature", temperature);
        tooltip.add(tempString.withStyle(TextFormatting.GRAY));
        // 流体状态（气体/液体）
        TranslationTextComponent stateString = new TranslationTextComponent(
                "eternalcore.toolTip.fluid." + (fluid.getAttributes().isGaseous() ? "gas" : "liquid")
        );
        tooltip.add(stateString.withStyle(TextFormatting.GRAY));
        return tooltip;
    }
}
