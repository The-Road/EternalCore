package com.road.eternalcore.common.inventory;

import com.road.eternalcore.api.material.MaterialTierData;
import com.road.eternalcore.common.inventory.container.PartCraftTableContainer;
import com.road.eternalcore.common.item.tool.CustomTierItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class PartCraftTableInventory extends Inventory {
    private PartCraftTableContainer container;

    public PartCraftTableInventory(PartCraftTableContainer container){
        super(3);
        this.container = container;
    }
    public void setChanged() {
        super.setChanged();
        container.slotsChanged(this);
    }
    public int getSmithingLevel(){
        ItemStack itemStack = getItem(0);
        if (itemStack.getItem() instanceof CustomTierItem){
            MaterialTierData tierData = CustomTierItem.getMaterialData(itemStack);
            return tierData.getSmithLevel();
        }else{
            return 0;
        }
    }
}
