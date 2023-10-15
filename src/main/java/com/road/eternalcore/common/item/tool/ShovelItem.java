package com.road.eternalcore.common.item.tool;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;

import java.util.Set;

public class ShovelItem extends CustomToolItem{
    private static final Set<Block> DIGGABLES = Sets.newHashSet(
            Blocks.CLAY, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.FARMLAND, Blocks.GRASS_BLOCK,
            Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.RED_SAND, Blocks.SNOW_BLOCK, Blocks.SNOW,
            Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER,
            Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER,
            Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER,
            Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER,
            Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER,
            Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER, Blocks.SOUL_SOIL
    );
    private static final net.minecraft.item.ShovelItem shovelReference = (net.minecraft.item.ShovelItem) Items.IRON_SHOVEL;
    public ShovelItem(){
        super(DIGGABLES, 1.5F, 1.0F, new Properties().addToolType(ModToolType.SHOVEL, 0));
    }
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return blockState.is(Blocks.SNOW) || blockState.is(Blocks.SNOW_BLOCK);
    }
    public ActionResultType useOn(ItemUseContext useContext) {
        return shovelReference.useOn(useContext);
    }
}
