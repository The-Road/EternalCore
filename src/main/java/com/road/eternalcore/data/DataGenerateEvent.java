package com.road.eternalcore.data;

import com.road.eternalcore.data.loot.ModLootTableProvider;
import com.road.eternalcore.data.loot.modifiers.GLMDataProvider;
import com.road.eternalcore.data.model.ModBlockModelProvider;
import com.road.eternalcore.data.model.ModBlockStateProvider;
import com.road.eternalcore.data.model.ModItemModelProvider;
import com.road.eternalcore.data.recipes.provider.ModRecipeProvider;
import com.road.eternalcore.data.tags.ModBlockTagsProvider;
import com.road.eternalcore.data.tags.ModItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerateEvent {
    @SubscribeEvent
    public static void dataGenerate(GatherDataEvent event){
        DataGenerator gen = event.getGenerator();

        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(gen, existingFileHelper);
        gen.addProvider(blockTags);
        gen.addProvider(new ModItemTagsProvider(gen, blockTags, existingFileHelper));
        gen.addProvider(new ModRecipeProvider(gen));
        gen.addProvider(new ModLootTableProvider(gen));
        gen.addProvider(new GLMDataProvider(gen));
        gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
    }
}
