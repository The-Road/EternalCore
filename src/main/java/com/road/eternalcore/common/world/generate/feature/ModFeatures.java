package com.road.eternalcore.common.world.generate.feature;

import com.road.eternalcore.common.block.ModBlocks;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;

public class ModFeatures {
    // 散矿
    public static final ConfiguredFeature<?, ?> SCATTERED_ORE_COPPER = register(
            "scattered_ore_copper", Feature.ORE.configured(
                    new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                            States.copperOre,
                            8
                    )
            ).range(80).squared().count(15)
    );
    public static final ConfiguredFeature<?, ?> SCATTERED_ORE_TIN = register(
            "scattered_ore_tin", Feature.ORE.configured(
                    new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                            States.tinOre,
                            8
                    )
            ).decorated(
                    Placement.RANGE.configured(
                            new TopSolidRangeConfig(48, 48, 128)
                    )
            ).squared().count(15)
    );

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> feature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ModResourceLocation(name), feature);
    }
    public static final class States {
        protected static final BlockState copperOre = ModBlocks.copperOre.defaultBlockState();
        protected static final BlockState tinOre = ModBlocks.tinOre.defaultBlockState();
    }
}
