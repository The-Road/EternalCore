package com.road.eternalcore.data.model;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.road.eternalcore.Utils;
import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.item.ModItems;
import com.road.eternalcore.common.item.block.BlockItems;
import com.road.eternalcore.common.item.material.MaterialItems;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModItemModelProvider extends ItemModelProvider {
    private final JsonObject missingTexture = new JsonObject();

    public ModItemModelProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, Utils.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        addBlockModels();
        addMaterialModels();
        addToolModels();
        saveMissingTextureData();
    }
    private void saveMissingTextureData() {
        // 调试用，用于输出没添加的材质
        try {
            Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
            HashFunction SHA1 = Hashing.sha1();
            DirectoryCache directoryCache = new DirectoryCache(this.generator.getOutputFolder(), "cache");
            Path path = this.generator.getOutputFolder().resolve("assets/eternalcore/itemMissingTexture.json");
            String s = GSON.toJson(this.missingTexture);
            String s1 = SHA1.hashUnencodedChars(s).toString();
            Files.createDirectories(path.getParent());
            try (BufferedWriter bufferedwriter = Files.newBufferedWriter(path)) {
                bufferedwriter.write(s);
            }
            directoryCache.putNew(path, s1);
        } catch (IOException ioexception) {
            LogManager.getLogger().error("Couldn't save itemMissingTexture json", ioexception);
        }
    }
    protected void addBlockModels(){
        for (Item item : BlockItems.getAll()){
            generatedModels.put(item.getRegistryName(), withExistingParent(item.toString(), modLoc(BLOCK_FOLDER) + "/" + item));
        }
    }
    protected void addMaterialModels(){
        for (Materials material : Materials.getAllMaterials()){
            for (MaterialShape shape : material.getShapes()){
                Item item = MaterialItems.getMod(shape, material);
                if (item != null) {
                    ResourceLocation texture = modLoc(ITEM_FOLDER + "/" + shape.getName() + "/" + material.getName());
                    if (existingFileHelper.exists(texture, ModelProvider.TEXTURE)){
                        addFlatModel(item, texture);
                    }else{
                        if (missingTexture.get("materials") == null){
                            missingTexture.add("materials", new JsonObject());
                        }
                        JsonObject missingMaterials = missingTexture.getAsJsonObject("materials");
                        if (missingMaterials.get(shape.getName()) == null){
                            missingMaterials.add(shape.getName(), new JsonArray());
                        }
                        missingMaterials.getAsJsonArray(shape.getName()).add(material.getName());
                    }
                }
            }
        }
    }
    protected void addToolModels(){
        addHandheldModel(ModItems.debugTool);
        addHandheldModel(ModItems.pickaxe);
        addHandheldModel(ModItems.axe);
        addHandheldModel(ModItems.shovel);
        addHandheldModel(ModItems.hoe);
        addHandheldModel(ModItems.sword);
        addHandheldSpinModel(ModItems.knife);
        addHandheldModel(ModItems.hammer);
        addHandheldMirrorModel(ModItems.softMallet);
        addHandheldVerticalModel(ModItems.wrench);
        addHandheldMirrorModel(ModItems.file);
        addHandheldVerticalModel(ModItems.wireCutter);
        addHandheldMirrorModel(ModItems.screwdriver);
        addHandheldModel(ModItems.crowbar);
        addHandheldSpinModel(ModItems.saw);
    }

    protected void addFlatModel(Item item) {
        addFlatModel(item, new ModResourceLocation(ITEM_FOLDER + "/" + item));
    }
    protected void addFlatModel(Item item, String string) {
        addFlatModel(item, new ModResourceLocation(ITEM_FOLDER + "/" + string));
    }
    protected void addFlatModel(Item item, ResourceLocation layer0) {
        generatedModels.put(item.getRegistryName(), singleTexture(
                item.toString(),
                mcLoc(ITEM_FOLDER + "/generated"),
                "layer0",
                layer0
        ));
    }
    // 标准手持模型，物品朝右上，正面向左
    protected void addHandheldModel(Item item){
        addHandheldModel(item, new ModResourceLocation(ITEM_FOLDER + "/" + item));
    }
    protected void addHandheldModel(Item item, ResourceLocation layer0) {
        generatedModels.put(item.getRegistryName(), singleTexture(
                item.toString(),
                mcLoc(ITEM_FOLDER + "/handheld"),
                "layer0",
                layer0
        ));
    }
    // 垂直手持模型，物品朝正上，正面向左
    protected void addHandheldVerticalModel(Item item){
        addHandheldVerticalModel(item, new ModResourceLocation(ITEM_FOLDER + "/" + item));
    }
    protected void addHandheldVerticalModel(Item item, ResourceLocation layer0) {
        generatedModels.put(item.getRegistryName(), singleTexture(
                item.toString(),
                modLoc(ITEM_FOLDER + "/handheld_vertical"),
                "layer0",
                layer0
        ));
    }
    // 转向手持模型，物品朝左上，正面向左
    protected void addHandheldSpinModel(Item item){
        addHandheldSpinModel(item, new ModResourceLocation(ITEM_FOLDER + "/" + item));
    }
    protected void addHandheldSpinModel(Item item, ResourceLocation layer0) {
        generatedModels.put(item.getRegistryName(), singleTexture(
                item.toString(),
                modLoc(ITEM_FOLDER + "/handheld_spin"),
                "layer0",
                layer0
        ));
    }
    // 镜像手持模型，物品朝左上，正面向右
    protected void addHandheldMirrorModel(Item item){
        addHandheldMirrorModel(item, new ModResourceLocation(ITEM_FOLDER + "/" + item));
    }
    protected void addHandheldMirrorModel(Item item, ResourceLocation layer0) {
        generatedModels.put(item.getRegistryName(), singleTexture(
                item.toString(),
                modLoc(ITEM_FOLDER + "/handheld_mirror"),
                "layer0",
                layer0
        ));
    }
}
