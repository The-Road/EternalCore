package com.road.eternalcore.common.item.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.RecipeBookContainer;

public class ServerRecipePlacerToolCrafting<C extends IInventory> extends ServerRecipePlacer<C> {
    public ServerRecipePlacerToolCrafting(RecipeBookContainer<C> container) {
        super(container);
    }
    protected void clearGrid(){
        for(int i = 10; i < 13; i++) {
            moveItemToInventory(i);
        }
        super.clearGrid();
    }
}
