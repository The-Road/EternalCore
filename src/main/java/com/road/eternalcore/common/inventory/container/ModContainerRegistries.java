package com.road.eternalcore.common.inventory.container;

import com.road.eternalcore.Utils;
import com.road.eternalcore.common.inventory.container.machine.BatteryBufferContainer;
import com.road.eternalcore.common.inventory.container.machine.MachineBlockContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerRegistries {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Utils.MOD_ID);
    public static final RegistryObject<ContainerType<HandcraftAssemblyContainer>> handcraftAssembly = register(
            "handcraft_assembly", HandcraftAssemblyContainer::new);
    public static final RegistryObject<ContainerType<PartCraftTableContainer>> partCraftTable = register(
            "part_craft_table", PartCraftTableContainer::new);
    public static final RegistryObject<ContainerType<MachineBlockContainer>> machineBlock = register(
            "machine_block", MachineBlockContainer::new);

    public static final RegistryObject<ContainerType<BatteryBufferContainer>> batteryBuffer = register(
            "battery_buffer", BatteryBufferContainer::new);
    private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, ContainerType.IFactory<T> factory){
        return CONTAINERS.register(name, () -> new ContainerType<>(factory));
    }
    public static void register(IEventBus bus){
        CONTAINERS.register(bus);
    }
}
