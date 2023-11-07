package com.road.eternalcore.api.material;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MaterialBlockData {
    // 管理方块属性，包括硬度和爆炸抗性之类的
    protected static final Map<Materials, MaterialBlockData> DATA = new LinkedHashMap<>();
    public static final MaterialBlockData NULL = new MaterialBlockData(Materials.NULL)
            .block(5.0F, 6.0F)
            .casing(3.0F, 4.8F);
    private static void init(){
        setData(Materials.BRONZE)
                .block(5.0F, 6.0F)
                .casing(3.5F, 5.0F);
        setData(Materials.WROUGHT_IRON)
                .block(5.0F, 6.0F)
                .casing(3.5F, 5.0F);
        setData(Materials.STONE).stone()
                .brickedCasing(3.5F, 3.5F);
    }

    protected Materials material;
    protected boolean isStone; // 石头方块注册的时候采用Material.STONE，采集工具为镐子而非扳手
    protected BlockHardnessData blockData; // 实心块数据
    protected BlockHardnessData casingData; // 机器外壳数据
    protected BlockHardnessData brickedCasingData; // 砖砌外壳数据
    private MaterialBlockData(Materials material){
        this.material = material;
    }
    protected static MaterialBlockData setData(Materials material){
        if (DATA.containsKey(material)){
            throw new IllegalArgumentException("MaterialSmeltData "+material+" has already existed!");
        }
        MaterialBlockData data = new MaterialBlockData(material);
        DATA.put(material, data);
        return data;
    }
    public static Map<Materials, MaterialBlockData> getData(){
        return DATA;
    }
    public static Collection<MaterialBlockData> getValidBlockData(){
        return DATA.values().stream().filter(
                data -> data.blockData != null
        ).collect(Collectors.toList());
    }
    public static Collection<MaterialBlockData> getValidCasingData(){
        return DATA.values().stream().filter(
                data -> data.casingData != null
        ).collect(Collectors.toList());
    }
    public static Collection<MaterialBlockData> getValidBrickedCasingData(){
        return DATA.values().stream().filter(
                data -> data.brickedCasingData != null
        ).collect(Collectors.toList());
    }
    public static MaterialBlockData get(String name){
        return DATA.getOrDefault(Materials.get(name), NULL);
    }
    public static MaterialBlockData get(Materials material){
        return DATA.getOrDefault(material, NULL);
    }
    public Materials getMaterial() {
        return material;
    }
    public boolean isStone(){
        return isStone;
    }
    public BlockHardnessData getBlockData() {
        return blockData;
    }
    public BlockHardnessData getCasingData() {
        return casingData;
    }
    public BlockHardnessData getBrickedCasingData(){
        return brickedCasingData;
    }

    protected MaterialBlockData stone(){
        this.isStone = true;
        return this;
    }

    protected MaterialBlockData block(float destroyTime, float explosionResistance){
        this.blockData = new BlockHardnessData(destroyTime, explosionResistance);
        return this;
    }
    protected MaterialBlockData casing(float destroyTime, float explosionResistance){
        this.casingData = new BlockHardnessData(destroyTime, explosionResistance);
        return this;
    }
    protected MaterialBlockData brickedCasing(float destroyTime, float explosionResistance){
        this.brickedCasingData = new BlockHardnessData(destroyTime, explosionResistance);
        return this;
    }

    public static class BlockHardnessData {
        private final float destroyTime;
        private final float explosionResistance;
        protected BlockHardnessData(float destroyTime, float explosionResistance){
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

    static {init();}
}
