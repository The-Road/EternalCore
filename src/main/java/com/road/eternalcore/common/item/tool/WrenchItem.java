package com.road.eternalcore.common.item.tool;

import com.road.eternalcore.common.block.machine.AbstractMachineBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public class WrenchItem extends CustomToolItem{
    public WrenchItem(){
        super(1.0F, 1.2F, new Properties().addToolType(ModToolType.WRENCH, 0));
    }

    public boolean canHarvestBlock(ItemStack itemStack, BlockState blockState){
        return blockState.getBlock() instanceof AbstractMachineBlock;
    }
}
