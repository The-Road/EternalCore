package com.road.eternalcore.common.inventory.container;

import com.road.eternalcore.common.inventory.container.machine.BatteryBufferContainer;
import com.road.eternalcore.common.inventory.container.machine.MachineBlockContainer;
import net.minecraft.inventory.container.ContainerType;

public class ModContainerType{
    public static final ContainerType<HandcraftAssemblyContainer> handcraftAssembly = ModContainerRegistries.handcraftAssembly.get();
    public static final ContainerType<PartCraftTableContainer> partCraftTable = ModContainerRegistries.partCraftTable.get();
    public static final ContainerType<MachineBlockContainer> machineBlock = ModContainerRegistries.machineBlock.get();
    public static final ContainerType<BatteryBufferContainer> batteryBuffer = ModContainerRegistries.batteryBuffer.get();
}
