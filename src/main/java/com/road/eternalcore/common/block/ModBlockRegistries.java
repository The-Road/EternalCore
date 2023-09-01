package com.road.eternalcore.common.block;

import com.road.eternalcore.common.block.machine.MachineBlocks;
import com.road.eternalcore.common.block.ore.OreBlocks;
import com.road.eternalcore.common.block.pipe.PipeBlocks;
import com.road.eternalcore.registries.BlockRegister;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;

public class ModBlockRegistries {
    public static final BlockRegister BLOCKS = new BlockRegister();
    public static final RegistryObject<Block> handcraftAssemblyTable = BLOCKS.registerFunctional(
            "handcraft_assembly_table", () -> new HandcraftAssemblyTableBlock(
                    AbstractBlock.Properties.of(Material.WOOD)
                            .strength(2.5F)
                            .sound(SoundType.WOOD)
            ));
    public static final RegistryObject<Block> smithingTable = BLOCKS.registerFunctional(
            "smithing_table", () -> new SmithingTableBlock(
                    AbstractBlock.Properties.of(Material.METAL)
                            .strength(2.5F)
                            .sound(SoundType.METAL)
            ));

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        OreBlocks.BLOCKS.register(bus);
        PipeBlocks.BLOCKS.register(bus);
        MachineBlocks.BLOCKS.register(bus);
    }
}
