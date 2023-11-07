package com.road.eternalcore.common.item.block;

import com.road.eternalcore.ModConstant;
import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.client.renderer.tileentity.MachineISTER;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
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
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class MachineBlockItem extends ModBlockItem{
    private static final Supplier<Callable<ItemStackTileEntityRenderer>> ISTER = () -> () -> MachineISTER.INSTANCE;
    public MachineBlockItem(Block block, Properties properties) {
        super(block, properties.setISTER(ISTER));
    }
    public static MaterialBlockData getMaterialBlockData(ItemStack itemStack){
        CompoundNBT tileEntityTag = itemStack.getTagElement("BlockEntityTag");
        if (tileEntityTag != null && tileEntityTag.contains(ModConstant.Material, 8)){
            return MaterialBlockData.get(Materials.get(tileEntityTag.getString(ModConstant.Material)));
        }
        return MaterialBlockData.NULL;
    }

    @OnlyIn(Dist.CLIENT)
    public void appendOtherHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag iTooltipFlag) {
        MaterialBlockData blockData = getMaterialBlockData(itemStack);
        list.add(new TranslationTextComponent(
                "eternalcore.toolTip.block.material", blockData.getMaterial().getText()
        ).withStyle(TextFormatting.GRAY));
    }
}
