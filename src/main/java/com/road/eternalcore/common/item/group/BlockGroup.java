package com.road.eternalcore.common.item.group;

import com.road.eternalcore.Utils;
import com.road.eternalcore.common.item.block.BlockItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BlockGroup extends ItemGroup {
    public BlockGroup(){
        super(Utils.MOD_ID + ".block_group");
    }

    @Override
    public ItemStack makeIcon(){
        return new ItemStack(BlockItems.get("copper_ore"));
    }
}
