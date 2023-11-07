package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.registries.BlockRegister;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MachineBlocks {
    // 机器的注册就单独写了
    // 机器结构方块的注册批量处理
    public static final BlockRegister BLOCKS = new BlockRegister();
    public static final Map<Materials, RegistryObject<Block>> machine_casing = new LinkedHashMap<>();
    public static final Map<Materials, RegistryObject<Block>> bricked_casing = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<Block>> machines = new LinkedHashMap<>();
    public static final RegistryObject<Block> alloyFurnace = registerMachine("alloy_furnace", AlloyFurnaceBlock::new);
    public static final RegistryObject<Block> locker = registerMachine("locker", LockerBlock::new);
    public static final RegistryObject<Block> machineBlock = registerMachine("machine_block", MachineBlockBlock::new);
    public static final RegistryObject<Block> batteryBuffer = registerMachine("battery_buffer", BatteryBufferBlock::new);

    private static void init(){
        registerMachineCasing();
        registerBrickedCasing();
    }

    private static void registerMachineCasing(){
        MaterialBlockData.getValidCasingData().forEach(blockData -> {
            Materials material = blockData.getMaterial();
            RegistryObject<Block> casing = BLOCKS.registerNormal(
                    material + "_" + MachineCasingBlock.NAME,
                    () -> new MachineCasingBlock(blockData)
            );
            machine_casing.put(material, casing);
        });
    }
    private static void registerBrickedCasing(){
        MaterialBlockData.getValidBrickedCasingData().forEach(blockData -> {
            Materials material = blockData.getMaterial();
            RegistryObject<Block> brickedCasing = BLOCKS.registerNormal(
                    "bricked_" + material + "_casing",
                    () -> new BrickedCasingBlock(blockData)
            );
            bricked_casing.put(material, brickedCasing);
        });
    }
    private static RegistryObject<Block> registerMachine(final String name, final Supplier<? extends Block> sup){
        RegistryObject<Block> machine = BLOCKS.registerFunctional(name, sup);
        machines.put(name, machine);
        return machine;
    }

    public static Collection<Block> getAll(){
        return machines.values().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
    public static Block getMachineCasing(Materials material){
        if (machine_casing.containsKey(material)){
            return machine_casing.get(material).get();
        }
        // 默认材质为锻铁
        return machine_casing.get(Materials.WROUGHT_IRON).get();
    }
    public static Block getBrickedCasing(Materials material){
        if (bricked_casing.containsKey(material)){
            return bricked_casing.get(material).get();
        } else {
            throw new IllegalArgumentException("Bricked " + material + " casing do not exist");
        }
    }

    static{init();}
}
