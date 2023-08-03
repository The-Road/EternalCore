package com.road.eternalcore.data.loot.conditions;

import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.LootConditionManager;
import net.minecraft.util.registry.Registry;

public class ModLootConditionManager extends LootConditionManager {
    public static void init(){} // 用于注册
    public static final LootConditionType MATCH_TOOL = register("match_tool", new ModMatchTool.Serializer());
    private static LootConditionType register(String name, ILootSerializer<? extends ILootCondition> serializer) {
        return Registry.register(Registry.LOOT_CONDITION_TYPE, new ModResourceLocation(name), new LootConditionType(serializer));
    }
}
