package com.road.eternalcore.common.inventory.container;

import com.road.eternalcore.common.inventory.SmithingTableInventory;
import com.road.eternalcore.common.inventory.container.slot.CraftingToolSlot;
import com.road.eternalcore.common.inventory.container.slot.NormalResultSlot;
import com.road.eternalcore.common.inventory.container.slot.SmithingTableResultSlot;
import com.road.eternalcore.common.item.crafting.ModRecipeType;
import com.road.eternalcore.common.item.crafting.recipe.SmithingRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;

import java.util.Optional;

public class SmithingTableContainer extends Container {
    protected final CraftResultInventory resultSlot = new CraftResultInventory();
    protected final Inventory byProductSlot = new Inventory(1);
    protected final SmithingTableInventory inputSlots = new SmithingTableInventory(this);
    private final IWorldPosCallable access;
    private final Block accessBlock;
    private final PlayerEntity player;
    private final int playerStartIndex = 5;
    public SmithingTableContainer(int containerId, PlayerInventory inventory) {
        this(0, containerId, inventory, IWorldPosCallable.NULL);
    }
    public SmithingTableContainer(int smithingLevel, int containerId, PlayerInventory inventory, IWorldPosCallable posCallable) {
        super(ModContainerType.smithingTable, containerId);
        this.inputSlots.setSmithingLevel(smithingLevel);
        this.access = posCallable;
        this.accessBlock = posCallable.evaluate((world, blockPos) -> world.getBlockState(blockPos).getBlock(), Blocks.AIR);
        this.player = inventory.player;
        addSlot(new SmithingTableResultSlot(player, inputSlots, resultSlot, byProductSlot, 0, 107, 48));
        addSlot(new NormalResultSlot(byProductSlot, 0, 125, 48));
        addSlot(new CraftingToolSlot(inputSlots, 0, 44, 28));
        for(int i = 1; i < inputSlots.getContainerSize(); i++){
            addSlot(new Slot(inputSlots, i, 35 + (i-1) * 18, 48));
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
    protected static void slotChangedCraftingGrid(int containerId, World world, PlayerEntity player, SmithingTableInventory inputSlots, CraftResultInventory resultSlot) {
        if (!world.isClientSide()) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<SmithingRecipe> recipeOptional = world.getServer().getRecipeManager().getRecipeFor(ModRecipeType.SMITHING, inputSlots, world);
            if (recipeOptional.isPresent()){
                SmithingRecipe recipe = recipeOptional.get();
                if (resultSlot.setRecipeUsed(world, serverplayerentity, recipe)){
                    itemstack = recipe.assemble(inputSlots);
                }
            }
            resultSlot.setItem(0, itemstack);
            serverplayerentity.connection.send(new SSetSlotPacket(containerId, 0, itemstack));
        }
    }

    public void slotsChanged(IInventory inventory) {
        this.access.execute((world, blockPos) -> slotChangedCraftingGrid(this.containerId, world, this.player, this.inputSlots, this.resultSlot));
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
                if (!this.moveItemStackTo(itemStack, playerStartIndex, playerStartIndex + 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack, itemStackCopy);
            } else if (slotId >= playerStartIndex && slotId < playerStartIndex + 36) {
                // 工具放到2号位，其余材料放到3、4号位
                if (!this.moveItemStackTo(itemStack, 2, 3, false)) {
                    if (!this.moveItemStackTo(itemStack, 3, 5, false)) {
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

            ItemStack itemstack2 = slot.onTake(player, itemStack);
            if (slotId == 0) {
                player.drop(itemstack2, false);
            }
        }
        return itemStackCopy;
    }
    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        return slot.container != this.resultSlot && super.canTakeItemForPickAll(itemStack, slot);
    }
}
