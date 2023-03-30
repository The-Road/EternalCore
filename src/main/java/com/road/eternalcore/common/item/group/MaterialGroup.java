package com.road.eternalcore.common.item.group;

import com.road.eternalcore.Utils;
import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.item.material.MaterialItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MaterialGroup extends ItemGroup {
    public MaterialGroup(){
        super(Utils.MOD_ID + ".material_group");
    }

    @Override
    public ItemStack makeIcon(){
        return new ItemStack(MaterialItems.get(MaterialShape.INGOT, Materials.COPPER));
    }
}
