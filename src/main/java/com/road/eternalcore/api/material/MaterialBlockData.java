package com.road.eternalcore.api.material;

import java.util.LinkedHashMap;
import java.util.Map;

public class MaterialBlockData {
    // 管理方块属性，包括硬度和爆炸抗性之类的
    protected static final Map<Materials, MaterialBlockData> materialBlockData = new LinkedHashMap<>();
    public static final MaterialBlockData NULL = new MaterialBlockData(Materials.NULL)
            .block(5.0F, 6.0F)
            .hull(3.0F, 4.8F);

    public static final MaterialBlockData IRON = setData(Materials.IRON)
            .block(5.0F, 6.0F)
            .hull(3.0F, 4.8F);
    public static final MaterialBlockData BRONZE = setData(Materials.BRONZE)
            .block(5.0F, 6.0F)
            .hull(3.0F, 4.8F);

    protected Materials material;
    protected BlockData blockData; // 实心块数据
    protected BlockData hullData; // 机器外壳数据
    private MaterialBlockData(Materials material){
        this.material = material;
    }
    protected static MaterialBlockData setData(Materials material){
        if (materialBlockData.containsKey(material)){
            throw new IllegalStateException("MaterialSmeltData "+material+" has already existed!");
        }
        MaterialBlockData data = new MaterialBlockData(material);
        materialBlockData.put(material, data);
        return data;
    }
    public static Map<Materials, MaterialBlockData> getData(){
        return materialBlockData;
    }
    public static MaterialBlockData get(String name){
        return materialBlockData.getOrDefault(Materials.get(name), NULL);
    }
    public static MaterialBlockData get(Materials material){
        return materialBlockData.getOrDefault(material, NULL);
    }
    public Materials getMaterial() {
        return material;
    }
    public BlockData getBlockData() {
        return blockData;
    }
    public BlockData getHullData() {
        return hullData;
    }

    protected MaterialBlockData block(float destroyTime, float explosionResistance){
        this.blockData = new BlockData(destroyTime, explosionResistance);
        return this;
    }
    protected MaterialBlockData hull(float destroyTime, float explosionResistance){
        this.hullData = new BlockData(destroyTime, explosionResistance);
        return this;
    }

    public static class BlockData{
        private final float destroyTime;
        private final float explosionResistance;
        protected BlockData(float destroyTime, float explosionResistance){
            this.destroyTime = destroyTime;
            this.explosionResistance = explosionResistance;
        }
        public float getDestroyTime() {
            return destroyTime;
        }
        public float getExplosionResistance() {
            return explosionResistance;
        }
    }
}
