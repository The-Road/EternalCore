package com.road.eternalcore.common.inventory.container;

import com.road.eternalcore.Utils;
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
    public static final RegistryObject<ContainerType<SmithingTableContainer>> smithingTable = register(
            "smithing_table", SmithingTableContainer::new);
    public static final RegistryObject<ContainerType<MachineBlockContainer>> machineBlock = register(
            "machine_block", MachineBlockContainer::new);
    private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, ContainerType.IFactory<T> factory){
        return CONTAINERS.register(name, () -> new ContainerType<>(factory));
    }
    public static void register(IEventBus bus){
        CONTAINERS.register(bus);
    }
}
