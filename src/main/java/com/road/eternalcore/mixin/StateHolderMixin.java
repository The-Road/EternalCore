package com.road.eternalcore.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.state.Property;
import net.minecraft.state.StateHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(StateHolder.class)
public abstract class StateHolderMixin<O, S> {
    @Final
    @Shadow
    private ImmutableMap<Property<?>, Comparable<?>> values;

    // setValue时额外判断一次equals, 因为字符串用==判断可能会出问题
    // 顺便这个地方每次build的时候都要报个错，不知道什么毛病
    @Inject(method = "setValue", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Table;get(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), cancellable = true)
    private <T extends Comparable<T>, V extends T> void setValue$checkEquals(Property<T> property, V value, CallbackInfoReturnable<S> cir){
        if (Objects.equals(values.get(property), value)){
            cir.setReturnValue((S) this);
        }
    }
}
