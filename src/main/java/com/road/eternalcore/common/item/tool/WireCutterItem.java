package com.road.eternalcore.common.item.tool;

import com.road.eternalcore.common.block.pipe.WireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public class WireCutterItem extends CustomToolItem{
    public WireCutterItem(){
        super(0.0F, 1.2F, new Properties().addToolType(ModToolType.WIRE_CUTTER, 0));
    }

    public boolean canHarvestBlock(ItemStack itemStack, BlockState blockState){
        return blockState.getBlock() instanceof WireBlock;
    }
}
