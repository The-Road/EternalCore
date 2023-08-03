package com.road.eternalcore.data.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.road.eternalcore.api.advancements.criterion.ModToolPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;

public class ModMatchTool implements ILootCondition {
    private final ModToolPredicate predicate;
    public ModMatchTool(ModToolPredicate predicate){
        this.predicate = predicate;
    }
    public LootConditionType getType() {
        return ModLootConditionManager.MATCH_TOOL;
    }

    public boolean test(LootContext lootContext) {
        ItemStack itemStack = lootContext.getParamOrNull(LootParameters.TOOL);
        System.out.println(itemStack);
        return itemStack != null && this.predicate.match(itemStack);
    }

    public static ILootCondition.IBuilder toolMatches(ModToolPredicate.Builder builder){
        return () -> new ModMatchTool(builder.build());
    }
    public static class Serializer implements ILootSerializer<ModMatchTool> {

        public void serialize(JsonObject json, ModMatchTool matchTool, JsonSerializationContext context) {
            json.add("predicate", matchTool.predicate.serializeToJson());
        }

        public ModMatchTool deserialize(JsonObject jsonObject, JsonDeserializationContext context) {
            ModToolPredicate predicate = ModToolPredicate.fromJson(jsonObject.get("predicate"));
            return new ModMatchTool(predicate);
        }
    }
}
