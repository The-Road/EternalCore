package com.road.eternalcore.common.block.ore;

import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.api.ore.OreShape;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.registries.BlockRegister;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OreBlocks {
    // 方块的注册
    public static final BlockRegister BLOCKS = new BlockRegister();
    public static final Map<Ores, RegistryObject<Block>> blockMap = new HashMap<>();

    private static void init(){
        registerOres();
    }
    private static void registerOres(){
        for(Ores ore : Ores.getAllOres()){
            RegistryObject<Block> block = BLOCKS.registerNormal(
                    Ores.getRegisterName(OreShape.ORE, ore),
                    () -> new OreBlock(ore)
            );
            blockMap.put(ore, block);
        }
    }
    public static Collection<Block> getAll(){
        return blockMap.values().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
    public static Block get(Ores ore){
        if (blockMap.containsKey(ore)) {
            return blockMap.get(ore).get();
        }else{
            return null;
        }
    }
    public static Block get(String oreName){
        return get(Ores.get(Materials.get(oreName)));
    }

    static{init();}
}
