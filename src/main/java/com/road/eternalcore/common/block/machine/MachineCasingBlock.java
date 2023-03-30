package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.Utils;
import com.road.eternalcore.api.material.MaterialBlockData;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MachineCasingBlock extends MachineStructureBlock{
    public static final String NAME = "machine_casing";
    public MachineCasingBlock(MaterialBlockData blockData) {
        super(blockData);
    }

    public IFormattableTextComponent getName(){
        return new TranslationTextComponent(this.getDescriptionId(), blockData.getMaterial().getText());
    }
    public String getDescriptionId() {
        return Utils.BlockDescriptionId(NAME);
    }
}
