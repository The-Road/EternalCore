package com.road.eternalcore.common.item.material;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.api.ore.OreShape;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.common.block.ore.OreBlocks;
import com.road.eternalcore.registries.ItemRegister;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

import static com.road.eternalcore.api.material.MaterialShape.*;
import static com.road.eternalcore.api.material.Materials.*;

// 用于注册和获取所有的基础材料（锭、粉、板等）以及矿石产物（粉碎矿、含杂粉等）
public class MaterialItems {
    public static final ItemRegister ITEMS = new ItemRegister();
    // 用于获取物品的Map，通过MaterialItems.get(type, name)可以获取材料对应的物品
    private static final Table<MaterialShape, Materials, RegistryObject<Item>> MaterialItemTable = HashBasedTable.create();
    private static final Table<MaterialShape, Materials, Item> VanillaMaterialItemTable = HashBasedTable.create();
    private static final Table<OreShape, Ores, RegistryObject<Item>> OreItemTable = HashBasedTable.create();
    // 通过物品ID(无前缀)获取对应物品的Map，通过MaterialItems.get(item_id)可以获取对应物品
    private static final Map<String, RegistryObject<Item>> ItemIdMap = new HashMap<>();
    private static final Map<String, Item> VanillaItemIdMap = new HashMap<>();
    private static void addVanillaItem(MaterialShape shape, Materials material, Item item){
        VanillaMaterialItemTable.put(shape, material, item);
        VanillaItemIdMap.put(ForgeRegistries.ITEMS.getKey(item).getPath(), item);
    }
    private static void addVanillaItems(){
        // 添加原版物品对应的材料
        addVanillaItem(INGOT, IRON, Items.IRON_INGOT);
        addVanillaItem(INGOT, GOLD, Items.GOLD_INGOT);
        addVanillaItem(INGOT, NETHERITE, Items.NETHERITE_INGOT);
        addVanillaItem(NUGGET, IRON, Items.IRON_NUGGET);
        addVanillaItem(NUGGET, GOLD, Items.GOLD_NUGGET);
        addVanillaItem(GEM, DIAMOND, Items.DIAMOND);
        addVanillaItem(GEM, EMERALD, Items.EMERALD);
        addVanillaItem(MINERAL, COAL, Items.COAL);
        addVanillaItem(DUST, REDSTONE, Items.REDSTONE);
    }
    private static void init(){
        // 注册物品
        addVanillaItems();
        registerMaterials();
        registerOreProducts();
    }
    private static void registerMaterials(){
        // 用一个Map来管理注册以方便创造模式物品栏里的排序
        Map<MaterialShape, List<Materials>> registerMap = new LinkedHashMap<>();
        for (MaterialShape shape : MaterialShape.getAllShapes()){
            registerMap.put(shape, new ArrayList<>());
        }
        for (Materials material : Materials.getAllMaterials()){
            for (MaterialShape shape : material.getShapes()){
                registerMap.get(shape).add(material);
            }
        }
        registerMap.forEach((shape, materials) -> {
            for (Materials material : materials){
                if (!VanillaMaterialItemTable.contains(shape, material)){
                    String registerID = Materials.getRegisterName(shape, material);
                    RegistryObject<Item> item = ITEMS.register(
                            registerID,
                            () -> new BasicMaterialItem(material, shape, material.getProperties())
                    );
                    MaterialItemTable.put(shape, material, item);
                    ItemIdMap.put(registerID, item);
                }
            }
        });
    }
    private static void registerOreProducts(){
        for (OreShape shape : OreShape.getProductShapes()){
            for (Ores ore : Ores.getAllOres()){
                String registerID = Ores.getRegisterName(shape, ore);
                RegistryObject<Item> item = ITEMS.register(
                        registerID,
                        () -> new OreProduct(ore, shape)
                );
                OreItemTable.put(shape, ore, item);
                ItemIdMap.put(registerID, item);
            }
        }
    }
    // 获取物品
    public static Item get(MaterialShape shape, Materials material){
        if (VanillaMaterialItemTable.contains(shape, material)){
            return VanillaMaterialItemTable.get(shape, material);
        }else{
            return getMod(shape, material);
        }
    }
    public static Item get(OreShape shape, Ores ore){
        if (shape == OreShape.ORE){
            return OreBlocks.get(ore).asItem();
        }
        if (OreItemTable.contains(shape, ore)){
            return OreItemTable.get(shape, ore).get();
        }else{
            return null;
        }
    }
    public static Item get(String itemId){
        if (VanillaItemIdMap.containsKey(itemId)){
            return VanillaItemIdMap.get(itemId);
        }else{
            return getMod(itemId);
        }
    }
    public static Item getMod(MaterialShape shape, Materials material){
        if (MaterialItemTable.contains(shape, material)) {
            return MaterialItemTable.get(shape, material).get();
        }else{
            return null;
        }
    }
    public static Item getMod(String itemId){
        if (ItemIdMap.containsKey(itemId)){
            return ItemIdMap.get(itemId).get();
        }else{
            return null;
        }
    }

    static{init();}
}
