package com.road.eternalcore.common.item.tool;

import com.road.eternalcore.common.item.group.ModGroup;

public class CustomCraftToolItem extends CustomTierItem{
    public CustomCraftToolItem(Properties properties) {
        super(properties.tab(ModGroup.toolGroup));
    }

    public boolean forCrafting() {
        return true;
    }
}
