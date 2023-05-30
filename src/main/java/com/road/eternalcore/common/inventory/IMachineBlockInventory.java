package com.road.eternalcore.common.inventory;

import net.minecraft.inventory.IInventory;

import java.util.stream.IntStream;

public interface IMachineBlockInventory extends IInventory {

    int getInputSize();
    int getResultSize();
    int getBatterySize();

    default int getContainerSize(){
        return getInputSize() + getResultSize() + getBatterySize();
    }
    default int[] getInputRange(){
        return IntStream.range(0, getInputSize()).toArray();
    }
    default int[] getResultRange(){
        return IntStream.range(getInputSize(), getInputSize() + getResultSize()).toArray();
    }
    default int[] getBatteryRange(){
        return IntStream.range(getInputSize() + getResultSize(), getInputSize() + getResultSize() + getBatterySize()).toArray();
    }
}
