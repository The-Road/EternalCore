package com.road.eternalcore;

public class ModConstant {
    // 存储物品耐久尾数的NBT
    public static final String Durability_decimal = "etcDbDecimal";
    // 存储材料的NBT
    public static final String Material = "material";
    // 存储覆盖板数据的NBT
    public static final String Machine_cover = "cover";
    // 存储机器电量的NBT
    public static final String Machine_energy = "energy";
    public static final String Machine_euTier = "euTier";

    public static String blockEntityTag(String str){
        return String.format("BlockEntityTag.%s", str);
    }
}
