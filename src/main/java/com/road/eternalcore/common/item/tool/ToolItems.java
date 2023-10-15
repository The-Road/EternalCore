package com.road.eternalcore.common.item.tool;

import com.road.eternalcore.registries.ItemRegister;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ToolItems {
    // 物品的注册
    public static final ItemRegister ITEMS = new ItemRegister();
    public static final RegistryObject<Item> debugTool = ITEMS.register("debug_tool", DebugToolItem::new);
    public static final RegistryObject<Item> pickaxe = ITEMS.register("pickaxe", PickaxeItem::new);
    public static final RegistryObject<Item> axe = ITEMS.register("axe", AxeItem::new);

    public static final RegistryObject<Item> shovel = ITEMS.register("shovel", ShovelItem::new);

    public static final RegistryObject<Item> hoe = ITEMS.register("hoe", HoeItem::new);
    public static final RegistryObject<Item> sword = ITEMS.register("sword", SwordItem::new);
    public static final RegistryObject<Item> knife = ITEMS.register("knife", KnifeItem::new);
    public static final RegistryObject<Item> hammer = ITEMS.register("hammer", HammerItem::new);
    public static final RegistryObject<Item> softMallet = ITEMS.register("soft_mallet", SoftMalletItem::new);
    public static final RegistryObject<Item> wrench = ITEMS.register("wrench", WrenchItem::new);
    public static final RegistryObject<Item> file = ITEMS.register("file", FileItem::new);
    public static final RegistryObject<Item> wireCutter = ITEMS.register("wire_cutter", WireCutterItem::new);
    public static final RegistryObject<Item> screwdriver = ITEMS.register("screwdriver", ScrewdriverItem::new);
    public static final RegistryObject<Item> crowbar = ITEMS.register("crowbar", CrowbarItem::new);
    public static final RegistryObject<Item> saw = ITEMS.register("saw", SawItem::new);
}
