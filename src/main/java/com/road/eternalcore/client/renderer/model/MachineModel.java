package com.road.eternalcore.client.renderer.model;

import com.road.eternalcore.api.block.ModBlockStateProperties;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.client.renderer.model.data.MachineModelData;
import com.road.eternalcore.common.block.machine.MachineBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MachineModel extends BakedModelWrapper<IBakedModel>{
    // 机器类方块的模型，用于额外渲染方块的材质（从对应的MachineCasing中读取）,机器的正面和输出面，以及覆盖板等。
    public MachineModel(IBakedModel originalModel) {
        super(originalModel);
    }

    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData){
        List<BakedQuad> quads = new ArrayList<>();
        RenderType layer = MinecraftForgeClient.getRenderLayer();
        if (layer == RenderType.solid()){
            // solid层是机器外壳的贴图
            BlockState casingBlockState = getCasingBlockState(extraData);
            IBakedModel casingModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(casingBlockState);
            quads.addAll(casingModel.getQuads(casingBlockState, side, rand, extraData));
        } else {
            // 添加机器本身的贴图
            quads.addAll(super.getQuads(state, side, rand, extraData));
        }
        return quads;
    }

    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        // 返回外壳粒子
        BlockState casingBlockState = getCasingBlockState(data);
        IBakedModel casingModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(casingBlockState);
        return casingModel.getParticleTexture(data);
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
}