package com.road.eternalcore.common.block.pipe;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.road.eternalcore.api.material.MaterialWireData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.registries.BlockRegister;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PipeBlocks {
    public static final BlockRegister BLOCKS = new BlockRegister();
    public static final Table<Materials, MaterialWireData.WireType, RegistryObject<Block>> wireTable = HashBasedTable.create();
    public static final Map<String, RegistryObject<Block>> wires = new LinkedHashMap<>();

    private static void init(){
        registerWires();
    }

    private static void registerWires(){
        for (MaterialWireData.WireType wireType : MaterialWireData.WireType.values()){
           MaterialWireData.getData().forEach((material, materialWireData) -> {
               if (materialWireData.getWireTypes().contains(wireType)) {
                   String wireName = MaterialWireData.getRegisterName(material, wireType);
                   RegistryObject<Block> wire = BLOCKS.registerNormal(
                           wireName, () -> new WireBlock(materialWireData, wireType)
                   );
                   wireTable.put(material, wireType, wire);
                   wires.put(wireName, wire);
               }
           });
        }
    }

    public static Collection<Block> getWires(){
        return wireTable.values().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
    public static Collection<Block> getWires(Materials material){
        return wireTable.row(material).values().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
    public static Collection<Block> getWires(MaterialWireData.WireType wireType){
        return wireTable.column(wireType).values().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    static{init();}
}
