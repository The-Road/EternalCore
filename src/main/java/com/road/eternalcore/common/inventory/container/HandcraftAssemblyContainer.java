package com.road.eternalcore.common.inventory.container;

import com.road.eternalcore.common.inventory.ToolCraftingInventory;
import com.road.eternalcore.common.inventory.container.slot.CraftingToolSlot;
import com.road.eternalcore.common.inventory.container.slot.ToolCraftingResultSlot;
import com.road.eternalcore.common.item.crafting.ModRecipeType;
import com.road.eternalcore.common.item.crafting.ServerRecipePlacerToolCrafting;
import com.road.eternalcore.common.item.crafting.recipe.IToolCraftingRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

public class HandcraftAssemblyContainer extends RecipeBookContainer<CraftingInventory> {
    private final CraftingInventory craftSlots = new CraftingInventory(this, 3, 3);
    private final CraftingInventory toolSlots = new CraftingInventory(this, 1, 3);
    private final ToolCraftingInventory craftAndToolSlots = new ToolCraftingInventory(craftSlots, toolSlots);
    private final CraftResultInventory resultSlots = new CraftResultInventory();
    private final int playerStartIndex = 13;
    private final IWorldPosCallable access;
    private final Block accessBlock;
    private final PlayerEntity player;
    public HandcraftAssemblyContainer(int containerId, PlayerInventory inventory) {
        this(containerId, inventory, IWorldPosCallable.NULL);
    }
    public HandcraftAssemblyContainer(int containerId, PlayerInventory inventory, IWorldPosCallable posCallable) {
        super(ModContainerType.handcraftAssembly, containerId);
        this.access = posCallable;
        this.accessBlock = posCallable.evaluate((world, blockPos) -> world.getBlockState(blockPos).getBlock(), Blocks.AIR);
        this.player = inventory.player;
        this.addSlot(new ToolCraftingResultSlot(inventory.player, this.craftAndToolSlots, this.resultSlots, 0, 143, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(this.craftSlots, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }
        for(int i = 0; i < 3; i++){
            addSlot(new CraftingToolSlot(toolSlots, i, 36, 17 + i * 18));
        }

        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, 142));
        }

    }

    protected static void slotChangedCraftingGrid(int containerId, World world, PlayerEntity player, ToolCraftingInventory craftAndToolSlots, CraftResultInventory resultSlots) {
        if (!world.isClientSide()) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
            ItemStack itemstack = ItemStack.EMPTY;
            CraftingInventory craftSlots = craftAndToolSlots.getCraftSlots();
            CraftingInventory toolSlots = craftAndToolSlots.getToolSlots();
            if (!toolSlots.isEmpty()){
                Optional<IToolCraftingRecipe> toolRecipeOptional = world.getServer().getRecipeManager().getRecipeFor(ModRecipeType.TOOL_CRAFTING, craftAndToolSlots, world);
                if (toolRecipeOptional.isPresent()) {
                    IToolCraftingRecipe recipe = toolRecipeOptional.get();
                    if (resultSlots.setRecipeUsed(world, serverplayerentity, recipe)) {
                        itemstack = recipe.assemble(craftAndToolSlots);
                    }
                }
            } else {
                Optional<ICraftingRecipe> recipeOptional = world.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, craftSlots, world);
                if (recipeOptional.isPresent()) {
                    ICraftingRecipe recipe = recipeOptional.get();
                    if (resultSlots.setRecipeUsed(world, serverplayerentity, recipe)) {
                        itemstack = recipe.assemble(craftSlots);
                    }
                }
            }
            resultSlots.setItem(0, itemstack);
            serverplayerentity.connection.send(new SSetSlotPacket(containerId, 0, itemstack));
        }
    }

    public void slotsChanged(IInventory inventory) {
        this.access.execute((world, blockPos) -> slotChangedCraftingGrid(this.containerId, world, this.player, this.craftAndToolSlots, this.resultSlots));
    }

    public void fillCraftSlotsStackedContents(RecipeItemHelper recipeItemHelper) {
        this.craftAndToolSlots.fillStackedContents(recipeItemHelper);
    }

    public void clearCraftingContent() {
        this.craftAndToolSlots.clearContent();
        this.resultSlots.clearContent();
    }

    public boolean recipeMatches(IRecipe<? super CraftingInventory> recipe) {
        return recipe.matches(this.craftSlots, this.player.level);
    }

    public void removed(PlayerEntity player) {
        super.removed(player);
        this.access.execute((world, blockPos) -> {
            this.clearContainer(player, world, this.craftAndToolSlots);
        });
    }

    public boolean stillValid(PlayerEntity player) {
        return stillValid(this.access, player, this.accessBlock);
    }

    public ItemStack quickMoveStack(PlayerEntity player, int slotId) {
        ItemStack itemStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack = slot.getItem();
            itemStackCopy = itemStack.copy();
            if (slotId == 0) {
                this.access.execute((world, blockPos) -> {
                    itemStack.getItem().onCraftedBy(itemStack, world, player);
                });
                if (!this.moveItemStackTo(itemStack, playerStartIndex, playerStartIndex + 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack, itemStackCopy);
            } else if (slotId >= playerStartIndex && slotId < playerStartIndex + 36) {
                // 如果是工具则优先放置到左侧工具栏
                if (!this.moveItemStackTo(itemStack, 10, 13, false)) {
                    if (!this.moveItemStackTo(itemStack, 1, 10, false)) {
                        if (slotId < playerStartIndex + 27) {
                            if (!this.moveItemStackTo(itemStack, playerStartIndex + 27, playerStartIndex + 36, false)) {
                                return ItemStack.EMPTY;
                            }
                        } else if (!this.moveItemStackTo(itemStack, playerStartIndex, playerStartIndex + 27, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            } else if (!this.moveItemStackTo(itemStack, playerStartIndex, playerStartIndex + 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack.getCount() == itemStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }
            // 如果合成产物只成功移动了一部分，那么剩下的会掉到地上
            ItemStack remainItem = slot.onTake(player, itemStack);
            if (slotId == 0) {
                player.drop(remainItem, false);
            }
        }

        return itemStackCopy;
    }

    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(itemStack, slot);
    }

    public int getResultSlotIndex() {
        return 0;
    }

    public int getGridWidth() {
        return this.craftSlots.getWidth();
    }

    public int getGridHeight() {
        return this.craftSlots.getHeight();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return playerStartIndex;
    }

    @OnlyIn(Dist.CLIENT)
    public RecipeBookCategory getRecipeBookType() {
        return RecipeBookCategory.CRAFTING;
    }

    public void handlePlacement(boolean isShiftDown, IRecipe<?> recipe, ServerPlayerEntity player) {
        (new ServerRecipePlacerToolCrafting<>(this)).recipeClicked(player, (IRecipe<CraftingInventory>) recipe, isShiftDown);
    }
}
