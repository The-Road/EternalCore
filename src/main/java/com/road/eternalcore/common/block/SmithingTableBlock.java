package com.road.eternalcore.common.block;

import com.road.eternalcore.common.inventory.container.SmithingTableContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class SmithingTableBlock extends Block {
    private static final ITextComponent TITLE = new TranslationTextComponent("container.eternalcore.smithing_table");
    public SmithingTableBlock(AbstractBlock.Properties properties){
        super(properties);
    }
    public ActionResultType use(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace){
        if (world.isClientSide()){
            return ActionResultType.SUCCESS;
        }else{
            player.openMenu(blockState.getMenuProvider(world, pos));
            return ActionResultType.CONSUME;
        }
    }
    public INamedContainerProvider getMenuProvider(BlockState blockState, World world, BlockPos pos) {
        return new SimpleNamedContainerProvider(
                (containerId, playerInventory, playerEntity) -> new SmithingTableContainer(1, containerId, playerInventory, IWorldPosCallable.create(world, pos)),
                TITLE
        );
    }
}
