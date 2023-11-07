package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.api.block.ModBlockStateProperties;
import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.block.ICasingRenderBlock;
import com.road.eternalcore.common.block.ICustomHardnessBlock;
import com.road.eternalcore.common.item.block.MachineBlockItem;
import com.road.eternalcore.common.tileentity.MachineTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;

import java.util.ArrayList;
import java.util.List;

public abstract class CasingMachineBlock extends MachineBlock implements ICustomHardnessBlock, ICasingRenderBlock {
    // 基于机器外壳的机器类方块，BlockState拥有MATERIAL方块状态，TileEntity也拥有blockData属性
    // BlockState的方块状态仅用于渲染外观，剩余功能由TileEntity中的blockData完成
    // 材料用于计算方块的硬度和爆炸抗性

    public CasingMachineBlock() {
        super(machineProperties(1.0F, 1.0F));
    }

    protected List<ItemStack> getPartsDrops(MachineTileEntity tileEntity){
        List<ItemStack> list = new ArrayList<>();
        Materials material = tileEntity.getMaterial();
        // 获取机器外壳
        list.add(new ItemStack(MachineBlocks.getMachineCasing(material)));
        // TODO: 获取机器零件

        return list;
    }
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        BlockState blockState = super.getStateForPlacement(useContext);
        Materials material = MachineBlockItem.getMaterialBlockData(useContext.getItemInHand()).getMaterial();
        blockState = ModBlockStateProperties.MATERIAL.setBlockStateProperty(blockState, material);
        return blockState;
    }
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ModBlockStateProperties.MATERIAL);
        super.createBlockStateDefinition(builder);
    }

    // 获取材料属性
    public MaterialBlockData getMaterialBlockData(BlockState blockState, IBlockReader world, BlockPos pos){
        if (blockState.getBlock() instanceof CasingMachineBlock){
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof MachineTileEntity){
                return ((MachineTileEntity) tileEntity).getMaterialBlockData();
            }
        }
        return MaterialBlockData.NULL;
    }
    // 通过材料属性获取硬度和爆炸抗性

    public float customDestroySpeed(BlockState blockState, IBlockReader world, BlockPos pos) {
        return getMaterialBlockData(blockState, world, pos).getCasingData().getDestroyTime();
    }
    public float customExplosionResistance(BlockState blockState, IBlockReader world, BlockPos pos) {
        return getMaterialBlockData(blockState, world, pos).getCasingData().getExplosionResistance();
    }

    public float getDestroyProgress(BlockState blockState, PlayerEntity player, IBlockReader world, BlockPos pos) {
        return super.getDestroyProgress(blockState, player, world, pos) / customDestroySpeed(blockState, world, pos);
    }
    public float getExplosionResistance(BlockState blockState, IBlockReader world, BlockPos pos, Explosion explosion){
        return customExplosionResistance(blockState, world, pos);
    }

    public BlockState getRenderCasing(BlockState blockState) {
        if (blockState.hasProperty(ModBlockStateProperties.MATERIAL)) {
            return MachineBlocks.getMachineCasing(
                    Materials.get(blockState.getValue(ModBlockStateProperties.MATERIAL).value())
            ).defaultBlockState();
        }
        return MachineBlocks.getMachineCasing(Materials.NULL).defaultBlockState();
    }
}
