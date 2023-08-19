package com.road.eternalcore.common.block.ore;

import com.road.eternalcore.api.ore.OreShape;
import com.road.eternalcore.api.ore.Ores;
import net.minecraft.block.Block;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class OreBlock extends Block {
    protected final Ores ore;
    public OreBlock(Ores ore) {
        super(ore.getBlockProperties());
        this.ore = ore;
    }
    public IFormattableTextComponent getName(){
        return new TranslationTextComponent(OreShape.ORE.getDescriptionId(), ore.getMainProduct().getText());
    }
}
