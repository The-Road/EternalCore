package com.road.eternalcore.api.material;

import com.road.eternalcore.api.machine.BlastFurnaceProcessData;

import java.util.LinkedHashMap;
import java.util.Map;

public class MaterialSmeltData {
    // 管理材料的加工和冶炼数据，例如是否需要用高炉冶炼等
    protected static final Map<Materials, MaterialSmeltData> DATA = new LinkedHashMap<>();
    private static void init(){
        setData(Materials.IRON);
        setData(Materials.COPPER);
        setData(Materials.TIN);
    }
    protected Materials material;
    // 高炉数据，为null表示直接在熔炉里烧，否则表示需要在高炉里烧（粉烧锭之类的）
    protected BlastFurnaceProcessData blastFurnaceData = null;
    protected float smeltExp = 0.0F;

    private MaterialSmeltData(Materials material){
        this.material = material;
    }
    protected static MaterialSmeltData setData(Materials material){
        if (DATA.containsKey(material)){
            throw new IllegalArgumentException("MaterialSmeltData "+material+" has already existed!");
        }
        MaterialSmeltData data = new MaterialSmeltData(material);
        DATA.put(material, data);
        return data;
    }

    public static Map<Materials, MaterialSmeltData> getData(){
        return DATA;
    }
    public static MaterialSmeltData get(String name){
        return DATA.get(Materials.get(name));
    }
    public static MaterialSmeltData get(Materials material){
        return DATA.get(material);
    }
    public Materials getMaterial(){
        return material;
    }
    public BlastFurnaceProcessData getBlastFurnaceData(){
        return blastFurnaceData;
    }
    public float getSmeltExp(){
        return smeltExp;
    }

    protected MaterialSmeltData blastFurnace(int voltage, int current, int time, int temperature){
        this.blastFurnaceData = new BlastFurnaceProcessData(voltage, current, time, temperature);
        return this;
    }
    protected MaterialSmeltData exp(float smeltExp){
        this.smeltExp = smeltExp;
        return this;
    }

    public boolean canSmeltInFurnace(){
        return blastFurnaceData == null;
    }

    static {init();}
}
