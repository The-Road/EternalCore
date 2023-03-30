package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.api.material.MaterialBlockData;

public class MachineStructureBlock extends AbstractMachineBlock{
    // 这一类方块通过材料属性来获取硬度和爆炸抗性
    protected final MaterialBlockData blockData;

    public MachineStructureBlock(MaterialBlockData blockData) {
        super(blockData.getHullData().getDestroyTime(), blockData.getHullData().getExplosionResistance());
        this.blockData = blockData;
    }
}
