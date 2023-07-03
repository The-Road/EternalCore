package com.road.eternalcore.common.item;

import com.road.eternalcore.common.item.battery.BatteryItem;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;

public class ModItemModelsProperties {
    public static void registerModelsProperties(){
        for(Item item :ModItems.getAll()){
            // 给电池物品添加电量标记
            if (item instanceof BatteryItem){
                registerProperty(item, "charge_level", BatteryItem.GetChargeLevel);
            }
        }
    }
    private static void registerProperty(Item item, String propertyName, IItemPropertyGetter proc){
        ItemModelsProperties.register(item, new ModResourceLocation(propertyName), proc);
    }
}
