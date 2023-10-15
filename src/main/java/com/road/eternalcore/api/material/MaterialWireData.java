package com.road.eternalcore.api.material;

import com.google.common.collect.ImmutableSet;
import com.road.eternalcore.api.energy.eu.EUTier;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.road.eternalcore.api.energy.eu.EUTier.*;
import static com.road.eternalcore.api.material.MaterialWireData.WireType.*;

public class MaterialWireData {
    protected static final Map<Materials, MaterialWireData> materialWireData = new LinkedHashMap<>();
    protected static final Set<WireType> NORMAL_TYPES = ImmutableSet.of(
            WIRE_1X, WIRE_2X, WIRE_4X, WIRE_8X, WIRE_12X, WIRE_16X,
            CABLE_1X, CABLE_2X, CABLE_4X, CABLE_8X, CABLE_12X
    );
    protected static final Set<WireType> AUTO_INSULATION_TYPES = ImmutableSet.of(
            INSULATED_WIRE_1X, INSULATED_WIRE_2X, INSULATED_WIRE_4X, INSULATED_WIRE_8X,
            INSULATED_WIRE_12X, INSULATED_WIRE_16X
    );
    public static final MaterialWireData NULL = new MaterialWireData(Materials.NULL)
            .wire(LV, 1,1);

    private static void init(){
        setData(Materials.REDSTONE_ALLOY)
                .wire(ULV, 1, 1)
                .autoInsulation();
        setData(Materials.TIN)
                .wire(LV, 1,1);
        setData(Materials.COPPER)
                .wire(MV, 1, 1);
        setData(Materials.GOLD)
                .wire(HV, 1, 1);
        setData(Materials.ALUMINIUM)
                .wire(EV, 1, 1);
        setData(Materials.TUNGSTEN)
                .wire(IV, 1, 1);
    }

    protected Materials material;
    protected EUTier euTier;
    protected int maxCurrent;
    protected int lineLoss;
    protected boolean insulated = false;
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
    protected MaterialWireData autoInsulation(){
        this.insulated = true;
        return this;
    }

    public Set<WireType> getWireTypes(){
        return insulated ? AUTO_INSULATION_TYPES : NORMAL_TYPES;
    }
    public static String getRegisterName(Materials material, WireType type){
        return String.format(type.registerName, material.getName());
    }
    public enum WireType {
        WIRE_1X("1x_wire", "1x_%s_wire", 2, 1, 2, false),
        WIRE_2X("2x_wire", "2x_%s_wire", 3, 2, 2, false),
        WIRE_4X("4x_wire", "4x_%s_wire", 4, 4, 2, false),
        WIRE_8X("8x_wire", "8x_%s_wire", 6, 8, 2, false),
        WIRE_12X("12x_wire", "12x_%s_wire", 7, 12, 2, false),
        WIRE_16X("16x_wire", "16x_%s_wire", 8, 16, 2, false),
        CABLE_1X("1x_cable", "1x_%s_cable", 3, 1, 1, true),
        CABLE_2X("2x_cable", "2x_%s_cable", 4, 2, 1, true),
        CABLE_4X("4x_cable", "4x_%s_cable", 5, 4, 1, true),
        CABLE_8X("8x_cable", "8x_%s_cable", 7, 8, 1, true),
        CABLE_12X("12x_cable", "12x_%s_cable", 8, 12, 1, true),
        INSULATED_WIRE_1X("1x_wire", "1x_%s_wire", 2, 1, 1, true),
        INSULATED_WIRE_2X("2x_wire", "2x_%s_wire", 3, 2, 1, true),
        INSULATED_WIRE_4X("4x_wire", "4x_%s_wire", 4, 4, 1, true),
        INSULATED_WIRE_8X("8x_wire", "8x_%s_wire", 6, 8, 1, true),
        INSULATED_WIRE_12X("12x_wire", "12x_%s_wire", 7, 12, 1, true),
        INSULATED_WIRE_16X("16x_wire", "16x_%s_wire", 8, 16, 1, true);

        public final String name;
        private final String registerName;
        public final int radius; // 方块大小
        public final int currentRate; // 电流倍率
        public final int lossRate; // 线损倍率
        public final boolean insulated;

        WireType(String name, String registerName, int radius, int currentRate, int lossRate, boolean insulated){
            this.name = name;
            this.registerName = registerName;
            this.radius = radius;
            this.currentRate = currentRate;
            this.lossRate = lossRate;
            this.insulated = insulated;
        }
    }

    static {init();}
}
