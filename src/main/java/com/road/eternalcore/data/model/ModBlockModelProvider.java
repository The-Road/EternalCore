package com.road.eternalcore.data.model;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.road.eternalcore.Utils;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.common.block.ModBlocks;
import com.road.eternalcore.common.block.machine.MachineBlocks;
import com.road.eternalcore.common.block.ore.OreBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.loaders.MultiLayerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class ModBlockModelProvider extends BlockModelProvider{
    private final JsonObject missingTexture = new JsonObject();
    protected BlockModelBuilder machineCasingLayer;

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
        addCube(ModBlocks.smithingTable,
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
    private void addMachines(){
        addMachineCasing();
        addOrientableMachine2(MachineBlocks.locker.get(), "locker");
        addOrientableMachine(MachineBlocks.machineBlock.get(), "machine_block");
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
        // 添加铁外壳作为默认的机器外壳材质
        this.machineCasingLayer = withExistingParent("machine_casing_layer", modLoc("iron_machine_casing"));
    }

    protected void add(Block block, Supplier<BlockModelBuilder> sup){
        generatedModels.put(block.getRegistryName(), sup.get());
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
    // 有朝向的机器，提供正面材质和侧面材质（注意该材质是贴在机器外壳材质上面一层的）
    protected void addOrientableMachine(Block block, String faceName) {
        addOrientableMachine(block, faceName, "empty");
    }
    protected void addOrientableMachine(Block block, String faceName, String sideName){
        ResourceLocation side = machineSide(sideName);
        generatedModels.put(block.getRegistryName(), withExistingParent(
                ModBlocks.getBlockName(block), BLOCK_FOLDER + "/block")
                .texture("particle", side)
                .customLoader(MultiLayerModelBuilder::begin)
                .submodel(RenderType.solid(), machineCasingLayer)
                .submodel(RenderType.cutoutMipped(), orientable(
                                ModBlocks.getBlockName(block) + "_layer1",
                                side, machineFace(faceName), side
                        )
                ).end()
        );
    }
    protected void addOrientableMachine2(Block block, String faceName) {
        addOrientableMachine2(block, faceName, "empty");
    }
    protected void addOrientableMachine2(Block block, String faceName, String sideName){
        ResourceLocation side = machineSide(sideName);
        generatedModels.put(block.getRegistryName(), withExistingParent(
                ModBlocks.getBlockName(block), BLOCK_FOLDER + "/block")
                .texture("particle", side)
                .customLoader(MultiLayerModelBuilder::begin)
                .submodel(RenderType.solid(), machineCasingLayer)
                .submodel(RenderType.cutoutMipped(), orientable(
                        ModBlocks.getBlockName(block) + "_layer1",
                        side, machineFace(faceName), side
                        )
                ).end()
        );
    }
    protected void addTest(Block block){
        generatedModels.put(block.getRegistryName(), withExistingParent(
                ModBlocks.getBlockName(block), BLOCK_FOLDER + "/block")
                .customLoader(MultiLayerModelBuilder::begin)
                .submodel(RenderType.solid(), machineCasingLayer)
                .submodel(RenderType.cutoutMipped(), withExistingParent("machine_block_layer1", modLoc("machine_block")))
                .end()
        );
    }
}
