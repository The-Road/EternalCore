package com.road.eternalcore.common.block;

import com.road.eternalcore.TranslationUtils;
import net.minecraft.block.Block;
import net.minecraft.util.text.IFormattableTextComponent;

public class ModBlock extends Block {
    public ModBlock(Properties properties) {
        super(properties);
    }
    public final IFormattableTextComponent getName(){
        if (TranslationUtils.hasTranslationName(this)){
            return super.getName();
        } else {
            return customBlockName();
        }
    }
    public IFormattableTextComponent customBlockName(){
        return super.getName();
    }
}
