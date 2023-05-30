package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.registries.BlockRegister;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MachineBlocks {
    // 机器的注册就单独写了
    // 机器结构方块的注册批量处理
    public static final BlockRegister BLOCKS = new BlockRegister();
    public static final Map<Materials, RegistryObject<Block>> machine_casing = new HashMap<>();
    public static final Map<String, RegistryObject<Block>> machines = new HashMap<>();

    public static final RegistryObject<Block> locker = register("locker", LockerBlock::new);
    public static final RegistryObject<Block> machineBlock = register("machine_block", MachineBlockBlock::new);

    private static void init(){
        registerMachineCasing();
    }

    protected static void registerMachineCasing(){
        MaterialBlockData.getData().forEach((material, blockData) -> {
            RegistryObject<Block> casing = BLOCKS.registerNormal(
                    material + "_" + MachineCasingBlock.NAME,
                    () -> new MachineCasingBlock(blockData)
            );
            machine_casing.put(material, casing);
        });
    }

    private static RegistryObject<Block> register(final String name, final Supplier<? extends Block> sup){
        RegistryObject<Block> machine = BLOCKS.registerFunctional(name, sup);
        machines.put(name, machine);
        return machine;
    }
    public static Collection<Block> getAll(){
        return machines.values().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    static{init();}
}
