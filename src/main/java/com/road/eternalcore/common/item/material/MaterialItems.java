package com.road.eternalcore.common.item.material;

import com.mojang.datafixers.util.Pair;
import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.api.ore.OreShape;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.registries.ItemRegister;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.road.eternalcore.api.material.MaterialShape.*;
import static com.road.eternalcore.api.material.Materials.*;

// 用于注册和获取所有的基础材料（锭、粉、板等）以及矿石产物（粉碎矿、含杂粉等）
public class MaterialItems {
    public static final ItemRegister ITEMS = new ItemRegister();
    // 用于获取物品的Map，通过MaterialItems.get(type, name)可以获取材料对应的物品
    private static final Map<Pair<MaterialShape, Materials>, RegistryObject<Item>> MaterialItemMap = new HashMap<>();
    private static final Map<Pair<MaterialShape, Materials>, Item> VanillaMaterialItemMap = new HashMap<>();
    private static final Map<Pair<OreShape, Ores>, RegistryObject<Item>> OreItemMap = new HashMap<>();
    // 通过物品ID(无前缀)获取对应物品的Map，通过MaterialItems.get(item_id)可以获取对应物品
    private static final Map<String, RegistryObject<Item>> ItemIdMap = new HashMap<>();
    private static final Map<String, Item> VanillaItemIdMap = new HashMap<>();
    private static void addVanillaItem(MaterialShape shape, Materials material, Item item){
        VanillaMaterialItemMap.put(Pair.of(shape, material), item);
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
        Map<MaterialShape, List<Materials>> registerMap = new HashMap<>();
        for (MaterialShape shape : MaterialShape.getAllShapes()){
            registerMap.put(shape, new ArrayList<>());
        }
        for (Materials material : Materials.getAllMaterials()){
            for (MaterialShape shape : material.getShapes()){
                registerMap.get(shape).add(material);
            }
        }
        registerMap.forEach((shape, materials) -> {
            String shapeName = shape.getName();
            for (Materials material : materials){
                Pair<MaterialShape, Materials> pair = new Pair<>(shape, material);
                if (!VanillaMaterialItemMap.containsKey(pair)){
                    String registerID = Materials.getRegisterName(shape, material);
                    RegistryObject<Item> item = ITEMS.register(
                            registerID,
                            () -> new BasicMaterialItem(material, shape, material.getProperties())
                    );
                    MaterialItemMap.put(pair, item);
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
                OreItemMap.put(new Pair<>(shape, ore), item);
                ItemIdMap.put(registerID, item);
            }
        }
    }
    // 获取物品
    public static Item get(MaterialShape shape, Materials material){
        Pair<MaterialShape, Materials> key = Pair.of(shape, material);
        if (VanillaMaterialItemMap.containsKey(key)){
            return VanillaMaterialItemMap.get(key);
        }else{
            return getMod(shape, material);
        }
    }
    public static Item get(OreShape shape, Ores ore){
        Pair<OreShape, Ores> key = Pair.of(shape, ore);
        if (OreItemMap.containsKey(key)){
            return OreItemMap.get(key).get();
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
        Pair<MaterialShape, Materials> key = Pair.of(shape, material);
        if (MaterialItemMap.containsKey(key)) {
            return MaterialItemMap.get(key).get();
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
