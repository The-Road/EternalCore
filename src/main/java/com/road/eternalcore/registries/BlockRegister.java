package com.road.eternalcore.registries;

import com.road.eternalcore.Utils;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockRegister {
    private static final Map<String, RegistryObject<Block>> normalBlocks = new HashMap<>();
    private static final Map<String, RegistryObject<Block>> functionalBlocks = new HashMap<>();
    private final DeferredRegister<Block> blockRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, Utils.MOD_ID);
    public RegistryObject<Block> registerNormal(final String name, final Supplier<? extends Block> sup){
        RegistryObject<Block> block = blockRegister.register(name, sup);
        BlockRegister.normalBlocks.put(name, block);
        return block;
    }
    public RegistryObject<Block> registerFunctional(final String name, final Supplier<? extends Block> sup){
        RegistryObject<Block> block = blockRegister.register(name, sup);
        BlockRegister.functionalBlocks.put(name, block);
        return block;
    }
    public void register(IEventBus bus){
        blockRegister.register(bus);
    }
    public static Map<String, RegistryObject<Block>> getNormalBlocks(){
        return normalBlocks;
    }
    public static Map<String, RegistryObject<Block>> getFunctionalBlocks(){
        return functionalBlocks;
    }
}
