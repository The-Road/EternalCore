package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.api.material.MaterialBlockData;

public abstract class MachineStructureBlock extends AbstractMachineBlock{
    // 这一类方块通过材料属性来获取硬度和爆炸抗性
    protected final MaterialBlockData blockData;

    public MachineStructureBlock(MaterialBlockData blockData, MaterialBlockData.BlockHardnessData hardnessData) {
        super(blockData.isStone() ?
                stoneProperties(hardnessData.getDestroyTime(), hardnessData.getExplosionResistance()) :
                machineProperties(hardnessData.getDestroyTime(), hardnessData.getExplosionResistance())
        );
        this.blockData = blockData;
    }
}
