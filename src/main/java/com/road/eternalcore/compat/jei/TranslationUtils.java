package com.road.eternalcore.compat.jei;

import com.road.eternalcore.common.item.tool.CustomTierItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.DecimalFormat;

public class TranslationUtils {
    private static final DecimalFormat FORMAT = ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

    public static TranslationTextComponent toolUse(int consume){
        return new TranslationTextComponent(
                "eternalcore.toolTip.jei.toolUse",
                FORMAT.format(1.0 * consume / CustomTierItem.DEFAULT_DURABILITY_SUBDIVIDE)
        );
    }

    public static TranslationTextComponent smithingLevel(int level){
        return new TranslationTextComponent("gui.eternalcore.text.smithingLevel", level);
    }
}
