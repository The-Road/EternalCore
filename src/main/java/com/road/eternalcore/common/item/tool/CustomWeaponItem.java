package com.road.eternalcore.common.item.tool;

import com.road.eternalcore.common.item.group.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

public abstract class CustomWeaponItem extends CustomTierItem{

    public CustomWeaponItem(float atk_damage, float atk_speed, Item.Properties properties){
        super(properties.tab(ModGroup.toolGroup), atk_damage, atk_speed);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag tooltipFlag) {
        // 显示攻击伤害、攻击速度
        DecimalFormat format = ItemStack.ATTRIBUTE_MODIFIER_FORMAT;
        if (!this.onlyForCrafting) {
            list.add(new TranslationTextComponent(
                    "eternalcore.toolTip.tool.basicDamage",
                    format.format((getBasicAttackDamage(itemStack) + 1))).withStyle(TextFormatting.DARK_GREEN));
            list.add(new TranslationTextComponent(
                    "eternalcore.toolTip.tool.attackSpeed",
                    format.format((getAttackSpeed(itemStack) + 4))).withStyle(TextFormatting.DARK_GREEN));
        }
        addDurabilityText(itemStack, list, tooltipFlag);
        // 关掉原本的属性显示
        itemStack.hideTooltipPart(ItemStack.TooltipDisplayFlags.MODIFIERS);
    }
}
