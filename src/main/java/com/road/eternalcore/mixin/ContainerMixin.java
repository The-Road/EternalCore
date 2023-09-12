package com.road.eternalcore.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Container.class)
public abstract class ContainerMixin {
    @Shadow
    public abstract void broadcastChanges();

    // 修复快捷移动物品后客户端和服务端数据可能不同步的BUG
    @Inject(method = "doClick", at = @At(value = "RETURN"))
    private void doClick$refresh(int slot, int button, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir){
        broadcastChanges();
    }
}
