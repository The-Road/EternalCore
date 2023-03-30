package com.road.eternalcore.common.item;

import com.road.eternalcore.common.item.battery.BatteryItems;
import com.road.eternalcore.common.item.block.BlockItems;
import com.road.eternalcore.common.item.material.MaterialItems;
import com.road.eternalcore.common.item.tool.ToolItems;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModItemRegistries {

    public static void register(IEventBus bus){
        MaterialItems.ITEMS.register(bus);
        BlockItems.ITEMS.register(bus);
        ToolItems.ITEMS.register(bus);
        BatteryItems.ITEMS.register(bus);
    }

}
