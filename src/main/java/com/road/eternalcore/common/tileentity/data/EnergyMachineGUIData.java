package com.road.eternalcore.common.tileentity.data;

import com.road.eternalcore.common.tileentity.EnergyMachineTileEntity;
import net.minecraft.util.IIntArray;

public class EnergyMachineGUIData implements IIntArray {
    public final EnergyMachineTileEntity tileEntity;
    public EnergyMachineGUIData(EnergyMachineTileEntity tileEntity){
        this.tileEntity = tileEntity;
    }

    public int get(int i) {
        switch (i) {
            case 0:
                return tileEntity.getEnergyStored();
            case 1:
                return tileEntity.getMaxEnergyStored();
            case 2:
                return tileEntity.getTier().getLevel();
            default:
                return 0;
        }
    }

    public void set(int i, int value) {
        // 只能看，不能改
    }

    public int getCount() {
        return 3;
    }
}
