package com.road.eternalcore.api.energy.eu;

import com.road.eternalcore.Utils;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.*;

public class EUTier implements IEUTier{
    // 电压等级，注意这里的电压等级顺序是写死的（按照注册的顺序排序），不要改动
    protected static final Map<String, EUTier> tiers = new LinkedHashMap<>();
    protected static final List<EUTier> tierList = new ArrayList<>(15);

    public static final EUTier ULV = new EUTier("ulv", 2 << 2); // 8
    public static final EUTier LV = new EUTier("lv", 2 << 4); // 32
    public static final EUTier MV = new EUTier("mv", 2 << 6); // 128
    public static final EUTier HV = new EUTier("hv", 2 << 8); // 512
    public static final EUTier EV = new EUTier("ev", 2 << 10); // 2048
    public static final EUTier IV = new EUTier("iv", 2 << 12); // 8192
    public static final EUTier LuV = new EUTier("luv", 2 << 14); // 32768
    public static final EUTier ZPM = new EUTier("zpm", 2 << 16); // 131072
    public static final EUTier UV = new EUTier("uv", 2 << 18); // 524288
    public static final EUTier ELV = new EUTier("elv", 2 << 20); // 2097152
    public static final EUTier EMV = new EUTier("emv", 2 << 22); // 8388608
    public static final EUTier EHV = new EUTier("ehv", 2 << 24); // 33554432
    public static final EUTier EEV = new EUTier("eev", 2 << 26); // 134217728
    public static final EUTier EIV = new EUTier("eiv", 2 << 28); // 536870912
    public static final EUTier MAX = new EUTier("max", Integer.MAX_VALUE); // 2147483647


    protected final int level;
    protected final String name;
    protected final int maxVoltage;
    private EUTier(String name, int maxVoltage){
        if (tiers.containsKey(name)){
            throw new IllegalStateException("EUTierLevel "+name+" has already existed!");
        }
        this.name = name;
        this.maxVoltage = maxVoltage;
        tiers.put(name, this);
        this.level = tierList.size();
        tierList.add(this);
    }
    public static EUTier tier(String name){
        return tiers.getOrDefault(name, LV);
    }
    public static EUTier tier(int level){
        if (level >=0 && level < tierList.size()){
            return tierList.get(level);
        }
        return LV;
    }
    public int getLevel() {
        return level;
    }
    public String getName() {
        return name;
    }
    public int getMaxVoltage() {
        return maxVoltage;
    }

    public String getDescriptionId(){
        return Utils.MOD_ID + ".eu.tier." + this.getName();
    }
    public TranslationTextComponent getText(){
        return new TranslationTextComponent(getDescriptionId());
    }
    public EUTier nextTier(){
        return EUTier.tier(getLevel() + 1);
    }
    public EUTier lastTier(){
        return EUTier.tier(getLevel() - 1);
    }

    public EUTier getTier() {
        return this;
    }
}
