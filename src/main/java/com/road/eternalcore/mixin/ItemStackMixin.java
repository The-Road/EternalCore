package com.road.eternalcore.mixin;

import com.road.eternalcore.Constant;
import com.road.eternalcore.common.item.tool.CustomTierItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    // 给判断是否已损耗的条件加上耐久尾数大于0
    @Inject(method = "isDamaged", at = @At(value = "RETURN"), cancellable = true)
    protected void isDamaged$advance(CallbackInfoReturnable<Boolean> cir){
        ItemStack self = (ItemStack) (Object) this;
        boolean result = self.isDamageableItem() && self.getTag().getInt(Constant.Durability_decimal) > 0;
        cir.setReturnValue(cir.getReturnValue() || result);
    }

    // 给显示耐久添加小数点功能
    @ModifyArg(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 15))
    protected Object getTooltipLines$addDurabilityDecimal(Object arg){
        ItemStack self = (ItemStack) (Object) this;
        if (self.getTag().getInt(Constant.Durability_decimal) > 0){
            return new TranslationTextComponent(
                    "item.durability",
                    ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(
                            self.getMaxDamage() - self.getDamageValue() - 1.0 * self.getTag().getInt(Constant.Durability_decimal) / CustomTierItem.getBinary(self)
                    ),
                    self.getMaxDamage()
            );
        }else {
            return arg;
        }
    }
}
