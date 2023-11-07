package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.api.material.MaterialBlockData;

public class BrickedCasingBlock extends MachineStructureBlock{
    public static final String NAME = "machine_casing";
    public BrickedCasingBlock(MaterialBlockData blockData){
        super(blockData, blockData.getBrickedCasingData());
    }

}
