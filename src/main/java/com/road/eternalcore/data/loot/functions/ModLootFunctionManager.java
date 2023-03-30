package com.road.eternalcore.data.loot.functions;

import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.loot.functions.LootFunctionManager;
import net.minecraft.util.registry.Registry;

public class ModLootFunctionManager extends LootFunctionManager {
    public static void init(){} // 用于注册
    public static final LootFunctionType HAMMER_SMASH = register("hammer_smash", new HammerSmash.Serializer());

    private static LootFunctionType register(String name, ILootSerializer<? extends ILootFunction> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ModResourceLocation(name), new LootFunctionType(serializer));
    }
}
