package com.road.eternalcore.common.item.block;

import com.road.eternalcore.api.material.MaterialBlockData;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class MachineBlockItem extends ModBlockItem{
    public MachineBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
    public static MaterialBlockData getMaterialBlockData(ItemStack itemStack){
        CompoundNBT tileEntityTag = itemStack.getTagElement("BlockEntityTag");
        if (tileEntityTag != null && tileEntityTag.contains("material", 8)){
            return MaterialBlockData.get(tileEntityTag.getString("material"));
        }
        return MaterialBlockData.NULL;
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag iTooltipFlag) {
        MaterialBlockData blockData = getMaterialBlockData(itemStack);
        list.add(
                new TranslationTextComponent(
                        "eternalcore.toolTip.block.material",
                        blockData.getMaterial().getText()
                ).withStyle(TextFormatting.GRAY)
        );
    }
}
