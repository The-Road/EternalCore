package com.road.eternalcore.common.block;

import com.road.eternalcore.registries.BlockRegister;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;
import java.util.stream.Collectors;

public class ModBlocks {
    // 矿石
    public static final Block handcraftAssemblyTable = ModBlockRegistries.handcraftAssemblyTable.get();
    public static final Block partCraftTable = ModBlockRegistries.partCraftTable.get();
    public static Collection<Block> getNormalBlocks(){
        return BlockRegister.getNormalBlocks().values().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
    public static Collection<Block> getFunctionalBlocks(){
        return BlockRegister.getFunctionalBlocks().values().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
    public static Collection<Block> getAll(){
        Collection<Block> list = getNormalBlocks();
        list.addAll(getFunctionalBlocks());
        return list;
    }
    public static Block get(String name){
        RegistryObject<Block> block = BlockRegister.getNormalBlocks().get(name);
        if (block == null){
            block = BlockRegister.getFunctionalBlocks().get(name);
        }
        if (block != null){
            return block.get();
        } else {
            return null;
        }
    }
    public static String getBlockName(Block block){
        return block.getRegistryName().getPath();
    }
}
