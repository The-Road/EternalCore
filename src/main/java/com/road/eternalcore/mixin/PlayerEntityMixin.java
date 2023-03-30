package com.road.eternalcore.mixin;

import com.road.eternalcore.common.item.tool.CustomTierItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    // 获取挖掘速度时根据主手物品种类添加速度倍率
    @Inject(method = "getDigSpeed", at = @At(value = "RETURN"), cancellable = true, remap = false)
    protected void getDigSpeed$applySpeedRate(BlockState blockState, BlockPos pos, CallbackInfoReturnable<Float> cir){
        PlayerEntity self = (PlayerEntity) (Object) this;
        ItemStack mainHandItem = self.getMainHandItem();
        if (mainHandItem.getItem() instanceof CustomTierItem){
            cir.setReturnValue(cir.getReturnValue() * ((CustomTierItem) mainHandItem.getItem()).getMineSpeedRate());
        }
    }
}
