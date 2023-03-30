package com.road.eternalcore.common.item.tool;

import com.road.eternalcore.registries.ItemRegister;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ToolItems {
    // 物品的注册
    public static final ItemRegister ITEMS = new ItemRegister();
    public static final RegistryObject<Item> debugTool = ITEMS.register("debug_tool",DebugToolItem::new);
    public static final RegistryObject<Item> pickaxe = ITEMS.register("pickaxe", PickaxeItem::new);
    public static final RegistryObject<Item> hammer = ITEMS.register("hammer", HammerItem::new);
    public static final RegistryObject<Item> softHammer = ITEMS.register("soft_hammer", SoftHammerItem::new);
    public static final RegistryObject<Item> wrench = ITEMS.register("wrench", WrenchItem::new);
}
