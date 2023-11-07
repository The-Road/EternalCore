package com.road.eternalcore.common.item.block;

import com.road.eternalcore.common.block.pipe.WireBlock;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class WireBlockItem extends ModBlockItem {

    public WireBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendOtherHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag iTooltipFlag) {
        Block block = ((BlockItem) itemStack.getItem()).getBlock();
        if (block instanceof WireBlock) {
            WireBlock wire = (WireBlock) block;
            list.add(new TranslationTextComponent(
                    "eternalcore.toolTip.eu.tier", wire.getTier().getText(), wire.getTier().getMaxVoltage()
            ).withStyle(TextFormatting.GRAY));
            list.add(new TranslationTextComponent(
                    "eternalcore.toolTip.eu.maxCurrent", wire.getMaxCurrent()
            ).withStyle(TextFormatting.GRAY));
            list.add(new TranslationTextComponent(
                    "eternalcore.toolTip.eu.lineLoss", wire.getLineLoss()
            ).withStyle(TextFormatting.GRAY));
        }
    }
}