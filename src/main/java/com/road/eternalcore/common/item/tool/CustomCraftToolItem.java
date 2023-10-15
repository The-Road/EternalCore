package com.road.eternalcore.common.item.tool;

import com.road.eternalcore.common.item.group.ModGroup;

public abstract class CustomCraftToolItem extends CustomTierItem{
    public CustomCraftToolItem(Properties properties) {
        super(properties.tab(ModGroup.toolGroup));
        this.onlyForCrafting = true;
    }
}
