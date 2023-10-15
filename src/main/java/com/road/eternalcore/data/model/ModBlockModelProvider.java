package com.road.eternalcore.data.model;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.road.eternalcore.Utils;
import com.road.eternalcore.api.material.MaterialWireData;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.client.renderer.model.builder.MachineModelBuilder;
import com.road.eternalcore.common.block.ModBlocks;
import com.road.eternalcore.common.block.machine.MachineBlocks;
import com.road.eternalcore.common.block.ore.OreBlocks;
import com.road.eternalcore.common.block.pipe.PipeBlocks;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModBlockModelProvider extends BlockModelProvider{
    private final JsonObject missingTexture = new JsonObject();

    public ModBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Utils.MOD_ID, existingFileHelper);
    }
    protected ResourceLocation mcBlock(String name){
        return mcLoc(BLOCK_FOLDER + "/" + name);
    }
    protected ResourceLocation modBlock(String name){
        return modLoc(BLOCK_FOLDER + "/" + name);
    }
    // 机器的材质（统一的）
    protected ResourceLocation machineSide(String name){
        return modLoc(BLOCK_FOLDER + "/machine/side/" + name);
    }
    protected ResourceLocation machineFace(String name){
        return modLoc(BLOCK_FOLDER + "/machine/face/" + name);
    }

    @Override
    protected void registerModels() {
        addOres();
        addPipesAndWires();
        addMachines();
        addCube(ModBlocks.handcraftAssemblyTable,
                mcBlock("oak_planks"),
                mcBlock("enchanting_table_top"),
                mcBlock("crafting_table_front"),
                mcBlock("crafting_table_side"),
                mcBlock("crafting_table_side"),
                mcBlock("crafting_table_front"),
                mcBlock("crafting_table_front")
        );
        addCube(ModBlocks.partCraftTable,
                mcBlock("oak_planks"),
                mcBlock("enchanting_table_top"),
                mcBlock("smithing_table_front"),
                mcBlock("smithing_table_side"),
                mcBlock("smithing_table_front"),
                mcBlock("smithing_table_side"),
                mcBlock("smithing_table_front")
        );
        saveMissingTextureData();
    }
    private void saveMissingTextureData() {
        // 调试用，用于输出没添加的材质
        try {
            Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
            HashFunction SHA1 = Hashing.sha1();
            DirectoryCache directoryCache = new DirectoryCache(this.generator.getOutputFolder(), "cache");
            Path path = this.generator.getOutputFolder().resolve("assets/eternalcore/blockMissingTexture.json");
            String s = GSON.toJson(this.missingTexture);
            String s1 = SHA1.hashUnencodedChars(s).toString();
            Files.createDirectories(path.getParent());
            try (BufferedWriter bufferedwriter = Files.newBufferedWriter(path)) {
                bufferedwriter.write(s);
            }
            directoryCache.putNew(path, s1);
        } catch (IOException ioexception) {
            LogManager.getLogger().error("Couldn't save blockMissingTexture json", ioexception);
        }
    }

    private void addOres(){
        for(Ores ore : Ores.getAllOres()){
            ResourceLocation texture = modBlock("ore/" + ore.getName());
            if (existingFileHelper.exists(texture, TEXTURE)) {
                addCubeAll(OreBlocks.get(ore), texture);
            }else{
                if (missingTexture.get("ore") == null){
                    missingTexture.add("ore", new JsonArray());
                }
                missingTexture.getAsJsonArray("ore").add(ore.getName());
            }
        }
    }
    private void addPipesAndWires(){
        for (MaterialWireData.WireType wireType : MaterialWireData.WireType.values()){
            ResourceLocation texture = modBlock("wire/wire");
            addWireModel("wire", wireType.radius, texture);
            PipeBlocks.getWires(wireType).forEach(wire -> addWireInventory(wire, wireType, texture));
        }
    }
    private void addMachines(){
        addMachineCasing();
        addOrientableMachine(MachineBlocks.locker.get(), "locker");
        addOrientableMachineOpen(MachineBlocks.locker.get(), "locker_open");
        addCubeTopMachine(MachineBlocks.machineBlock.get(), "machine_block");
        addCubeTopMachine(MachineBlocks.batteryBuffer.get(), "battery_buffer");
    }
    private void addMachineCasing(){
        MachineBlocks.machine_casing.forEach((material, blockRegistryObject) -> {
            Block casing = blockRegistryObject.get();
            ResourceLocation texture = modBlock("machine/casing/" + material);
            if (existingFileHelper.exists(texture, TEXTURE)){
                addCubeAll(casing, texture);
            }else{
                if (missingTexture.get("machine") == null){
                    missingTexture.add("machine", new JsonObject());
                }
                JsonObject missingMachine = missingTexture.getAsJsonObject("machine");
                if (missingMachine.get("casing") == null){
                    missingMachine.add("casing", new JsonArray());
                }
                missingMachine.getAsJsonArray("casing").add(material.getName());
            }
        });
    }

    protected void addCube(Block block, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west){
        generatedModels.put(block.getRegistryName(), cube(
                ModBlocks.getBlockName(block),
                down, up, north, south, east, west
        ));
    }
    protected void addCube(Block block, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west, ResourceLocation particle){
        generatedModels.put(block.getRegistryName(), cube(
                ModBlocks.getBlockName(block),
                down, up, north, south, east, west
        ).texture("particle", particle));
    }
    protected void addCubeAll(Block block, ResourceLocation texture){
        generatedModels.put(block.getRegistryName(), cubeAll(ModBlocks.getBlockName(block), texture));
    }
    // 添加管道类材质
    protected void addWireModel(String name, int radius, ResourceLocation texture){
        if (radius < 1 || radius > 7) return;
        String coreName = name + "_core_" + radius;
        String sideName = name + "_side_" + radius;
        ResourceLocation coreRL = new ModResourceLocation(coreName);
        ResourceLocation sideRL = new ModResourceLocation(sideName);
        generatedModels.put(coreRL, withExistingParent(
                coreName, modLoc(BLOCK_FOLDER + "/template_pipe_core_" + radius))
                .texture("cover", texture)
        );
        generatedModels.put(sideRL, withExistingParent(
                sideName, modLoc(BLOCK_FOLDER + "/template_pipe_side_" + radius))
                .texture("cover", texture)
        );
    }
    protected void addWireInventory(Block block, MaterialWireData.WireType wireType, ResourceLocation texture){
        if (wireType.radius >= 1 && wireType.radius <= 7) {
            String coreName = "wire_core_" + wireType.radius;
            generatedModels.put(block.getRegistryName(), withExistingParent(
                    ModBlocks.getBlockName(block), modLoc(BLOCK_FOLDER + "/" + coreName))
            );
        } else {
            generatedModels.put(block.getRegistryName(), cubeAll(ModBlocks.getBlockName(block), texture));
        }
    }
    // 有朝向的机器，提供正面材质和侧面材质（注意该材质是贴在机器外壳材质上面一层的）
    protected void addCubeTopMachine(Block block, String faceName) {
        addCubeTopMachine(block, faceName, "empty");
    }
    protected void addCubeTopMachine(Block block, String faceName, String sideName){
        ResourceLocation side = machineSide(sideName);
        generatedModels.put(block.getRegistryName(), withExistingParent(
                ModBlocks.getBlockName(block), BLOCK_FOLDER + "/block")
                .customLoader(MachineModelBuilder::begin)
                .submodel(RenderType.cutoutMipped(), cubeTop(
                        "multilayer_" +ModBlocks.getBlockName(block),
                        side, machineFace(faceName)
                )).end()
        );
    }
    protected void addOrientableMachine(Block block, String faceName) {
        addOrientableMachine(block, faceName, "empty");
    }
    protected void addOrientableMachine(Block block, String faceName, String sideName){
        ResourceLocation side = machineSide(sideName);
        generatedModels.put(block.getRegistryName(), withExistingParent(
                ModBlocks.getBlockName(block), BLOCK_FOLDER + "/block")
                .customLoader(MachineModelBuilder::begin)
                .submodel(RenderType.cutoutMipped(), orientable(
                        "multilayer_" +ModBlocks.getBlockName(block),
                        side, machineFace(faceName), side
                )).end()
        );
    }
    protected void addOrientableMachineOpen(Block block, String faceName) {
        addOrientableMachineOpen(block, faceName, "empty");
    }
    protected void addOrientableMachineOpen(Block block, String faceName, String sideName){
        ResourceLocation side = machineSide(sideName);
        generatedModels.put(new ResourceLocation(block.getRegistryName() + "_open"),
                withExistingParent(
                ModBlocks.getBlockName(block) + "_open", BLOCK_FOLDER + "/block")
                .customLoader(MachineModelBuilder::begin)
                .submodel(RenderType.cutoutMipped(), orientable(
                        "multilayer_" + ModBlocks.getBlockName(block) + "_open",
                        side, machineFace(faceName), side
                )).end()
        );
    }

}
