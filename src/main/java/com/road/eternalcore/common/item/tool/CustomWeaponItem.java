package com.road.eternalcore.common.item.tool;

import com.road.eternalcore.TranslationUtils;
import com.road.eternalcore.common.item.group.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CustomWeaponItem extends CustomTierItem{

    public CustomWeaponItem(float atk_damage, float atk_speed, Item.Properties properties){
        super(properties.tab(ModGroup.toolGroup), atk_damage, atk_speed);
    }

    @OnlyIn(Dist.CLIENT)
    public void addOtherHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag tooltipFlag) {
        // 显示攻击伤害、攻击速度
        if (!this.onlyForCrafting) {
            list.add(TranslationUtils.toolBasicDamage(getBasicAttackDamage(itemStack) + 1));
            list.add(TranslationUtils.toolAttackSpeed(getAttackSpeed(itemStack) + 4));
        }
        addDurabilityText(itemStack, list, tooltipFlag);
        // 关掉原本的属性显示
        itemStack.hideTooltipPart(ItemStack.TooltipDisplayFlags.MODIFIERS);
    }
}
