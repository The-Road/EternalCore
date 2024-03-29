package com.road.eternalcore.common.tileentity;

import net.minecraft.tileentity.TileEntityType;

public class ModTileEntityType {
    public static final TileEntityType<AlloyFurnaceTileEntity> alloyFurnace = ModTileEntityRegistries.alloyFurnace.get();
    public static final TileEntityType<LockerTileEntity> locker = ModTileEntityRegistries.locker.get();
    public static final TileEntityType<MachineBlockTileEntity> machineBlock = ModTileEntityRegistries.machineBlock.get();
    public static final TileEntityType<BatteryBufferTileEntity> batteryBuffer = ModTileEntityRegistries.batteryBuffer.get();
}
