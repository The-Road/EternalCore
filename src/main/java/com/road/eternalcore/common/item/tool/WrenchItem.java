package com.road.eternalcore.common.item.tool;

import com.google.common.collect.Sets;
import com.road.eternalcore.common.block.machine.AbstractMachineBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Set;

public class WrenchItem extends CustomToolItem{
    protected static final Set<Block> DIGGABLE_BLOCKS = Sets.newHashSet();
    public WrenchItem(){
        super(DIGGABLE_BLOCKS, 1.0F, 1.2F, new Properties().addToolType(ModToolType.WRENCH, 0));
    }


    public boolean isCorrectToolForDrops(BlockState blockState){
        return blockState.getBlock() instanceof AbstractMachineBlock;
    }
    public boolean forCrafting() {
        return true;
    }
}
