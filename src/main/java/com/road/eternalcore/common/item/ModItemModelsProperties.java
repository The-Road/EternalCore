package com.road.eternalcore.common.item;

import com.road.eternalcore.common.item.battery.BatteryItem;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModItemModelsProperties {
    @SubscribeEvent
    public static void registerModelsProperties(FMLClientSetupEvent event){
        event.enqueueWork(() -> {
            for(Item item :ModItems.getAll()){
                // 给电池物品添加电量标记
                if (item instanceof BatteryItem){
                    registerProperty(item, BatteryItem.EnergyLevel, BatteryItem.GetEnergyLevel);
                }
            }
        });
    }
    private static void registerProperty(Item item, String propertyName, IItemPropertyGetter proc){
        ItemModelsProperties.register(item, new ModResourceLocation(propertyName), proc);
    }
}
