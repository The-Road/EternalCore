package com.road.eternalcore.api.material;

import com.google.common.collect.Sets;

import java.util.*;

public class MaterialShape {
    protected static final Map<String, MaterialShape> shapes = new LinkedHashMap<>();
    public static MaterialShape INGOT = new MaterialShape("ingot"); //锭
    public static MaterialShape HOT_INGOT = new MaterialShape("hot_ingot", "hot_%s_ingot"); //热锭
    public static MaterialShape GEM = new MaterialShape("gem", "%s"); //宝石
    public static MaterialShape EXQUISITE_GEM = new MaterialShape("exquisite_gem", "exquisite_%s", 576); //精致宝石（4宝石）
    public static MaterialShape FLAWLESS_GEM = new MaterialShape("flawless_gem", "flawless_%s", 288); //无暇宝石（2宝石）
    public static MaterialShape FLAWED_GEM = new MaterialShape("flawed_gem", "flawed_%s", 72); //有暇宝石（1/2宝石）
    public static MaterialShape CHIPPED_GEM = new MaterialShape("chipped_gem", "chipped_%s", 36); //破碎宝石（1/4宝石）
    public static MaterialShape MINERAL = new MaterialShape("mineral", "%s"); //矿物（煤、石英等）
    public static MaterialShape DUST = new MaterialShape("dust"); //粉
    public static MaterialShape SMALL_DUST = new MaterialShape("small_dust", "small_%s_dust", 36); //小堆粉（1/4粉）
    public static MaterialShape TINY_DUST = new MaterialShape("tiny_dust", "tiny_%s_dust", 16); //小撮粉（1/9粉）
    public static MaterialShape NUGGET = new MaterialShape("nugget", 16); //粒
    public static MaterialShape PLATE = new MaterialShape("plate"); //板
    public static MaterialShape DOUBLE_PLATE = new MaterialShape("double_plate", "double_%s_plate", 288); //双层板
    public static MaterialShape DENSE_PLATE = new MaterialShape("dense_plate", "dense_%s_plate",1296); //致密板（9）
    public static MaterialShape ROD = new MaterialShape("rod", 72); //杆
    public static MaterialShape LONG_ROD = new MaterialShape("long_rod", "long_%s_rod", 144); //长杆
    public static MaterialShape FOIL = new MaterialShape("foil", 36); //箔
    public static MaterialShape FINE_WIRE = new MaterialShape("fine_wire", "fine_%s_wire", 18); //细导线
    public static MaterialShape LENS = new MaterialShape("lens", 144); //透镜
    public static MaterialShape ROTOR = new MaterialShape("rotor", 576); //转子
    public static MaterialShape RING = new MaterialShape("ring", 36); //环
    public static MaterialShape GEAR = new MaterialShape("gear", 216); //齿轮

    public static Map<MaterialShape, Set<MaterialShape>> RelatedShapes = createRelatedShapes();
    private static Map<MaterialShape, Set<MaterialShape>> createRelatedShapes(){
        // 添加关联材料种类（如果材料拥有主要种类，则自动添加附属种类）
        Map<MaterialShape, Set<MaterialShape>> relatedShapes = new HashMap<>();
        relatedShapes.put(INGOT, Sets.newHashSet(NUGGET));
        relatedShapes.put(DUST, Sets.newHashSet(SMALL_DUST, TINY_DUST));
        relatedShapes.put(GEM, Sets.newHashSet(CHIPPED_GEM, FLAWED_GEM, FLAWLESS_GEM, EXQUISITE_GEM));
        return relatedShapes;
    }

    protected String name;
    protected String registerName;
    protected int liquidVolume;
    public MaterialShape(String name){
        this(name, 144);
    }
    public MaterialShape(String name, String registerName){
        this(name, registerName, 144);
    }
    public MaterialShape(String name, int liquidVolume) {
        this(name, "%s_" + name, liquidVolume);
    }
    public MaterialShape(String name, String registerName, int liquidVolume){
        if (shapes.containsKey(name)){
            throw new IllegalArgumentException("Material Shape "+name+" has already existed!");
        }
        this.name = name;
        this.registerName = registerName;
        this.liquidVolume = liquidVolume;
        shapes.put(name, this);
    }
    public static MaterialShape get(String name){
        return shapes.get(name);
    }
    public static Collection<MaterialShape> getAllShapes(){
        return shapes.values();
    }
    public String getName(){
        return name;
    }
    public String toString(){
        return getName();
    }
    public String getDescriptionId(){
        return "eternalcore.materialShape." + this.getName();
    }
}
