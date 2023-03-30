package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.common.stats.ModStats;
import com.road.eternalcore.common.tileentity.MachineTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public abstract class MachineBlock extends AbstractMachineBlock{
    // 机器类方块，全部提供TileEntity和Inventory接口
    // 这一类方块的TileEntity全部实现IMaterialTileEntity接口，拥有材质属性
    // 材质属性会影响机器外壳的渲染和方块的硬度和爆炸抗性

    public MachineBlock() {
        super(1.0F, 1.0F);
    }

    public ActionResultType use(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace){
        if (world.isClientSide()){
            return ActionResultType.SUCCESS;
        }else{
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (TileEntityMatch(tileEntity)){
                player.openMenu((INamedContainerProvider) tileEntity);
                player.awardStat(ModStats.INTERACT_WITH_MACHINE);
            }
            return ActionResultType.CONSUME;
        }
    }
    protected abstract boolean TileEntityMatch(TileEntity tileEntity);

    public void onRemove(BlockState thisBlock, World world, BlockPos pos, BlockState lastBlock, boolean blockUpdate) {
        if (!thisBlock.is(lastBlock.getBlock())){
            // 被破坏时掉落自身物品栏
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof IInventory){
                InventoryHelper.dropContents(world, pos, (IInventory) tileEntity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(thisBlock, world, pos, lastBlock, blockUpdate);
        }
    }

    public boolean hasTileEntity(BlockState state){
        return true;
    }
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
    //给机器命名
    public void setPlacedBy(World world, BlockPos pos, BlockState blockState, LivingEntity player, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (TileEntityMatch(tileEntity)) {
                ((BarrelTileEntity)tileEntity).setCustomName(itemStack.getHoverName());
            }
        }

    }

    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    protected abstract DirectionProperty facingType();
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        if (facingType() == BlockStateProperties.FACING){
            return this.defaultBlockState().setValue(facingType(), useContext.getNearestLookingDirection().getOpposite());
        }
        if (facingType() == BlockStateProperties.HORIZONTAL_FACING){
            return this.defaultBlockState().setValue(facingType(), useContext.getHorizontalDirection().getOpposite());
        }
        return super.getStateForPlacement(useContext);
    }
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(facingType());
    }
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(facingType(), rotation.rotate(blockState.getValue(facingType())));
    }
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(facingType())));
    }

    // 获取材料属性
    public MaterialBlockData getMaterialBlockData(BlockState blockState, IBlockReader world, BlockPos pos){
        if (blockState.getBlock() instanceof MachineBlock){
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof MachineTileEntity){
                return ((MachineTileEntity) tileEntity).getMaterialBlockData();
            }
        }
        return MaterialBlockData.NULL;
    }
    // 通过材料属性获取硬度和爆炸抗性
    public float getDestroyProgress(BlockState blockState, PlayerEntity player, IBlockReader world, BlockPos pos) {
        MaterialBlockData blockData = getMaterialBlockData(blockState, world, pos);
        return super.getDestroyProgress(blockState, player, world, pos) / blockData.getHullData().getDestroyTime();
    }
    public float getExplosionResistance(BlockState blockState, IBlockReader world, BlockPos pos, Explosion explosion){
        MaterialBlockData blockData = getMaterialBlockData(blockState, world, pos);
        return blockData.getHullData().getExplosionResistance();
    }
}
