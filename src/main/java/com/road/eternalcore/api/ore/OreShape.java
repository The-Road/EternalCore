package com.road.eternalcore.api.ore;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class OreShape {
    // 记录矿石中间产物的类型，包括粉碎矿、洗净矿、含杂粉……之类的
    // 混合矿石的最终产物xx矿粉算粉末类材料
    protected static final Map<String, OreShape> shapes = new LinkedHashMap<>();
    public static OreShape ORE = new OreShape("ore", "%s_ore");
    public static OreShape CRUSHED_ORE = new OreShape("crushed_ore", "crushed_%s_ore");
    public static OreShape PURIFIED_ORE = new OreShape("purified_ore", "purified_%s_ore");
    public static OreShape CENTRIFUGED_ORE = new OreShape("centrifuged_ore", "centrifuged_%s_ore");
    public static OreShape IMPURE_DUST = new OreShape("impure_dust", "impure_%s_dust");
    public static OreShape PURIFIED_DUST = new OreShape("purified_dust", "purified_%s_dust");

    protected String name;
    protected String registerName;
    public OreShape(String name, String registerName){
        if (shapes.containsKey(name)){
            throw new IllegalStateException("Ore Product "+name+" has already existed!");
        }
        this.name = name;
        this.registerName = registerName;
        shapes.put(name, this);
    }
    public static OreShape get(String name){
        return shapes.get(name);
    }
    public static Collection<OreShape> getAllShapes(){
        return shapes.values();
    }
    public static Collection<OreShape> getProductShapes() {
        Collection<OreShape> list = shapes.values();
        list.remove(ORE);
        return list;
    }
    public String getName(){
        return name;
    }
    public String toString(){
        return getName();
    }
    public String getDescriptionId(){
        return "eternalcore.oreShape." + this.getName();
    }
}
