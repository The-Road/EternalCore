package com.road.eternalcore.common.inventory.container;

import com.road.eternalcore.common.inventory.SmithingTableInventory;
import com.road.eternalcore.common.inventory.container.slot.CraftingToolSlot;
import com.road.eternalcore.common.inventory.container.slot.SmithingTableResultSlot;
import com.road.eternalcore.common.item.crafting.IModRecipeType;
import com.road.eternalcore.common.item.crafting.recipe.SmithingRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;

import java.util.Optional;

public class SmithingTableContainer extends Container {
    protected final CraftResultInventory resultSlots = new CraftResultInventory();
    protected final SmithingTableInventory inputSlots = new SmithingTableInventory(this);
    private final IWorldPosCallable access;
    private final Block accessBlock;
    private final PlayerEntity player;
    private final int craftSlotsNum = 4;
    public SmithingTableContainer(int containerId, PlayerInventory inventory) {
        this(0, containerId, inventory, IWorldPosCallable.NULL);
    }
    public SmithingTableContainer(int smithingLevel, int containerId, PlayerInventory inventory, IWorldPosCallable posCallable) {
        super(ModContainerType.smithingTable, containerId);
        this.inputSlots.setSmithingLevel(smithingLevel);
        this.access = posCallable;
        this.accessBlock = posCallable.evaluate((world, blockPos) -> world.getBlockState(blockPos).getBlock(), Blocks.AIR);
        this.player = inventory.player;
        addSlot(new SmithingTableResultSlot(player, inputSlots, resultSlots, 0, 134, 47));
        addSlot(new CraftingToolSlot(inputSlots, 0, 27, 47));
        for(int i = 1; i < inputSlots.getContainerSize(); i++){
            addSlot(new Slot(inputSlots, i, 40 + i * 18, 47));
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
    protected static void slotChangedCraftingGrid(int containerId, World world, PlayerEntity player, SmithingTableInventory inputSlots, CraftResultInventory resultSlots) {
        if (!world.isClientSide()) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<SmithingRecipe> recipeOptional = world.getServer().getRecipeManager().getRecipeFor(IModRecipeType.SMITHING, inputSlots, world);
            if (recipeOptional.isPresent()){
                SmithingRecipe recipe = recipeOptional.get();
                if (resultSlots.setRecipeUsed(world, serverplayerentity, recipe)){
                    itemstack = recipe.assemble(inputSlots);
                }
            }
            resultSlots.setItem(0, itemstack);
            serverplayerentity.connection.send(new SSetSlotPacket(containerId, 0, itemstack));
        }
    }

    public void slotsChanged(IInventory inventory) {
        this.access.execute((world, blockPos) -> slotChangedCraftingGrid(this.containerId, world, this.player, this.inputSlots, this.resultSlots));
    }

    public boolean stillValid(PlayerEntity player) {
        return stillValid(this.access, player, this.accessBlock);
    }
    public void removed(PlayerEntity player) {
        super.removed(player);
        this.access.execute((world, blockPos) -> {
            this.clearContainer(player, world, this.inputSlots);
        });
    }

    public ItemStack quickMoveStack(PlayerEntity player, int slotId){
        ItemStack itemStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot != null && slot.hasItem()){
            ItemStack itemStack = slot.getItem();
            itemStackCopy = itemStack.copy();
            if (slotId == 0){
                this.access.execute((world, blockPos) -> {
                    itemStack.getItem().onCraftedBy(itemStack, world, player);
                });
                if (!this.moveItemStackTo(itemStack, craftSlotsNum, craftSlotsNum + 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack, itemStackCopy);
            } else if (slotId >= craftSlotsNum && slotId < craftSlotsNum + 36) {
                // 工具放到1号位，其余材料放到2、3号位
                if (!this.moveItemStackTo(itemStack, 1, 2, false)) {
                    if (!this.moveItemStackTo(itemStack, 2, 4, false)) {
                        if (slotId < craftSlotsNum + 27) {
                            if (!this.moveItemStackTo(itemStack, craftSlotsNum + 27, craftSlotsNum + 36, false)) {
                                return ItemStack.EMPTY;
                            }
                        } else if (!this.moveItemStackTo(itemStack, craftSlotsNum, craftSlotsNum + 27, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            } else if (!this.moveItemStackTo(itemStack, craftSlotsNum, craftSlotsNum + 36, false)) {
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

            ItemStack itemstack2 = slot.onTake(player, itemStack);
            if (slotId == 0) {
                player.drop(itemstack2, false);
            }
        }
        return itemStackCopy;
    }
    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(itemStack, slot);
    }
}
