package com.road.eternalcore.common.inventory;

import com.road.eternalcore.common.inventory.container.SmithingTableContainer;
import net.minecraft.inventory.Inventory;

public class SmithingTableInventory extends Inventory {
    private SmithingTableContainer container;
    private int smithingLevel;

    public SmithingTableInventory(SmithingTableContainer container){
        super(3);
        this.container = container;
        this.smithingLevel = 0;
    }
    public void setChanged() {
        super.setChanged();
        container.slotsChanged(this);
    }

    public void setSmithingLevel(int smithingLevel) {
        this.smithingLevel = smithingLevel;
    }
    public int getSmithingLevel(){
        return smithingLevel;
    }
}
