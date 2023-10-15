package com.road.eternalcore.api.material;

import com.road.eternalcore.Utils;
import com.road.eternalcore.api.RGB;
import com.road.eternalcore.data.tags.ModTags;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyValue;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.road.eternalcore.api.material.MaterialShape.*;

public class Materials {
    protected static final Map<String, Materials> materials = new LinkedHashMap<>();
    // 无材质，用于设置一些默认值
    public static final Materials NULL = new Materials("", Type.OTHER){
        public String getDescriptionId(){
            return "eternalcore.materials.null";
        }
    };
    // 单质元素
    public static final Materials LITHIUM = addSolid("lithium");
    public static final Materials BERYLLIUM = addSolid("beryllium");
    public static final Materials CARBON = addSolid("carbon");
    public static final Materials MAGNESIUM = addSolid("magnesium");
    public static final Materials ALUMINIUM = addSolid("aluminium");
    public static final Materials SILICON = addSolid("silicon");
    public static final Materials POTASSIUM = addSolid("potassium");
    public static final Materials TITANIUM = addSolid("titanium");
    public static final Materials VANADIUM = addSolid("vanadium");
    public static final Materials CHROME = addSolid("chrome");
    public static final Materials MANGANESE = addSolid("manganese");
    public static final Materials IRON = addSolid("iron");
    public static final Materials COBALT = addSolid("cobalt");
    public static final Materials NICKEL = addSolid("nickel");
    public static final Materials COPPER = addSolid("copper");
    public static final Materials ZINC = addSolid("zinc");
    public static final Materials GALLIUM = addSolid("gallium");
    public static final Materials ARSENIC = addSolid("arsenic");
    public static final Materials YTTRIUM = addSolid("yttrium");
    public static final Materials NIOBIUM = addSolid("niobium");
    public static final Materials MOLYBDENUM = addSolid("molybdenum");
    public static final Materials PALLADIUM = addSolid("palladium");
    public static final Materials SILVER = addSolid("silver");
    public static final Materials INDIUM = addSolid("indium");
    public static final Materials TIN = addSolid("tin");
    public static final Materials ANTIMONY = addSolid("antimony");
    public static final Materials CAESIUM = addSolid("caesium");
    public static final Materials CERIUM = addSolid("cerium");
    public static final Materials NEODYMIUM = addSolid("neodymium");
    public static final Materials EUROPIUM = addSolid("europium");
    public static final Materials LUTETIUM = addSolid("lutetium");
    public static final Materials TANTALUM = addSolid("tantalum");
    public static final Materials TUNGSTEN = addSolid("tungsten");
    public static final Materials OSMIUM = addSolid("osmium");
    public static final Materials IRIDIUM = addSolid("iridium");
    public static final Materials PLATINUM = addSolid("platinum");
    public static final Materials GOLD = addSolid("gold");
    public static final Materials LEAD = addSolid("lead");
    public static final Materials BISMUTH = addSolid("bismuth");
    public static final Materials THORIUM = addSolid("thorium");
    public static final Materials U235 = addSolid("uranium235");
    public static final Materials U238 = addSolid("uranium");
    public static final Materials Pu239 = addSolid("plutonium");
    public static final Materials Pu241 = addSolid("plutonium241");
    public static final Materials AMERICIUM = addSolid("americium");
    public static final Materials NEUTRONIUM = addSolid("neutronium");
    // 合金/复合材料
    public static final Materials BRONZE = addSolid("bronze");
    public static final Materials BRASS = addSolid("brass");
    public static final Materials INVAR = addSolid("invar");
    public static final Materials ELECTRUM = addSolid("electrum");
    public static final Materials WROUGHT_IRON = addSolid("wrought_iron");
    public static final Materials PIG_IRON = addSolid("pig_iron");
    public static final Materials STEEL = addSolid("steel");
    public static final Materials STAINLESS_STEEL = addSolid("stainless_steel");
    public static final Materials REDSTONE_ALLOY = addSolid("redstone_alloy");
    public static final Materials CUPRONICKEL = addSolid("cupronickel");
    public static final Materials NICHROME = addSolid("nichrome");
    public static final Materials KANTHAL = addSolid("kanthal");
    public static final Materials MAGNALIUM = addSolid("magnalium");
    public static final Materials NETHERITE = addSolid("netherite").fireResistant();
    // 宝石类材料
    public static final Materials DIAMOND = addGem("diamond");
    public static final Materials EMERALD = addGem("emerald");

    // 矿物类材料
    public static final Materials COAL = addMineral("coal");
    public static final Materials QUARTZ = addMineral("quartz");
    public static final Materials FLINT = addMineral("flint");

    // 粉末类材料
    public static final Materials REDSTONE = addPowder("redstone");

