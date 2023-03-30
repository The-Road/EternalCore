package com.road.eternalcore.data.loot.modifiers;

import com.google.gson.JsonObject;
import com.road.eternalcore.common.item.ModItems;
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
    public HammerSmashModifier(ILootCondition[] conditions){
        super(conditions);
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context){
        ArrayList<ItemStack> newLoot = new ArrayList<>();
        generatedLoot.forEach((stack) -> newLoot.add(smashed(stack, context)));
        return newLoot;
    }

    private static ItemStack smashed(ItemStack stack, LootContext context){
        // 被锤子挖掘的方块会掉落锤子粉碎后的产物
        if (stack.isEmpty()) {
            return stack;
        }else{
            if (stack.getItem() == Items.STONE || stack.getItem() == Items.COBBLESTONE){
                ItemStack stack1 = new ItemStack(ModItems.stoneDust);
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
