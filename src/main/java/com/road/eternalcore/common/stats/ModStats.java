package com.road.eternalcore.common.stats;

import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ModStats extends Stats {
    public static final ResourceLocation INTERACT_WITH_MACHINE = makeCustomStat(
            "interact_with_machine", IStatFormatter.DEFAULT
    );

    private static ResourceLocation makeCustomStat(String name, IStatFormatter statFormatter) {
        ResourceLocation resourcelocation = new ModResourceLocation(name);
        Registry.register(Registry.CUSTOM_STAT, name, resourcelocation);
        CUSTOM.get(resourcelocation, statFormatter);
        return resourcelocation;
    }
}
