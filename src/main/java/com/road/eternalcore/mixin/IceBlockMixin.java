package com.road.eternalcore.mixin;

import com.road.eternalcore.common.item.tool.ModToolType;
import net.minecraft.block.IceBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(IceBlock.class)
public abstract class IceBlockMixin {

    // 如果手持锯子，则视为拥有精准采集
    @Redirect(method = "playerDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getItemEnchantmentLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I"))
    private int playerDestroy$checkSaw(Enchantment enchantment, ItemStack itemStack){
        if (itemStack.getToolTypes().contains(ModToolType.SAW)){
            return 1;
        } else {
            return EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemStack);
        }
    }
}
