package com.road.eternalcore.common.inventory.container;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;

public class WorkbenchContainer extends net.minecraft.inventory.container.WorkbenchContainer {
    // 更改了stillValid方法，使得可以在不同的方块上召唤工作台
    private final Block accessBlock;
    protected IWorldPosCallable access;
    public WorkbenchContainer(int containerId, PlayerInventory inventory) {
        super(containerId, inventory);
        this.accessBlock = null;
    }
    public WorkbenchContainer(int containerId, PlayerInventory inventory, IWorldPosCallable posCallable) {
        super(containerId, inventory, posCallable);
        this.access = posCallable;
        this.accessBlock = posCallable.evaluate((world, pos) -> world.getBlockState(pos).getBlock()).get();
    }
    public boolean stillValid(PlayerEntity player){
        return stillValid(this.access, player, this.accessBlock);
    }
}
