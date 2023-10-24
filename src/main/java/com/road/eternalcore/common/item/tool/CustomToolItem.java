package com.road.eternalcore.common.item.tool;

import com.road.eternalcore.TranslationUtils;
import com.road.eternalcore.common.item.group.ModGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class CustomToolItem extends CustomTierItem{
    private final Set<Block> diggableBlocks;
    protected float mineSpeedRate = 1.0F;
    public CustomToolItem(float atk_damage, float atk_speed, Item.Properties properties){
        this(Collections.emptySet(), atk_damage, atk_speed, properties);
    }
    public CustomToolItem(Set<Block> diggableBlocks, float atk_damage, float atk_speed, Item.Properties properties){
        super(properties.tab(ModGroup.toolGroup), atk_damage, atk_speed);
        this.diggableBlocks = diggableBlocks;
    }

    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        if (getToolTypes(itemStack).stream().anyMatch(blockState::isToolEffective)){
            return getMineSpeed(itemStack);
        }
        if (this.diggableBlocks != null && this.diggableBlocks.contains(blockState.getBlock())){
            return getMineSpeed(itemStack);
        }
        return 1.0F;
    }
    public float getMineSpeedRate(){
        return mineSpeedRate;
    }

    @OnlyIn(Dist.CLIENT)
    public void addOtherHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag tooltipFlag) {
        // 显示物品的挖掘等级、挖掘速度、攻击伤害、攻击速度
        if (!this.onlyForCrafting) {
            list.add(TranslationUtils.toolTierLevel(getTierLevel(itemStack)));
            list.add(TranslationUtils.toolMineSpeed(getMineSpeed(itemStack) * getMineSpeedRate()));
            list.add(TranslationUtils.toolBasicDamage(getBasicAttackDamage(itemStack) + 1));
            list.add(TranslationUtils.toolAttackSpeed(getAttackSpeed(itemStack) + 4));
        }
        addDurabilityText(itemStack, list, tooltipFlag);
        // 关掉原本的属性显示
        itemStack.hideTooltipPart(ItemStack.TooltipDisplayFlags.MODIFIERS);
    }

}
