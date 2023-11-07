package com.road.eternalcore.common.block.machine;

import com.google.common.collect.Lists;
import com.road.eternalcore.api.block.ModBlockStateProperties;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.block.ICasingRenderBlock;
import com.road.eternalcore.common.tileentity.AlloyFurnaceTileEntity;
import com.road.eternalcore.common.tileentity.MachineTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

import java.util.List;

public class AlloyFurnaceBlock extends MachineBlock implements ICasingRenderBlock {
    public AlloyFurnaceBlock(){
        super(stoneProperties(3.5F, 3.5F));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(facingType(), Direction.NORTH)
                .setValue(ModBlockStateProperties.WORKING, false)
        );
    }

    protected boolean tileEntityMatch(TileEntity tileEntity) {
        return tileEntity instanceof AlloyFurnaceTileEntity;
    }

    protected List<ItemStack> getPartsDrops(MachineTileEntity tileEntity) {
        return Lists.newArrayList(new ItemStack(MachineBlocks.getBrickedCasing(Materials.STONE)));
    }

    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AlloyFurnaceTileEntity();
    }

    protected DirectionProperty facingType() {
        return BlockStateProperties.HORIZONTAL_FACING;
    }
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ModBlockStateProperties.WORKING);
    }
    public BlockState getRenderCasing(BlockState blockState) {
        return MachineBlocks.getBrickedCasing(Materials.STONE).defaultBlockState();
    }
}
