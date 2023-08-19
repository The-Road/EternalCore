package com.road.eternalcore.client.renderer.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import com.road.eternalcore.api.block.ModBlockStateProperties;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.client.renderer.model.data.MachineModelData;
import com.road.eternalcore.common.block.machine.MachineBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.MultiLayerModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class MachineModel extends BakedModelWrapper<IBakedModel>{
    // 机器类方块的模型，用于额外渲染方块的材质（从对应的MachineCasing中读取）,机器的正面和输出面，以及覆盖板等。
    // 方块的模型文件中只有机器表面的贴图，渲染时会通过getQuads获取机器外壳的贴图作为solid层
    // 是MultiLayerBakedModel的延伸，不过MultiLayerBakedModel是private所以这里只能继承IBakedModel了
    public MachineModel(IBakedModel originalModel) {
        super(originalModel);
    }

    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData){
        List<BakedQuad> quads = new ArrayList<>();
        RenderType layer = MinecraftForgeClient.getRenderLayer();
        if (layer == null || layer == RenderType.solid()){
            // solid层是机器外壳的贴图
            BlockState casingBlockState = getCasingBlockState(extraData);
            IBakedModel casingModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(casingBlockState);
            quads.addAll(casingModel.getQuads(casingBlockState, side, rand, extraData));
        }
        // 添加机器本身的贴图
        quads.addAll(super.getQuads(state, side, rand, extraData));
        return quads;
    }

    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        // 返回外壳粒子
        BlockState casingBlockState = getCasingBlockState(data);
        IBakedModel casingModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(casingBlockState);
        return casingModel.getParticleTexture(data);
    }

    public boolean isCustomRenderer() {
        return true;
    }

    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
        // super会返回一个MultiLayerBakedModel，需要重新包装一下
        IBakedModel result = super.handlePerspective(cameraTransformType, mat);
        if (!(result instanceof MachineModel)){
            result = new MachineModel(result);
        }
        return result;
    }

    public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.hasProperty(ModBlockStateProperties.MachineMaterial)){
            String materialName = blockState.getValue(ModBlockStateProperties.MachineMaterial);
            return new MachineModelData(Materials.get(materialName));
        }
        return super.getModelData(world, pos, state, tileData);
    }

    // 获取机器方块的外壳模型
    protected BlockState getCasingBlockState(IModelData data){
        Materials material = Materials.NULL;
        // 获取对应机器方块的材质
        if (data.hasProperty(MachineModelData.MaterialProperty)){
            material = data.getData(MachineModelData.MaterialProperty);
        }
        return MachineBlocks.getMachineCasing(material).defaultBlockState();
    }

    // ————注册和构造的部分————
    public static final class RawModel implements IModelGeometry<RawModel>{
        private final MultiLayerModel originModel;
        public RawModel(MultiLayerModel originModel){
            this.originModel = originModel;
        }
        public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
            return new MachineModel(originModel.bake(owner, bakery, spriteGetter, modelTransform, overrides, modelLocation));
        }

        public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
            return originModel.getTextures(owner, modelGetter, missingTextureErrors);
        }
    }
    public static final class Loader implements IModelLoader<RawModel>{
        public static final Loader INSTANCE = new Loader();
        private static final MultiLayerModel.Loader ML = MultiLayerModel.Loader.INSTANCE;
        private Loader(){}

        public void onResourceManagerReload(IResourceManager resourceManager) {
            ML.onResourceManagerReload(resourceManager);
        }

        public RawModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
            MultiLayerModel originModel = ML.read(deserializationContext, modelContents);
            return new RawModel(originModel);
        }
    }
}