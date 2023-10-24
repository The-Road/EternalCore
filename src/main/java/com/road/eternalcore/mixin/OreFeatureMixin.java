package com.road.eternalcore.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(OreFeature.class)
public abstract class OreFeatureMixin {

    @Inject(method = "place(Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/OreFeatureConfig;)Z", at = @At(value = "HEAD"), cancellable = true)
    private void place$reduceOre(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random random, BlockPos pos, OreFeatureConfig config, CallbackInfoReturnable<Boolean> cir){
        // 所有的散矿只有20%概率成功生成
        if (config.state.getBlock().is(Tags.Blocks.ORES)){
            if (random.nextFloat() > 0.2){
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
