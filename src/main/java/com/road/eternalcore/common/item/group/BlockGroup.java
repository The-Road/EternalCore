package com.road.eternalcore.common.item.group;

import com.road.eternalcore.Utils;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.common.block.ore.OreBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BlockGroup extends ItemGroup {
    public BlockGroup(){
        super(Utils.MOD_ID + ".block_group");
    }

    @Override
    public ItemStack makeIcon(){
        return new ItemStack(OreBlocks.get(Ores.COPPER_ORE));
    }
}
