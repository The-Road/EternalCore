package com.road.eternalcore.data.loot;

import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.IItemProvider;

public interface ModLootTableHelper {
    // 设置战利品表的抽奖次数
    default LootPool.Builder singleLootPool(){
        return multiLootPool(1);
    }
    default LootPool.Builder multiLootPool(int num){
        return LootPool.lootPool().setRolls(ConstantRange.exactly(num));
    }
    // 设置战利品物品的掉落个数
    default StandaloneLootEntry.Builder<?> lootItem(IItemProvider item){
        return ItemLootEntry.lootTableItem(item);
    }
    default StandaloneLootEntry.Builder<?> lootItem(IItemProvider item, int num){
        return ItemLootEntry.lootTableItem(item).apply(SetCount.setCount(ConstantRange.exactly(num)));
    }
}
