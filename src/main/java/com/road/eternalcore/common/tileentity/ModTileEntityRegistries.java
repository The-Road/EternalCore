package com.road.eternalcore.common.tileentity;

import com.road.eternalcore.Utils;
import com.road.eternalcore.common.block.machine.MachineBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.function.Supplier;

public class ModTileEntityRegistries {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Utils.MOD_ID);
    public static final RegistryObject<TileEntityType<AlloyFurnaceTileEntity>> alloyFurnace = register(
            "alloy_furnace",
            AlloyFurnaceTileEntity::new,
            MachineBlocks.alloyFurnace
    );
    public static final RegistryObject<TileEntityType<LockerTileEntity>> locker = register(
            "locker",
            LockerTileEntity::new,
            MachineBlocks.locker
    );
    public static final RegistryObject<TileEntityType<MachineBlockTileEntity>> machineBlock = register(
            "machine_block",
            MachineBlockTileEntity::new,
            MachineBlocks.machineBlock
    );
    public static final RegistryObject<TileEntityType<BatteryBufferTileEntity>> batteryBuffer = register(
            "battery_buffer",
            BatteryBufferTileEntity::new,
            MachineBlocks.batteryBuffer
    );

    public static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> supplier, RegistryObject<Block>... blocks){
        return TILE_ENTITY.register(name, () -> TileEntityType.Builder.of(supplier, Arrays.stream(blocks).map(RegistryObject::get).toArray(Block[]::new)).build(null));
    }
    public static void register(IEventBus bus) {
        TILE_ENTITY.register(bus);
    }
}
