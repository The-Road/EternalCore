package com.road.eternalcore.common.world.generate;

import com.road.eternalcore.common.world.generate.feature.ModFeatures;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class ScatteredOreGen {
    // 生成散矿
    public static void register(BiomeLoadingEvent event){
        ResourceLocation name = event.getName();
        ForgeRegistry<Biome> biomeRegistry = (ForgeRegistry<Biome>) ForgeRegistries.BIOMES;
        RegistryKey<Biome> biomeRegistryKey = biomeRegistry.getKey(biomeRegistry.getID(name));
        BiomeGenerationSettingsBuilder gen = event.getGeneration();
        // 主世界散矿
        if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.OVERWORLD)) {
            gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ModFeatures.SCATTERED_ORE_COPPER);
            gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ModFeatures.SCATTERED_ORE_TIN);
        }
    }
}
