package com.road.eternalcore.common.inventory.container.slot;

import com.road.eternalcore.common.inventory.ToolCraftingInventory;
import com.road.eternalcore.common.item.crafting.ModRecipeType;
import com.road.eternalcore.common.item.tool.CustomTierItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.function.Consumer;

public class ToolCraftingResultSlot extends CraftingResultSlot {
    private final ToolCraftingInventory craftAndToolSlots;
    private PlayerEntity player;

    public ToolCraftingResultSlot(PlayerEntity player, ToolCraftingInventory craftAndToolSlots, CraftResultInventory container, int index, int posX, int posY) {
        super(player, craftAndToolSlots.getCraftSlots(), container, index, posX, posY);
        this.player = player;
        this.craftAndToolSlots = craftAndToolSlots;
    }

    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack itemStack) {
        // 这个方法在客户端和服务端都会调用一次，而判断合成结果的方式只在服务端调用
        // 所以只能通过toolSlots.isEmpty()来判断是否使用工具了
        CraftingInventory toolSlots = craftAndToolSlots.getToolSlots();
        if(toolSlots.isEmpty()) {
            return super.onTake(player, itemStack);
        }else{
            this.checkTakeAchievements(itemStack);
            // 据我观察，这个setCraftingPlayer是用来判断合成过程中是否有工具损坏的（但是好像没调用过）
            ForgeHooks.setCraftingPlayer(player);
            NonNullList<ItemStack> remainItemList = player.level.getRecipeManager().getRemainingItemsFor(ModRecipeType.TOOL_CRAFTING, this.craftAndToolSlots, player.level);
            ForgeHooks.setCraftingPlayer(null);
            // 合成材料采用相同的逻辑消耗
            Consumer<Integer> normalRemainingItem = i -> {
                ItemStack originalItem = craftAndToolSlots.getItem(i);
                ItemStack remainItem = remainItemList.get(i);
                if (!originalItem.isEmpty()) {
                    craftAndToolSlots.removeItem(i, 1);
                    originalItem = craftAndToolSlots.getItem(i);
                }
                if (!remainItem.isEmpty()) {
                    if (originalItem.isEmpty()) {
                        craftAndToolSlots.setItem(i, remainItem);
                    } else if (ItemStack.isSame(originalItem, remainItem) && ItemStack.tagMatches(originalItem, remainItem)) {
                        remainItem.grow(originalItem.getCount());
                        craftAndToolSlots.setItem(i, remainItem);
                    } else if (!this.player.inventory.add(remainItem)) {
                        this.player.drop(remainItem, false);
                    }
                }
            };
            CraftingInventory craftSlots = craftAndToolSlots.getCraftSlots();
            for(int i = 0; i < craftSlots.getContainerSize(); ++i) {
                normalRemainingItem.accept(i);
            }
            // 用于合成的工具根据配方消耗耐久
            for(int i = 0; i < toolSlots.getContainerSize(); i++){
                if (toolSlots.getItem(i).isDamageableItem()) {
                    ItemStack remainToolItem = remainItemList.get(i + craftSlots.getContainerSize());
                    if (!remainToolItem.isEmpty()) {
                        // 注意hurtAndBreak在创造模式下是不掉耐久的
                        remainToolItem.hurtAndBreak(
                                CustomTierItem.addItemDamage(remainToolItem, 0),
                                player,
                                (playerEntity) -> ForgeEventFactory.onPlayerDestroyItem(playerEntity, remainToolItem, null)
                        );
                        toolSlots.setItem(i, remainToolItem);
                    }
                }else{
                    normalRemainingItem.accept(i + craftSlots.getContainerSize());
                }
            }
            return itemStack;
        }
    }
}
