package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.api.energy.eu.ISidedEUStorage;

public interface IEnergyProviderTileEntity extends ISidedEUStorage {
    // 可以输出能量的机器，设置其最大输出电流
    int maxProvideCurrent();
    default boolean canProvideEnergyToNetwork(){
        return getEnergyStored() >= getTier().getMaxVoltage();
    }
}
