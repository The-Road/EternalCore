package com.road.eternalcore.data.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.road.eternalcore.common.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;

public class HammerSmash extends LootFunction {
    // 已经用HammerSmashModifier代替，留着作为写其他LootFunction的样板
    private HammerSmash(ILootCondition[] conditions){
        super(conditions);
    }

    public LootFunctionType getType(){
        return ModLootFunctionManager.HAMMER_SMASH;
    }

    public ItemStack run(ItemStack stack, LootContext lootContext){
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

    public static LootFunction.Builder<?> smashed(){
        return simpleBuilder(HammerSmash::new);
    }

    public static class Serializer extends LootFunction.Serializer<HammerSmash> {
        public HammerSmash deserialize(JsonObject json, JsonDeserializationContext jsonContext, ILootCondition[] conditions){
            return new HammerSmash(conditions);
        }
    }
}
