package com.road.eternalcore.common.block.ore;

import com.road.eternalcore.api.ore.OreShape;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.registries.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OreBlocks {
    // 方块的注册
    public static final BlockRegister BLOCKS = new BlockRegister();
    private static final Map<Ores, RegistryObject<Block>> BlockMap = new HashMap<>();
    private static final Map<Ores, Block> VanillaBlockMap = new HashMap<>();

    private static void init(){
        registerVanillaOres();
        registerOres();
    }
    private static void registerVanillaOres(){
        VanillaBlockMap.put(Ores.COAL_ORE, Blocks.COAL_ORE);
        VanillaBlockMap.put(Ores.IRON_ORE, Blocks.IRON_ORE);
        VanillaBlockMap.put(Ores.GOLD_ORE, Blocks.GOLD_ORE);
        VanillaBlockMap.put(Ores.DIAMOND_ORE, Blocks.DIAMOND_ORE);
        VanillaBlockMap.put(Ores.REDSTONE_ORE, Blocks.REDSTONE_ORE);
        VanillaBlockMap.put(Ores.LAPIS_ORE, Blocks.LAPIS_ORE);

    }
    private static void registerOres(){
        for(Ores ore : Ores.getAllOres()){
            if (!VanillaBlockMap.containsKey(ore)) {
                RegistryObject<Block> block = BLOCKS.registerNormal(
                        Ores.getRegisterName(OreShape.ORE, ore),
                        () -> new OreBlock(ore)
                );
                BlockMap.put(ore, block);
            }
        }
    }
    public static Collection<Block> getAllMod(){
        return BlockMap.values().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
    public static Block get(Ores ore){
        if (VanillaBlockMap.containsKey(ore)){
            return VanillaBlockMap.get(ore);
        } else if (BlockMap.containsKey(ore)) {
            return BlockMap.get(ore).get();
        } else {
            throw new IllegalStateException("Nonexistent ore block : " + ore.getName());
        }
    }

    static{init();}
}
