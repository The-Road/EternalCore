package com.road.eternalcore.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.road.eternalcore.client.renderer.FluidRenderer;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class ModFluidStackRenderer implements IIngredientRenderer<FluidStack> {
    private final int width;
    private final int height;
    private final FluidRenderer fluidRenderer;

    public ModFluidStackRenderer(){
        this(16, 16);
    }
    public ModFluidStackRenderer(int width, int height){
        this.width = width;
        this.height = height;
        this.fluidRenderer = new FluidRenderer();
    }
    public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable FluidStack fluidStack) {
        fluidRenderer.render(matrixStack, xPosition, yPosition, this.width, this.height, fluidStack);
    }

    public List<ITextComponent> getTooltip(FluidStack fluidStack, ITooltipFlag tooltipFlag) {
        return FluidRenderer.getFluidToolTips(fluidStack);
    }
}
