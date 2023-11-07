package com.road.eternalcore.common.item.group;

import com.road.eternalcore.Utils;
import com.road.eternalcore.common.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class FunctionalBlockGroup extends ItemGroup {
    public FunctionalBlockGroup(){
        super(Utils.MOD_ID + ".functional_block_group");
    }

    @Override
    public ItemStack makeIcon(){
        return new ItemStack(ModBlocks.handcraftAssemblyTable);
    }
}
