package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.api.energy.eu.ISidedEUStorage;

public interface IEnergyReceiverTileEntity extends ISidedEUStorage {
    // 可以输入能量的机器，设置其最大输入电流
    int maxReceiveCurrent();
    default boolean canReceiveEnergyFromNetwork(){
        return !isEnergyFull();
    }
}
