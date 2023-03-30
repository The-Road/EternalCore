package com.road.eternalcore.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;

public class ToolCraftingInventory implements IInventory, IRecipeHelperPopulator {
    private final CraftingInventory craftSlots;
    private final CraftingInventory toolSlots;
    private final int craftSlotsSize;
    public ToolCraftingInventory(CraftingInventory craftSlots, CraftingInventory toolSlots){
        this.craftSlots = craftSlots;
        this.toolSlots = toolSlots;
        this.craftSlotsSize = craftSlots.getContainerSize();
    }
    public CraftingInventory getCraftSlots(){
        return craftSlots;
    }
    public CraftingInventory getToolSlots(){
        return toolSlots;
    }

    public int getContainerSize() {
        return craftSlots.getContainerSize() + toolSlots.getContainerSize();
    }

    public boolean isEmpty() {
        return craftSlots.isEmpty();
    }

    public ItemStack getItem(int slotId) {
        if (slotId < craftSlotsSize){
            return craftSlots.getItem(slotId);
        }else{
            return toolSlots.getItem(slotId - craftSlotsSize);
        }
    }

    public ItemStack removeItem(int slotId, int num) {
        if (slotId < craftSlotsSize){
            return craftSlots.removeItem(slotId, num);
        }else{
            return toolSlots.removeItem(slotId - craftSlotsSize, num);
        }
    }

    public ItemStack removeItemNoUpdate(int slotId) {
        if (slotId < craftSlotsSize){
            return craftSlots.removeItemNoUpdate(slotId);
        }else{
            return toolSlots.removeItemNoUpdate(slotId - craftSlotsSize);
        }
    }

    public void setItem(int slotId, ItemStack itemStack) {
        if (slotId < craftSlotsSize){
            craftSlots.setItem(slotId, itemStack);
        }else{
            toolSlots.setItem(slotId - craftSlotsSize, itemStack);
        }
    }

    public void setChanged() {
    }

    public boolean stillValid(PlayerEntity p_70300_1_) {
        return true;
    }

    public void clearContent() {
        craftSlots.clearContent();
        toolSlots.clearContent();
    }

    public void fillStackedContents(RecipeItemHelper recipeItemHelper) {
        // 这是用来根据物品数量判断配方书的"可合成"页面是否显示该物品用的
        craftSlots.fillStackedContents(recipeItemHelper);
        toolSlots.fillStackedContents(recipeItemHelper);
    }
}
