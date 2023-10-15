package com.road.eternalcore.common.item.tool;

import net.minecraft.item.ItemStack;

public class KnifeItem extends CustomWeaponItem{
    public KnifeItem(){
        super(3.0F, 2.5F, new Properties().addToolType(ModToolType.KNIFE, 0));
    }
    protected double getBasicAttackDamage(ItemStack itemStack){
        // 小刀的攻击力是剑的0.6倍
        return super.getBasicAttackDamage(itemStack) * 0.6;
    }
}