    // 其他材料
    public static Materials WOOD = addCustom("wood", DUST)
            .setIngredientTag(() -> ItemTags.PLANKS);
    public static Materials STONE = addCustom("stone", DUST)
            .setIngredientTag(() -> ItemTags.STONE_TOOL_MATERIALS);

    // ----实例变量----
    protected final String name;
    protected final Type type;
    protected Set<MaterialShape> shapes = new HashSet<>();
    protected RGB rgb = new RGB(255, 255, 255);
    protected boolean isFireResistant = false;
    protected LazyValue<ITag<Item>> customIngredientTag;
    public Materials(String name, Type type){
        if (Objects.equals(name, "null")){
            throw new IllegalStateException("Cannot register null as material name.");
        }
        if (materials.containsKey(name)){
            throw new IllegalStateException("Material "+name+" has already existed!");
        }
        this.name = name;
        this.type = type;
        // null不记录在用于查询和批量注册的材料中
        if (!name.isEmpty()) {
            materials.put(name, this);
        }
    }
    public static String getRegisterName(MaterialShape shape, Materials material){
        return String.format(shape.registerName, material.getName());
    }
    // ========
    // 读取材料
    // ========
    public static Materials get(String name){
        return materials.getOrDefault(name, NULL);
    }
    public static Collection<Materials> getAllMaterials(){
        return materials.values();
    }
    public static Collection<Materials> getMaterials(Type type){
        return materials.values().stream().filter(material -> material.getType() == type).collect(Collectors.toList());
    }
    // ========
    // 添加新材料
    // ========
    protected static Materials addCustom(String name, MaterialShape... shapes){
        Materials newMaterial = new Materials(name, Type.OTHER);
        newMaterial.addShape(shapes);
        return newMaterial;
    }
    protected static Materials addSolid(String name){
        Materials newMetal = new Materials(name, Type.SOLID);
        newMetal.addShape(INGOT, DUST, PLATE, ROD);
        return newMetal;
    }
    protected static Materials addGem(String name){
        Materials newGem = new Materials(name, Type.GEM);
        newGem.addShape(GEM, DUST, PLATE, ROD);
        return newGem;
    }
    protected static Materials addMineral(String name){
        Materials newMineral = new Materials(name, Type.MINERAL);
        newMineral.addShape(MINERAL, DUST);
        return newMineral;
    }
    protected static Materials addPowder(String name){
        Materials newPowder = new Materials(name, Type.POWDER);
        newPowder.addShape(DUST);
        return newPowder;
    }
    protected Materials fireResistant() {
        this.isFireResistant = true;
        return this;
    }
    // 获取属性
    public String getName(){
        return name;
    }
    public String toString(){
        return getName();
    }
    public Type getType(){
        return type;
    }
    public Set<MaterialShape> getShapes(){
        return shapes;
    }
    public RGB getRGB(){
        return this.rgb;
    }
    public Item.Properties getProperties(){
        if (this.isFireResistant){
            return new Item.Properties().fireResistant();
        }else{
            return new Item.Properties();
        }
    }
    // 添加材料对应的种类
    protected Materials addShape(MaterialShape shape){
        this.shapes.add(shape);
        if (RelatedShapes.containsKey(shape)){
            this.shapes.addAll(RelatedShapes.get(shape));
        }
        return this;
    }
    protected Materials addShape(MaterialShape... shapes){
        for(MaterialShape shape : shapes){
            addShape(shape);
        }
        return this;
    }
    protected Materials setRGB(int r, int g, int b){
        this.rgb = new RGB(r, g, b);
        return this;
    }

    protected Materials setIngredientTag(Supplier<ITag<Item>> tag){
        this.customIngredientTag = new LazyValue<>(tag);
        return this;
    }
    public ITag<Item> getIngredientTag(){
        if (this.customIngredientTag != null){
            return this.customIngredientTag.get();
        }
        if (this.type.ingredientShape != null){
            return ModTags.Items.getMaterialTag(this.type.ingredientShape, this);
        }
        return null;
    }

    public String getDescriptionId(){
        return Utils.MOD_ID + ".materials." + this.getName();
    }
    public TranslationTextComponent getText(){
        return new TranslationTextComponent(getDescriptionId());
    }

    public enum Type{
        SOLID(INGOT), // 固体类，本体材料为锭
        GEM(MaterialShape.GEM), // 宝石类，本体材料为宝石
        MINERAL(MaterialShape.MINERAL), // 矿物类，例如煤、石英等，和宝石类似但是没有gem，本体材料标签无前缀，矿石可以筛
        POWDER(DUST), // 粉末类，如红石、各种矿石的最终产物等，本体材料属于dust
        OTHER(null); // 其他特殊材料，如木头、石头等，本体材料单独指定

        public final MaterialShape ingredientShape;
        Type(MaterialShape ingredientShape){
            this.ingredientShape = ingredientShape;
        }
    }
}
