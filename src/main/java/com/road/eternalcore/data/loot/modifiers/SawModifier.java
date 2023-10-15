package com.road.eternalcore.data.loot.modifiers;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.road.eternalcore.data.loot.ModLootParameters;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class SawModifier extends LootModifier {
    // 使用锯子收获方块
    public SawModifier(ILootCondition[] conditions){
        super(conditions);
    }
    @Nonnull
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (context.hasParam(ModLootParameters.SAW_MODIFIED)){
            return generatedLoot;
        }
        BlockState blockState = context.getParamOrNull(LootParameters.BLOCK_STATE);
        if (blockState != null){
            if (blockState.getBlock().is(BlockTags.ICE)){
                return getIceDrops(blockState, context);
            }
        }
        return generatedLoot;
    }

    private List<ItemStack> getIceDrops(BlockState blockState, LootContext context){
        // 采集冰块时，视为拥有精准采集
        ItemStack tool = context.getParamOrNull(LootParameters.TOOL);
        ItemStack silkTouch = tool != null ? tool.copy() : new ItemStack(Items.AIR);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(Enchantments.SILK_TOUCH, 1), silkTouch);
        LootContext newContext = new LootContext.Builder(context)
                .withParameter(LootParameters.TOOL, silkTouch)
                .withParameter(ModLootParameters.SAW_MODIFIED, true)
                .create(LootParameterSets.BLOCK);
        LootTable lootTable = context.getLootTable(blockState.getBlock().getLootTable());
        return lootTable.getRandomItems(newContext);
    }
    public static class Serializer extends GlobalLootModifierSerializer<SawModifier> {
        public SawModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditions){
            return new SawModifier(conditions);
        }
        public JsonObject write(SawModifier instance){
            return makeConditions(instance.conditions);
        }
    }
}
