package com.road.eternalcore.common.item.crafting;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipePlacer;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.server.SPlaceGhostRecipePacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Iterator;

public class ServerRecipePlacer<C extends IInventory> implements IRecipePlacer<Integer> {
   // 重写了配方书的相关操作，修复原版相关BUG
   protected static final Logger LOGGER = LogManager.getLogger();
   protected final RecipeItemHelper stackedContents = new RecipeItemHelper();
   protected PlayerInventory inventory;
   protected RecipeBookContainer<C> menu;
   // 添加临时物品栏用于储存更改选中的配方时的物品变化
   protected Inventory tempInventory;

   public ServerRecipePlacer(RecipeBookContainer<C> container) {
      this.menu = container;
      tempInventory = new Inventory(container.getGridWidth() * container.getGridHeight());
   }

   public void recipeClicked(ServerPlayerEntity player, @Nullable IRecipe<C> recipe, boolean isShiftDown) {
      if (recipe != null && player.getRecipeBook().contains(recipe)) {
         this.inventory = player.inventory;
         if (this.testClearGrid() || player.isCreative()) {
            this.stackedContents.clear();
            player.inventory.fillStackedContents(this.stackedContents);
            this.menu.fillCraftSlotsStackedContents(this.stackedContents);
            if (this.stackedContents.canCraft(recipe, null)) {
               this.handleRecipeClicked(recipe, isShiftDown);
            } else {
               this.clearGrid();
               player.connection.send(new SPlaceGhostRecipePacket(player.containerMenu.containerId, recipe));
            }
            player.inventory.setChanged();
            tempInventory.clearContent();
         }
      }
   }
   protected void clearGrid() {
      for(int i = 0; i < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; ++i) {
         if (gridCanClear(i)) {
            this.moveItemToInventory(i);
         }
      }
      this.menu.clearCraftingContent();
   }
   // 额外添加的方法，用于判断格子是否可被清理
   protected boolean gridCanClear(int slot){
      return slot != this.menu.getResultSlotIndex();
   }
   protected void moveItemToInventory(int slot) {
      ItemStack itemstack = this.menu.getSlot(slot).getItem();
      if (!itemstack.isEmpty()) {
         for(; itemstack.getCount() > 0; this.menu.getSlot(slot).remove(1)) {
            int i = this.inventory.getSlotWithRemainingSpace(itemstack);
            if (i == -1) {
               i = this.inventory.getFreeSlot();
            }
            if (!this.inventory.add(i, itemstack)){
               tempInventory.addItem(itemstack);
               break;
            }
         }

      }
   }
   protected void clearTempInventory(){
      for(ItemStack itemStack: tempInventory.removeAllItems()){
         this.inventory.player.drop(itemStack, false);
      }
   }

   protected void handleRecipeClicked(IRecipe<C> recipe, boolean isShiftDown) {
      boolean recipeMatches = this.menu.recipeMatches(recipe);
      int biggestCraftableStack = this.stackedContents.getBiggestCraftableStack(recipe, null);
      int nowCraftStack = 0;
      if (recipeMatches) {
         nowCraftStack = this.getStackSize(false, 0, true) - 1;
         if (nowCraftStack >= biggestCraftableStack){
            return;
         }
      }

      int targetCraftStack = this.getStackSize(isShiftDown, biggestCraftableStack, recipeMatches);
      IntList slots = new IntArrayList();
      if (this.stackedContents.canCraft(recipe, slots, targetCraftStack)) {
         int recipeStackSize = targetCraftStack;

         for(int slot : slots) {
            int i = RecipeItemHelper.fromStackingIndex(slot).getMaxStackSize();
            if (i < recipeStackSize) {
               recipeStackSize = i;
            }
         }

         if (this.stackedContents.canCraft(recipe, slots, recipeStackSize)) {
            this.clearGrid();
            this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, slots.iterator(), recipeStackSize);
         }
      }
      this.clearTempInventory();
   }

   public void addItemToSlot(Iterator<Integer> iterator, int slotId, int num, int slotY, int slotX) {
      Slot slot = this.menu.getSlot(slotId);
      ItemStack itemstack = RecipeItemHelper.fromStackingIndex(iterator.next());
      if (!itemstack.isEmpty()) {
         for(int i = 0; i < num; ++i) {
            this.moveItemToGrid(slot, itemstack);
         }
      }
   }

   protected int getStackSize(boolean isShiftDown, int biggestCraftableStack, boolean recipeMatches) {
      int stackSize = 1;
      if (isShiftDown) {
         stackSize = biggestCraftableStack;
      } else if (recipeMatches) {
         stackSize = 64;

         for(int i = 0; i < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; ++i) {
            if (i != this.menu.getResultSlotIndex()) {
               ItemStack itemstack = this.menu.getSlot(i).getItem();
               if (!itemstack.isEmpty() && stackSize > itemstack.getCount()) {
                  stackSize = itemstack.getCount();
               }
            }
         }

         if (stackSize < 64) {
            ++stackSize;
         }
      }

      return stackSize;
   }

   protected void moveItemToGrid(Slot slot, ItemStack itemStack) {
      Pair<IInventory, Integer> pair = findSlotMatchingUnusedItem(itemStack);
      if (pair != null){
         IInventory inventory = pair.getFirst();
         int i = pair.getSecond();
         ItemStack itemstack = inventory.getItem(i).copy();
         if (!itemstack.isEmpty()) {
            if (itemstack.getCount() > 1) {
               inventory.removeItem(i, 1);
            } else {
               inventory.removeItemNoUpdate(i);
            }

            itemstack.setCount(1);
            if (slot.getItem().isEmpty()) {
               slot.set(itemstack);
            } else {
               slot.getItem().grow(1);
            }
         }
      }
   }
   protected Pair<IInventory, Integer> findSlotMatchingUnusedItem(ItemStack itemStack){
      for(int j = 0; j < tempInventory.getContainerSize(); j++){
         ItemStack itemStack1 = tempInventory.getItem(j);
         if (!itemStack1.isEmpty() && itemStack.getItem() == itemStack1.getItem() &&
                 ItemStack.tagMatches(itemStack, itemStack1) && !itemStack1.isDamaged() &&
                 !itemStack1.isEnchanted() && !itemStack1.hasCustomHoverName()) {
            return Pair.of(tempInventory, j);
         }
      }
      int i = this.inventory.findSlotMatchingUnusedItem(itemStack);
      if (i != -1) {
         return Pair.of(this.inventory, i);
      }
      return null;
   }
   private boolean testClearGrid() {
      return true;
   }
}