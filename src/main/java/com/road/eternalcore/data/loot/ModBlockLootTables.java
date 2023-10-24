package com.road.eternalcore.data.loot;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.road.eternalcore.ModConstant;
import com.road.eternalcore.api.advancements.criterion.ModToolPredicate;
import com.road.eternalcore.common.block.ModBlocks;
import com.road.eternalcore.common.block.machine.MachineBlocks;
import com.road.eternalcore.common.block.ore.OreBlocks;
import com.road.eternalcore.common.item.tool.ModToolType;
import com.road.eternalcore.data.loot.conditions.ModMatchTool;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class ModBlockLootTables extends BlockLootTables implements ModLootTableHelper {
    private static final ILootCondition.IBuilder HAS_SILK_TOUCH = MatchTool.toolMatches(
            ItemPredicate.Builder.item().hasEnchantment(
                    new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))
            )
    );

    private static final ILootCondition.IBuilder HAS_WRENCH = ModMatchTool.toolMatches(
            ModToolPredicate.Builder.tool().of(ModToolType.WRENCH)
    );
    private final Map<ResourceLocation, LootTable.Builder> map = Maps.newHashMap();

    // --常用的LootPool模板--
    protected LootPool.Builder singleDropLootPool(IItemProvider item){
        // 掉落单个物品（有爆炸判定）
        return singleLootPool().add(lootItem(item)).when(SurvivesExplosion.survivesExplosion());
    }
    protected LootPool.Builder singleDropWithConditionPool(IItemProvider item, ILootCondition.IBuilder condition, IItemProvider otherItem){
        // 满足条件则掉落单个物品，否则掉落别的物品（有爆炸判定）
        return singleLootPool().add(ItemLootEntry.lootTableItem(item).when(condition).otherwise(
                lootItem(otherItem).when(SurvivesExplosion.survivesExplosion())
        ));
    }
    protected LootPool.Builder multiDropLootPool(IItemProvider item, int num){
        // 掉落多个同一物品（有爆炸判定）
        return singleLootPool().add(lootItem(item, num)).when(SurvivesExplosion.survivesExplosion());
    }

    // --常用的add模板--
    protected void addSelfDrop(Block block){
        // 掉落自身的方块
        this.add(block, LootTable.lootTable().withPool(
                singleDropLootPool(block)
        ));
    }
    protected void addSelfDropWithSilkTouch(Block block, IItemProvider otherItem){
        // 需要精准采集才能掉落自身的方块
        this.add(block, LootTable.lootTable().withPool(
                singleDropWithConditionPool(block, HAS_SILK_TOUCH, otherItem)
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
                    addSelfDrop(block);
                }
                LootTable.Builder loottable$builder = this.map.remove(resourcelocation);
                if (loottable$builder != null) {
                    consumer.accept(resourcelocation, loottable$builder);
                }
            }
        }

        if (!this.map.isEmpty()) {
            throw new IllegalArgumentException("Created block loot tables for non-blocks: " + this.map.keySet());
        }
    }

    protected void add(Block block, LootTable.Builder builder) {
        this.map.put(block.getLootTable(), builder);
    }

    private void addOreTables(){
        for (Block block : OreBlocks.getAllMod()){
            addSelfDrop(block);
        }
    }
    private void addMachineTables(){
        // 用扳手拆除机器掉落机器本身，需要保存的NBT包括材料和电力等级
        // 机器因非扳手拆除而掉落的零件写在机器的getDrops里
        // 没有爆炸判定
        for (Block block : MachineBlocks.getAll()){
            this.add(block, LootTable.lootTable().withPool(
                    singleLootPool()
                            .add(lootItem(block)
                                    .apply(CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY)
                                            .copy(ModConstant.Material, ModConstant.blockEntityTag(ModConstant.Material))
                                            .copy(ModConstant.Machine_euTier, ModConstant.blockEntityTag(ModConstant.Machine_euTier))
                                    )
                            )
                            .when(HAS_WRENCH)
            ));
        }
    }
}
