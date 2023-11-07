package com.road.eternalcore.common.block.machine;

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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class MachineBlock extends AbstractMachineBlock{
    // 机器类方块，全部提供TileEntity和Inventory接口
    public MachineBlock(Properties properties){
        super(properties);
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

    public void onRemove(BlockState thisState, World world, BlockPos pos, BlockState newState, boolean blockUpdate) {
        if (!thisState.is(newState.getBlock())){
            // 被破坏时掉落自身物品栏
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof IInventory){
                InventoryHelper.dropContents(world, pos, (IInventory) tileEntity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(thisState, world, pos, newState, blockUpdate);
        }
    }

    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder lootContext$builder) {
        // 机器必须用扳手拆才能掉落完整机器，否则掉落零件
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
    protected abstract List<ItemStack> getPartsDrops(MachineTileEntity tileEntity);

    public boolean hasTileEntity(BlockState state){
        return true;
    }
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
    public void setPlacedBy(World world, BlockPos pos, BlockState blockState, LivingEntity player, ItemStack itemStack) {
        //给机器命名
        if (itemStack.hasCustomHoverName()) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntityMatch(tileEntity)) {
                ((LockableTileEntity)tileEntity).setCustomName(itemStack.getHoverName());
            }
        }

    }

    // 四面朝向的机器正面是显示屏，六面朝向的机器正面是输出口
    protected abstract DirectionProperty facingType();
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        BlockState blockState = this.defaultBlockState();
        if (facingType() == BlockStateProperties.FACING){
            blockState = blockState.setValue(facingType(), useContext.getNearestLookingDirection().getOpposite());
        } else if (facingType() == BlockStateProperties.HORIZONTAL_FACING){
            blockState = blockState.setValue(facingType(), useContext.getHorizontalDirection().getOpposite());
        }
        return blockState;
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
}
