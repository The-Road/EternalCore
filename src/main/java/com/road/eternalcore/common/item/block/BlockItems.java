package com.road.eternalcore.common.item.block;

import com.road.eternalcore.common.block.ModBlocks;
import com.road.eternalcore.common.block.machine.MachineBlocks;
import com.road.eternalcore.common.block.pipe.PipeBlocks;
import com.road.eternalcore.common.item.group.ModGroup;
import com.road.eternalcore.registries.BlockRegister;
import com.road.eternalcore.registries.ItemRegister;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BlockItems {
    public static final ItemRegister ITEMS = new ItemRegister();
    private static final Map<String, RegistryObject<Item>> blockItems = new HashMap<>();
    private static void init(){
        // 注册普通方块
        PipeBlocks.wires.forEach((name, wire) -> registerNormalBlock(
                name, () -> new WireBlockItem(wire.get(), new Item.Properties().tab(ModGroup.blockGroup))));
        BlockRegister.getNormalBlocks().forEach((name, block) -> registerNormalBlock(
                name, () -> new ModBlockItem(block.get(), new Item.Properties().tab(ModGroup.blockGroup))));
        // 注册功能型方块
        MachineBlocks.machines.forEach((name, machine) -> registerFunctionalBlock(
                name, () -> new MachineBlockItem(machine.get(), new Item.Properties().tab(ModGroup.functionalBlockGroup))));
        BlockRegister.getFunctionalBlocks().forEach((name, block) -> registerFunctionalBlock(
                name, () -> new ModBlockItem(block.get(), new Item.Properties().tab(ModGroup.functionalBlockGroup))));
    }
    private static void registerNormalBlock(String name, Supplier<? extends Item> sup){
        if (blockItems.containsKey(name)){
            return;
        }
        RegistryObject<Item> item = ITEMS.register(name, sup);
        blockItems.put(name, item);
    }
    private static void registerFunctionalBlock(String name, Supplier<? extends Item> sup){
        if (blockItems.containsKey(name)){
            return;
        }
        RegistryObject<Item> item = ITEMS.register(name, sup);
        blockItems.put(name, item);
    }

    public static Collection<Item> getAll(){
        return blockItems.values().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
    public static Item get(String blockName){
        if (blockItems.containsKey(blockName)){
            return blockItems.get(blockName).get();
        }else{
            return null;
        }
    }
    public static Item get(Block block){
        return get(ModBlocks.getBlockName(block));
    }

    static{init();}
}
