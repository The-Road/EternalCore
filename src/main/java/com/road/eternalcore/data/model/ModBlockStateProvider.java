package com.road.eternalcore.data.model;

import com.road.eternalcore.Utils;
import com.road.eternalcore.api.block.ModBlockStateProperties;
import com.road.eternalcore.api.block.properties.PipeConnection;
import com.road.eternalcore.api.material.MaterialWireData;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.common.block.machine.MachineBlocks;
import com.road.eternalcore.common.block.ore.OreBlocks;
import com.road.eternalcore.common.block.pipe.AbstractPipeBlock;
import com.road.eternalcore.common.block.pipe.PipeBlocks;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public class ModBlockStateProvider extends BlockStateProvider{
    protected final ModBlockModelProvider blockModels;
    protected final ModItemModelProvider itemModels;

    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Utils.MOD_ID, exFileHelper);
        this.blockModels = new ModBlockModelProvider(gen, exFileHelper);
        this.itemModels = new ModItemModelProvider(gen, exFileHelper);
    }
    public BlockModelProvider models() {
        return blockModels;
    }
    public ItemModelProvider itemModels() {
        return itemModels;
    }

    @Override
    protected void registerStatesAndModels() {
        blockModels.registerModels();
        itemModels.registerModels();
        addOres();
        addPipesAndWires();
        addMachines();
    }
    protected ResourceLocation mcBlock(String name){
        return mcLoc(BLOCK_FOLDER + "/" + name);
    }
    protected ResourceLocation modBlock(String name){
        return modLoc(BLOCK_FOLDER + "/" + name);
    }

    protected boolean hasModel(Block block){
        return blockModels.generatedModels.containsKey(block.getRegistryName());
    }
    protected ModelFile getModel(Block block){
        return blockModels.generatedModels.get(block.getRegistryName());
    }
    protected ModelFile getModel(Block block, String extra){
        return blockModels.generatedModels.get(new ResourceLocation(block.getRegistryName() + "_" + extra));
    }
    protected ModelFile getModel(ResourceLocation resourceLocation){
        return blockModels.generatedModels.get(resourceLocation);
    }

    private void addOres(){
        for(Ores ore : Ores.getAllOres()){
            Block block = OreBlocks.get(ore);
            if (hasModel(block)) {
                simpleBlock(block, getModel(block));
            }
        }
    }
    private void addPipesAndWires(){
        for (MaterialWireData.WireType wireType : MaterialWireData.WireType.values()){
            PipeBlocks.getWires(wireType).forEach(wire -> {
                if (hasModel(wire)) {
                    if (wireType.radius < 8){
                        addWire(wire, "wire", wireType.radius);
                    } else {
                        simpleBlock(wire, getModel(wire));
                    }
                }
            });
        }
    }
    private void addWire(Block block, String wireName, int radius){
        ModelFile core = getModel(new ModResourceLocation(wireName + "_core_" + radius));
        ModelFile side = getModel(new ModResourceLocation(wireName + "_side_" + radius));
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block)
                .part().modelFile(core).addModel().end();
        AbstractPipeBlock.DIRECTION_CONNECTION.forEach((dir, property) -> {
            builder.part().modelFile(side)
                    .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .addModel()
                    .condition(property, PipeConnection.ON)
                    .end();
        });
    }
    private void addMachines(){
        addMachineCasing();
        // 其他机器的方法，待添加
        addHorizontalOpenMachine(MachineBlocks.locker.get());
        addDirectionalMachine(MachineBlocks.machineBlock.get());
        addDirectionalMachine(MachineBlocks.batteryBuffer.get());
    }
    private void addMachineCasing(){
        MachineBlocks.machine_casing.forEach((material, blockRegistryObject) -> {
            Block casing = blockRegistryObject.get();
            if (hasModel(casing)){
                simpleBlock(casing, getModel(casing));
            }
        });
    }
    protected void addHorizontalMachine(Block block){
        if (hasModel(block)) {
            getVariantBuilder(block).forAllStatesExcept(state -> ConfiguredModel.builder()
                            .modelFile(getModel(block))
                            .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                            .build(),
                    ModBlockStateProperties.MATERIAL
            );
        }
    }
    protected void addHorizontalOpenMachine(Block block){
        if (hasModel(block)) {
            getVariantBuilder(block).forAllStatesExcept(state -> ConfiguredModel.builder()
                            .modelFile(state.getValue(BlockStateProperties.OPEN) ? getModel(block, "open") : getModel(block))
                            .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                            .build(),
                    ModBlockStateProperties.MATERIAL
            );
        }
    }
    protected void addDirectionalMachine(Block block){
        if (hasModel(block)) {
            getVariantBuilder(block).forAllStatesExcept(state -> {
                Direction dir = state.getValue(BlockStateProperties.FACING);
                return ConfiguredModel.builder()
                        .modelFile(getModel(block))
                        .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                        .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                        .build();
                }, ModBlockStateProperties.MATERIAL
            );
        }
    }
}