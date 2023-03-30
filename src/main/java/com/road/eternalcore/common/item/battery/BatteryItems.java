package com.road.eternalcore.common.item.battery;

import com.road.eternalcore.registries.ItemRegister;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BatteryItems {
    // 物品的注册
    public static final ItemRegister ITEMS = new ItemRegister();
    private static final Map<String, RegistryObject<Item>> map = new HashMap<>();

    private static void init(){
        registerDebugBattery("debug_battery");
        registerBattery("test_battery", 10000, 32);
    }
    private static void registerBattery(String name, int capacity, int voltage){
        RegistryObject<Item> item = ITEMS.register(
                name, () -> new BatteryItem(capacity, voltage, new Item.Properties())
        );
        map.put(name, item);
    }
    private static void registerDebugBattery(String name){
        RegistryObject<Item> item = ITEMS.register(name, DebugBatteryItem::new);
        map.put(name, item);
    }

    public static Collection<Item> getAll(){
        return map.values().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
    public static Item get(String name){
        return map.get(name).get();
    }
    static {init();}
}
