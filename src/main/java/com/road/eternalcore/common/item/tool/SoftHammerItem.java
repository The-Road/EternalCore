package com.road.eternalcore.common.item.tool;

public class SoftHammerItem extends CustomToolItem{
    public SoftHammerItem(){
        super(null, 0.0F, 1.0F, new Properties().addToolType(ModToolType.SOFT_HAMMER, 0));
        this.mineSpeedRate = 0.1F;
    }
    public boolean forCrafting() {
        return true;
    }
}
