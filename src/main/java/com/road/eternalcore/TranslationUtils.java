package com.road.eternalcore;

import com.road.eternalcore.common.item.ModItems;
import com.road.eternalcore.common.item.tool.CustomTierItem;
import net.minecraft.item.Item;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class TranslationUtils {
    private static final DecimalFormat DECIMAL_FORMAT = Util.make(new DecimalFormat("#.##"), (format) -> {
        format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
    });
    public static ITextComponent toolTierLevel(int tierLevel){
        return new TranslationTextComponent(
                "eternalcore.toolTip.tool.tierLevel",
                DECIMAL_FORMAT.format(tierLevel)).withStyle(TextFormatting.DARK_GREEN);
    }
    public static ITextComponent toolMineSpeed(double mineSpeed){
        return new TranslationTextComponent(
                "eternalcore.toolTip.tool.mineSpeed",
                DECIMAL_FORMAT.format(mineSpeed)).withStyle(TextFormatting.DARK_GREEN);
    }
    public static ITextComponent toolBasicDamage(double basicDamage){
        return new TranslationTextComponent(
                "eternalcore.toolTip.tool.basicDamage",
                DECIMAL_FORMAT.format(basicDamage)).withStyle(TextFormatting.DARK_GREEN);
    }
    public static ITextComponent toolAttackSpeed(double attackSpeed){
        return new TranslationTextComponent(
                "eternalcore.toolTip.tool.attackSpeed",
                DECIMAL_FORMAT.format(attackSpeed)).withStyle(TextFormatting.DARK_GREEN);
    }
    public static ITextComponent toolDurability(int durability){
        return new TranslationTextComponent(
                "eternalcore.toolTip.tool.durability",
                DECIMAL_FORMAT.format(durability)).withStyle(TextFormatting.DARK_GREEN);
    }
    public static ITextComponent toolSmithLevel(int smithLevel){
        return new TranslationTextComponent(
                "eternalcore.toolTip.tool.smithLevel",
                DECIMAL_FORMAT.format(smithLevel)).withStyle(TextFormatting.BLUE);
    }
    public static ITextComponent guiSmithLevel(int level){
        return new TranslationTextComponent("gui.eternalcore.text.smithLevel", level);
    }
    public static ITextComponent jeiToolUse(int consume){
        return new TranslationTextComponent(
                "eternalcore.toolTip.jei.toolUse",
               DECIMAL_FORMAT.format(1.0 * consume / CustomTierItem.DEFAULT_DURABILITY_SUBDIVIDE)
        );
    }

    public static boolean hasTranslationName(Item item){
        return hasTranslationText(item.getDescriptionId());
    }
    public static ITextComponent getItemDescription(Item item){
        String itemDescriptionStr = "eternalcore.itemDescription." + ModItems.getItemName(item);
        if (hasTranslationText(itemDescriptionStr)){
            return new TranslationTextComponent(itemDescriptionStr).withStyle(TextFormatting.GRAY);
        } else {
            return null;
        }
    }
    public static boolean hasTranslationText(String str){
        LanguageMap languagemap = LanguageMap.getInstance();
        return languagemap.has(str);
    }
}
