package com.road.eternalcore.common.block.machine;

import com.road.eternalcore.api.block.ModBlockStateProperties;
import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.item.block.MachineBlockItem;
import com.road.eternalcore.common.item.tool.ModToolType;
import com.road.eternalcore.common.stats.ModStats;
import com.road.eternalcore.common.tileentity.MachineTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.LockableTileEntity;
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

import java.util.ArrayList;
import java.util.List;

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
            if (tileEntityMatch(tileEntity)){
                player.openMenu((INamedContainerProvider) tileEntity);
                player.awardStat(ModStats.INTERACT_WITH_MACHINE);
            }
            return ActionResultType.CONSUME;
        }
    }
    protected abstract boolean tileEntityMatch(TileEntity tileEntity);

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

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder lootContext$builder) {
        ItemStack tool = lootContext$builder.getOptionalParameter(LootParameters.TOOL);
        if (tool != null && tool.getToolTypes().stream().anyMatch((type -> type == ModToolType.WRENCH))){
            return super.getDrops(blockState, lootContext$builder);
        } else {
            TileEntity tileEntity = lootContext$builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
            List<ItemStack> list = tileEntityMatch(tileEntity) ?
                    getPartsDrops((MachineTileEntity) tileEntity) : new ArrayList<>();
            return list;
        }
    }

    // 获取机器的零件掉落
    public List<ItemStack> getPartsDrops(MachineTileEntity tileEntity){
        List<ItemStack> list = new ArrayList<>();
        Materials material = tileEntity.getMaterial();
        // 获取机器外壳
        list.add(new ItemStack(MachineBlocks.getMachineCasing(material)));
        // 获取机器零件（待添加）

        return list;
    }

    public boolean hasTileEntity(BlockState state){
        return true;
    }
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
    //给机器命名
    public void setPlacedBy(World world, BlockPos pos, BlockState blockState, LivingEntity player, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntityMatch(tileEntity)) {
                ((LockableTileEntity)tileEntity).setCustomName(itemStack.getHoverName());
            }
        }

    }

    // 四面朝向的机器正面不可接入覆盖板和电线（视为拥有覆盖板），六面朝向的机器正面是输出口
    protected abstract DirectionProperty facingType();
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        BlockState blockState = this.defaultBlockState();
        Materials material = MachineBlockItem.getMaterialBlockData(useContext.getItemInHand()).getMaterial();
        blockState = ModBlockStateProperties.MATERIAL.setBlockStateProperty(blockState, material);
        if (facingType() == BlockStateProperties.FACING){
            blockState = blockState.setValue(facingType(), useContext.getNearestLookingDirection().getOpposite());
        } else if (facingType() == BlockStateProperties.HORIZONTAL_FACING){
            blockState = blockState.setValue(facingType(), useContext.getHorizontalDirection().getOpposite());
        }
        return blockState;
    }
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ModBlockStateProperties.MATERIAL);
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
