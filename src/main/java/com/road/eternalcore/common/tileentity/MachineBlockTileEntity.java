package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.common.inventory.container.machine.MachineBlockContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MachineBlockTileEntity extends EnergyMachineTileEntity implements IEnergyProviderTileEntity, IEnergyReceiverTileEntity{
    private static final ITextComponent TITLE = new TranslationTextComponent("container.eternalcore.machine_block");
    public MachineBlockTileEntity(){
        super(ModTileEntityType.machineBlock, 1, 0, 1);
    }

    protected ITextComponent getDefaultName(){
        return TITLE;
    }
    protected Container createMenu(int containerId, PlayerInventory playerInventory) {
        return new MachineBlockContainer(containerId, playerInventory, this, this.guiData);
    }
    public boolean canExtract(Direction side) {
        return covers.getFace() == side;
    }
    public boolean canReceive(Direction side) {
        return covers.getFace() != side;
    }
    public int maxProvideCurrent() {
        return 1;
    }
    public int maxReceiveCurrent() {
        return 1;
    }
}
