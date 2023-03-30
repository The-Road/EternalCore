package com.road.eternalcore.data.loot;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.road.eternalcore.common.block.ModBlocks;
import com.road.eternalcore.common.block.machine.MachineBlocks;
import com.road.eternalcore.common.block.ore.OreBlocks;
import com.road.eternalcore.common.item.ModItems;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class BlockLootTables extends net.minecraft.data.loot.BlockLootTables implements LootTableMaker{
    private static final ILootCondition.IBuilder HAS_SILK_TOUCH = MatchTool.toolMatches(
            ItemPredicate.Builder.item().hasEnchantment(
                    new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))
            )
    );

    private static final ILootCondition.IBuilder HAS_HAMMER = MatchTool.toolMatches(
            ItemPredicate.Builder.item().of(ModItems.hammer)
    );
    private final Map<ResourceLocation, LootTable.Builder> map = Maps.newHashMap();

    // --常用的LootPool模板--
    protected static LootPool.Builder singleDropLootPool(Block block, IItemProvider item){
        // 掉落单一物品的方块
        return applyExplosionCondition(
                block, LootTableMaker.singleLootPool().add(ItemLootEntry.lootTableItem(item))
        );
    }
    protected static LootPool.Builder selfDropLootPool(Block block){
        // 掉落自身的方块
        return singleDropLootPool(block, block);
    }
    protected static LootPool.Builder singleDropWithConditionPool(Block block, IItemProvider item, ILootCondition.IBuilder condition, IItemProvider otherItem){
        // 满足条件则掉落单一物品，否则掉落别的物品
        return LootTableMaker.singleLootPool().add(ItemLootEntry.lootTableItem(item).when(condition).otherwise(
                applyExplosionCondition(block, ItemLootEntry.lootTableItem(otherItem))
        ));
    }
    protected static LootPool.Builder selfDropWithConditionPool(Block block, ILootCondition.IBuilder condition, IItemProvider otherItem){
        return singleDropWithConditionPool(block, block, condition, otherItem);
    }

    // --常用的add模板--
    protected void addSelfDrop(Block block){
        // 掉落自身的方块
        this.add(block, LootTable.lootTable().withPool(
                selfDropLootPool(block)
        ));
    }
    protected void addSelfDropWithSilkTouch(Block block, IItemProvider item){
        // 需要精准采集才能掉落自身的方块
        this.add(block, LootTable.lootTable().withPool(
                selfDropWithConditionPool(block, HAS_SILK_TOUCH, item)
        ));
    }

    @Override
    protected void addTables() {
        addOreTables();
        addMachineTables();
    }

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer){
        this.addTables();
        Set<ResourceLocation> set = Sets.newHashSet();

        for(Block block : ModBlocks.getAll()) {
            ResourceLocation resourcelocation = block.getLootTable();
            if (resourcelocation != LootTables.EMPTY && set.add(resourcelocation)) {
                // 给所有未注册掉落物的物品添加默认掉落自身
                if (!this.map.containsKey(resourcelocation)){
                    dropSelf(block);
                }
                LootTable.Builder loottable$builder = this.map.remove(resourcelocation);
                if (loottable$builder != null) {
                    consumer.accept(resourcelocation, loottable$builder);
                }
            }
        }

        if (!this.map.isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
        }
    }

    protected void add(Block block, LootTable.Builder builder) {
        this.map.put(block.getLootTable(), builder);
    }

    private void addOreTables(){
        for (Block block : OreBlocks.getAll()){
            dropSelf(block);
        }
    }
    private void addMachineTables(){
        for (Block block : MachineBlocks.getAll()){
            dropSelf(block);
        }
    }
}
