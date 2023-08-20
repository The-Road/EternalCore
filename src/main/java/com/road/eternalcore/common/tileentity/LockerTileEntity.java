package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.common.block.machine.MachineBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class LockerTileEntity extends MachineTileEntity{
    private static final ITextComponent TITLE = new TranslationTextComponent("container.eternalcore.locker");
    private static final int ContainerSize = 54;
    protected int openCount = 0;
    public LockerTileEntity(){
        super(ModTileEntityType.locker);
    }
    // IInventory接口
    public int getContainerSize(){
        return ContainerSize;
    }
    protected ITextComponent getDefaultName(){
        return TITLE;
    }
    protected Container createMenu(int containerId, PlayerInventory playerInventory) {
        return ChestContainer.sixRows(containerId, playerInventory, this);
    }
    // 剩下的代码都是检测开箱状态用的
    public void startOpen(PlayerEntity player) {
        if (!player.isSpectator()){
            // 开箱人数+1
            if (this.openCount < 0){
                this.openCount = 0;
            }
            this.openCount++;
            BlockState blockState = getBlockState();
            if (!blockState.getValue(BlockStateProperties.OPEN)){
                playSound(blockState, SoundEvents.BARREL_OPEN);
                updateOpenState(blockState, true);
            }
            // 开始检查开箱状态
            scheduleRecheck();
        }
    }
    public void stopOpen(PlayerEntity player) {
        if (!player.isSpectator()) {
            // 开箱人数-1
            this.openCount--;
        }
    }
    // 检查开启状态，确保无人开箱时正常关闭（比如玩家开箱子时掉线之类的）
    public void recheckOpenState() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        this.openCount = getOpenCount(this.level, this, i, j, k);
        if (this.openCount > 0) {
            scheduleRecheck();
        } else {
            BlockState blockstate = getBlockState();
            if (!blockstate.is(MachineBlocks.locker.get())) {
                setRemoved();
                return;
            }
            if (blockstate.getValue(BlockStateProperties.OPEN)) {
                playSound(blockstate, SoundEvents.BARREL_CLOSE);
                updateOpenState(blockstate, false);
            }
        }
    }
    protected void scheduleRecheck(){
        // 检查的间隔时间
        int tick = 5;
        this.level.getBlockTicks().scheduleTick(getBlockPos(), getBlockState().getBlock(), tick);
    }

    // 从正面播放声音
    private void playSound(BlockState blockState, SoundEvent soundEvent) {
        Vector3i vector3i = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getNormal();
        double d0 = (double)this.worldPosition.getX() + 0.5D + (double)vector3i.getX() / 2.0D;
        double d1 = (double)this.worldPosition.getY() + 0.5D + (double)vector3i.getY() / 2.0D;
        double d2 = (double)this.worldPosition.getZ() + 0.5D + (double)vector3i.getZ() / 2.0D;
        this.level.playSound(null, d0, d1, d2, soundEvent, SoundCategory.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }
    private void updateOpenState(BlockState blockState, boolean open) {
        this.level.setBlock(getBlockPos(), blockState.setValue(BlockStateProperties.OPEN, open), 3);
    }
    // ChestTileEntity还有一个getOpenCount，是服务器每隔一段时间刷新箱子状态用的
    // 这里的代码是模仿Barrel写的，用scheduleRecheck在Block里刷新开关状态
    // Chest用了ITickableTileEntity接口每帧刷新，因为有开箱关箱的动画
    // 怪不得说箱子放多了卡

    // 检测正在开箱的人数
    public static int getOpenCount(World world, LockerTileEntity tileEntity, int x, int y, int z){
        int count = 0;
        float d = 5.0F;
        // 检查坐标差5格以内的玩家
        for(PlayerEntity player : world.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(x-d, y-d, z-d, x+1+d, y+1+d, z+1+d))){
            if (player.containerMenu instanceof ChestContainer){
                IInventory iInventory = ((ChestContainer) player.containerMenu).getContainer();
                if (iInventory == tileEntity){
                    count++;
                }
            }
        }
        return count;
    }
}
