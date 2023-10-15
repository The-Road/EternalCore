package com.road.eternalcore.data.loot.modifiers;

import com.google.gson.JsonObject;
import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.item.material.MaterialItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class HammerSmashModifier extends LootModifier {
    // 被锤子挖掘的方块会掉落锤子粉碎后的产物
    public HammerSmashModifier(ILootCondition[] conditions){
        super(conditions);
    }

    @Nonnull
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context){
        ArrayList<ItemStack> newLoot = new ArrayList<>();
        generatedLoot.forEach((stack) -> newLoot.add(smashed(stack, context)));
        return newLoot;
    }

    private static ItemStack smashed(ItemStack stack, LootContext context){
        // TODO: 根据锻造锤的合成配方修改
        if (stack.isEmpty()) {
            return stack;
        }else{
            if (stack.getItem() == Items.STONE || stack.getItem() == Items.COBBLESTONE){
                ItemStack stack1 = new ItemStack(MaterialItems.get(MaterialShape.DUST, Materials.STONE));
                stack1.setCount(stack.getCount());
                return stack1;
            }
            return stack;
        }
    }

    public static class Serializer extends GlobalLootModifierSerializer<HammerSmashModifier>{
        public HammerSmashModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditions){
            return new HammerSmashModifier(conditions);
        }
        public JsonObject write(HammerSmashModifier instance){
            return makeConditions(instance.conditions);
        }
    }
}
