package com.road.eternalcore.common.inventory.container.slot;

import com.road.eternalcore.api.tool.CraftToolType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class CraftingToolSlot extends Slot {
    public CraftingToolSlot(IInventory inventory, int index, int posX, int posY) {
        super(inventory, index, posX, posY);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        // 判断是否为工具物品
        return CraftToolType.isCraftTool(itemStack);
    }
}
