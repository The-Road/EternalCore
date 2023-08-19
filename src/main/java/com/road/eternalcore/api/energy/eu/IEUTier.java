package com.road.eternalcore.api.energy.eu;

public interface IEUTier {
    // 实现电压等级的比较
    EUTier getTier();
    default boolean tierHigherThan(IEUTier another){
        return tierHigherThan(another.getTier().getLevel());
    }
    default boolean tierHigherThan(int tierLevel){
        return getTier().getLevel() > tierLevel;
    }
}
