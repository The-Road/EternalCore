package com.road.eternalcore.data.loot;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.road.eternalcore.data.loot.BlockLootTables;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ForgeLootTableProvider;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModLootTableProvider extends ForgeLootTableProvider {
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> subProviders = ImmutableList.of(
            Pair.of(BlockLootTables::new, LootParameterSets.BLOCK));
    public ModLootTableProvider(DataGenerator generator){
        super(generator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables(){
        return subProviders;
    }
}
