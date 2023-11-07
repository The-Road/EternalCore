package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.Utils;
import com.road.eternalcore.api.material.MaterialBlockData;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MachineCasingBlock extends MachineStructureBlock{
    public static final String NAME = "machine_casing";
    // 统一DescriptionId，因为不同机器外壳的注册ID不一样
    private static final String DESCRIPTION_ID = Utils.BlockDescriptionId(NAME);
    public MachineCasingBlock(MaterialBlockData blockData) {
        super(blockData, blockData.getCasingData());
    }

    public IFormattableTextComponent customBlockName(){
        return new TranslationTextComponent(DESCRIPTION_ID, blockData.getMaterial().getText());
    }
}
