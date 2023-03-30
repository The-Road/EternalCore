package com.road.eternalcore.data.loot;

import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.LootPool;

public interface LootTableMaker {
    // 设置战利品表的抽奖次数
    static LootPool.Builder singleLootPool(){
        return multiLootPool(1);
    }
    static LootPool.Builder multiLootPool(int num){
        return LootPool.lootPool().setRolls(ConstantRange.exactly(num));
    }
}
