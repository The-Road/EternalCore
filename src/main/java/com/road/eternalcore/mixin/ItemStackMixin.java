package com.road.eternalcore.mixin;

import com.road.eternalcore.ModConstant;
import com.road.eternalcore.common.item.tool.CustomTierItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    // 给判断是否已损耗的条件加上耐久尾数大于0
    @Inject(method = "isDamaged", at = @At(value = "RETURN"), cancellable = true)
    private void isDamaged$advance(CallbackInfoReturnable<Boolean> cir){
        ItemStack self = (ItemStack) (Object) this;
        boolean result = self.isDamageableItem() && self.getTag().getInt(ModConstant.Durability_decimal) > 0;
        cir.setReturnValue(cir.getReturnValue() || result);
    }

    // 给显示耐久添加小数点功能
    @Redirect(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 15))
    private boolean getTooltipLines$addDurabilityDecimal(List list, Object arg){
        ItemStack self = (ItemStack) (Object) this;
        if (self.getTag().getInt(ModConstant.Durability_decimal) > 0){
            return list.add(new TranslationTextComponent(
                    "item.durability",
                    ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(
                            self.getMaxDamage() - self.getDamageValue() - 1.0 * self.getTag().getInt(ModConstant.Durability_decimal) / CustomTierItem.getBinary(self)
                    ),
                    self.getMaxDamage()
            ));
        }else {
            return list.add(arg);
        }
    }
}
