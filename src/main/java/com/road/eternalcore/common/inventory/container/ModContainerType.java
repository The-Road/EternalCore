package com.road.eternalcore.common.inventory.container;

import net.minecraft.inventory.container.ContainerType;

public class ModContainerType{
    public static final ContainerType<HandcraftAssemblyContainer> handcraftAssembly = ModContainerRegistries.handcraftAssembly.get();
    public static final ContainerType<SmithingTableContainer> smithingTable = ModContainerRegistries.smithingTable.get();
    public static final ContainerType<MachineBlockContainer> machineBlock = ModContainerRegistries.machineBlock.get();
}
