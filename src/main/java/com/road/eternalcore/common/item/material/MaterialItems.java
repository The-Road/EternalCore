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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.road.eternalcore.api.material.MaterialShape.*;
import static com.road.eternalcore.api.material.Materials.*;

// 用于注册和获取所有的基础材料（锭、粉、板等）以及矿石产物（粉碎矿、含杂粉等）
public class MaterialItems {
    public static final ItemRegister ITEMS = new ItemRegister();
    // 用于获取物品的Map，通过MaterialItems.get(type, name)可以获取材料对应的物品
    private static final Table<MaterialShape, Materials, RegistryObject<Item>> MaterialItemTable = HashBasedTable.create();
    private static final Table<MaterialShape, Materials, Item> VanillaMaterialItemTable = HashBasedTable.create();
    private static final Table<OreShape, Ores, RegistryObject<Item>> OreItemTable = HashBasedTable.create();
    private static void addVanillaItems(){
        // 添加原版物品对应的材料
        VanillaMaterialItemTable.put(INGOT, IRON, Items.IRON_INGOT);
        VanillaMaterialItemTable.put(INGOT, GOLD, Items.GOLD_INGOT);
        VanillaMaterialItemTable.put(INGOT, NETHERITE, Items.NETHERITE_INGOT);
        VanillaMaterialItemTable.put(NUGGET, IRON, Items.IRON_NUGGET);
        VanillaMaterialItemTable.put(NUGGET, GOLD, Items.GOLD_NUGGET);
        VanillaMaterialItemTable.put(GEM, DIAMOND, Items.DIAMOND);
        VanillaMaterialItemTable.put(GEM, EMERALD, Items.EMERALD);
        VanillaMaterialItemTable.put(MINERAL, COAL, Items.COAL);
        VanillaMaterialItemTable.put(MINERAL, FLINT, Items.FLINT);
        VanillaMaterialItemTable.put(DUST, REDSTONE, Items.REDSTONE);
        VanillaMaterialItemTable.put(ROD, WOOD, Items.STICK);
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
                        () -> new OreProductItem(ore, shape)
                );
                OreItemTable.put(shape, ore, item);
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
            throw new IllegalStateException("Nonexistent ore item :" + Ores.getRegisterName(shape, ore));
        }
    }
    private static Item getMod(MaterialShape shape, Materials material){
        if (MaterialItemTable.contains(shape, material)) {
            return MaterialItemTable.get(shape, material).get();
        }else{
            throw new IllegalStateException("Nonexistent material : " + Materials.getRegisterName(shape, material));
        }
    }

    static{init();}
}
