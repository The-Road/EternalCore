package com.road.eternalcore.common.item.tool;

public class SoftMalletItem extends CustomToolItem{
    public SoftMalletItem(){
        super(null, 0.0F, 1.0F, new Properties().addToolType(ModToolType.SOFT_MALLET, 0));
        this.mineSpeedRate = 0.1F;
    }
}
