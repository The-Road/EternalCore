package com.road.eternalcore.registries;

import com.road.eternalcore.Utils;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ItemRegister {
    private static final Map<String, RegistryObject<Item>> items = new HashMap<>();
    private final DeferredRegister<Item> itemRegister = DeferredRegister.create(ForgeRegistries.ITEMS, Utils.MOD_ID);
    public RegistryObject<Item> register(final String name, final Supplier<? extends Item> sup){
        RegistryObject<Item> item = itemRegister.register(name, sup);
        ItemRegister.items.put(name, item);
        return item;
    }
    public void register(IEventBus bus){
        itemRegister.register(bus);
    }
    public static Map<String, RegistryObject<Item>> getItems(){
        return items;
    }
}
