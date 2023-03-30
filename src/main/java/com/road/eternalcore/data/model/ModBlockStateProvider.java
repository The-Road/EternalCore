package com.road.eternalcore.data.model;

import com.road.eternalcore.Utils;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.common.block.machine.MachineBlocks;
import com.road.eternalcore.common.block.ore.OreBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
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
        addMachines();
    }
    protected ResourceLocation mcBlock(String name){
        return mcLoc(BLOCK_FOLDER + "/" + name);
    }
    protected ResourceLocation modBlock(String name){
        return modLoc(BLOCK_FOLDER + "/" + name);
    }

    private void addOres(){
        for(Ores ore : Ores.getAllOres()){
            Block block = OreBlocks.get(ore);
            simpleBlock(block, blockModels.generatedModels.get(block.getRegistryName()));
        }
    }
    private void addMachines(){
        addMachineCasing();
    }
    private void addMachineCasing(){
        MachineBlocks.machine_casing.forEach((material, blockRegistryObject) -> {
            Block casing = blockRegistryObject.get();
            if (blockModels.generatedModels.containsKey(casing.getRegistryName())){
                simpleBlock(casing, blockModels.generatedModels.get(casing.getRegistryName()));
            }
        });
    }
}