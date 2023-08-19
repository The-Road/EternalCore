package com.road.eternalcore.api.material;

import com.road.eternalcore.api.energy.eu.EUTier;

import java.util.*;
import java.util.stream.Collectors;

import static com.road.eternalcore.api.energy.eu.EUTier.*;

public class MaterialWireData {
    protected static final Map<Materials, MaterialWireData> materialWireData = new LinkedHashMap<>();
    public static final MaterialWireData NULL = new MaterialWireData(Materials.NULL)
            .wire(LV, 1,0);

    public static final MaterialWireData REDSTONE_ALLOY = setData(Materials.REDSTONE_ALLOY)
            .wire(ULV, 1, 1);
    public static final MaterialWireData TIN = setData(Materials.TIN)
            .wire(LV, 1,1);
    public static final MaterialWireData COPPER = setData(Materials.COPPER)
            .wire(MV, 1, 1);
    public static final MaterialWireData GOLD = setData(Materials.GOLD)
            .wire(HV, 1, 1);
    public static final MaterialWireData ALUMINIUM = setData(Materials.ALUMINIUM)
            .wire(EV, 1, 1);
    public static final MaterialWireData TUNGSTEN = setData(Materials.TUNGSTEN)
            .wire(IV, 1, 1);
    protected Materials material;
    protected EUTier euTier;
    protected int maxCurrent;
    protected int lineLoss;
    private MaterialWireData(Materials material){
        this.material = material;
    }
    protected static MaterialWireData setData(Materials material){
        if (materialWireData.containsKey(material)){
            throw new IllegalStateException("MaterialWireData "+material+" has already existed!");
        }
        MaterialWireData data = new MaterialWireData(material);
        materialWireData.put(material, data);
        return data;
    }
    public static Map<Materials, MaterialWireData> getData(){
        return materialWireData;
    }
    public static MaterialWireData get(String name){
        return materialWireData.getOrDefault(Materials.get(name), NULL);
    }
    public static MaterialWireData get(Materials material){
        return materialWireData.getOrDefault(material, NULL);
    }
    public static Collection<MaterialWireData> findAll(EUTier euTier, int maxCurrent, int lineLoss){
        return materialWireData.values().stream().filter(wireData ->
            wireData.getEuTier() == euTier && wireData.getMaxCurrent() == maxCurrent && wireData.getLineLoss() == lineLoss
        ).collect(Collectors.toList());
    }

    public Materials getMaterial(){
        return material;
    }
    public EUTier getEuTier() {
        return euTier;
    }
    public int getMaxCurrent() {
        return maxCurrent;
    }
    public int getLineLoss() {
        return lineLoss;
    }

    protected MaterialWireData wire(EUTier euTier, int maxCurrent, int lineLoss){
        this.euTier = euTier;
        this.maxCurrent = maxCurrent;
        this.lineLoss = lineLoss;
        return this;
    }
}
