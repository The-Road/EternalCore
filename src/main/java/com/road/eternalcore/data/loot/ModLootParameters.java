package com.road.eternalcore.data.loot;

import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.loot.LootParameter;

public class ModLootParameters {
    public static final LootParameter<Boolean> SAW_MODIFIED = create("saw_modified");
    private static <T> LootParameter<T> create(String name) {
        return new LootParameter<>(new ModResourceLocation(name));
    }
}
