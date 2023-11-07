package com.road.eternalcore.common.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AlloyFurnaceTileEntity extends MachineTileEntity implements ITickableTileEntity, IRecipeProcessTileEntity, IFuelBurnTileEntity{
    private int litTime;
    private int litDuration;
    private int workProgress;
    private int workTime;
    private static final ITextComponent TITLE = new TranslationTextComponent("container.eternalcore.alloy_furnace");
    public AlloyFurnaceTileEntity() {
        super(ModTileEntityType.alloyFurnace);
    }

    protected ITextComponent getDefaultName() {
        return TITLE;
    }
    protected Container createMenu(int containerId, PlayerInventory inventory) {
        return null;
    }
    public int getContainerSize() {
        return 4;
    }
    public int getLitTime() {
        return litTime;
    }
    public int getLitDuration() {
        return litDuration;
    }
    public void setLitTime(int litTime) {
        this.litTime = litTime;
    }
    public void setLitDuration(int litDuration) {
        this.litDuration = litDuration;
    }
    public int getWorkProgress() {
        return workProgress;
    }
    public int getWorkTime() {
        return workTime;
    }
    public void setWorkProgress(int workProgress) {
        this.workProgress = workProgress;
    }
    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        saveLitNBT(nbt);
        saveWorkNBT(nbt);
        return nbt;
    }

    public void load(BlockState blockState, CompoundNBT nbt) {
        super.load(blockState, nbt);
        loadLitNbt(nbt);
        loadWorkNbt(nbt);
    }

    public void tick() {
        if (!this.level.isClientSide()){
            tickBurn();
            tickWork();
        }
    }

    public void getWorkResult() {

    }
    public boolean tryNextWork() {
        return false;
    }
    public boolean canBurnNewFuel() {
        return isWorking();
    }
    public void burnNewFuel(){

    }
}
