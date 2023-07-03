package com.road.eternalcore.common.inventory.container.slot;

import com.road.eternalcore.common.item.tool.CustomTierItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CraftingToolSlot extends Slot {
    public CraftingToolSlot(IInventory inventory, int index, int posX, int posY) {
        super(inventory, index, posX, posY);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        // 判断是否为工具物品，这里没有用CraftToolType的match（因为太麻烦）
        Item item = itemStack.getItem();
        if (item instanceof CustomTierItem){
           return ((CustomTierItem) item).forCrafting();
        };
        return false;
    }
}
