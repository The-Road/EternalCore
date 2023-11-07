package com.road.eternalcore.compat.jei.category;

import com.road.eternalcore.compat.jei.ModFluidStackRenderer;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;

public class CategoryUtils {
    // 描绘默认格式（16*16）的流体
    public static void initGuiFluidStack(IGuiFluidStackGroup guiFluidStack, int slotIndex, boolean input, int xPosition, int yPosition){
        guiFluidStack.init(slotIndex, input, new ModFluidStackRenderer(), xPosition, yPosition, 16, 16, 0, 0);
    }
}
