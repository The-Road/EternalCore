package com.road.eternalcore.common.item.group;

import com.road.eternalcore.Utils;
import com.road.eternalcore.common.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ToolGroup extends ItemGroup {
    public ToolGroup(){
        super(Utils.MOD_ID + ".tool_group");
    }

    @Override
    public ItemStack makeIcon(){
        return new ItemStack(ModItems.hammer);
    }
}
