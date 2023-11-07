package com.road.eternalcore.common.block.ore;

import com.road.eternalcore.api.ore.OreShape;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.common.block.ModBlock;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class OreBlock extends ModBlock {
    protected final Ores ore;
    public OreBlock(Ores ore) {
        super(ore.getBlockProperties());
        this.ore = ore;
    }
    public IFormattableTextComponent customBlockName(){
        return new TranslationTextComponent(OreShape.ORE.getDescriptionId(), ore.getMainProduct().getText());
    }
}
