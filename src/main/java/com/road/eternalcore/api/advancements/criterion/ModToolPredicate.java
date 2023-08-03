package com.road.eternalcore.api.advancements.criterion;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ModToolPredicate {
    private List<ToolType> types;
    public ModToolPredicate(List<ToolType> types){
        this.types = types;
    }

    public boolean matchType(ToolType type){
        return types.contains(type);
    }
    public boolean match(ItemStack itemStack){
        return itemStack.getToolTypes().stream().anyMatch(this::matchType);
    }

    public static ModToolPredicate fromJson(@Nullable JsonElement jsonElement) {
        List<ToolType> list = new ArrayList<>();
        if (jsonElement != null && !jsonElement.isJsonNull()){
            JsonObject jsonObject = JSONUtils.convertToJsonObject(jsonElement, "item");
            if (jsonObject.has("tool")){
                JsonArray jsonArray = JSONUtils.convertToJsonArray(jsonObject.get("tool"), "tool");
                for (int i = 0; i < jsonArray.size(); i++){
                    list.add(ToolType.get(jsonArray.get(i).getAsString()));
                }
            }
        }
        return new ModToolPredicate(list);
    }
    public JsonElement serializeToJson() {
        if (types.isEmpty()){
            return JsonNull.INSTANCE;
        } else {
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray = new JsonArray();
            for (ToolType toolType : types){
                jsonArray.add(toolType.getName());
            }
            jsonObject.add("tool", jsonArray);
            return jsonObject;
        }
    }

    public static class Builder {
        private List<ToolType> list = new ArrayList<>();
        private Builder(){
        }
        public static Builder tool(){
            return new Builder();
        }
        public Builder of(ToolType type){
            list.add(type);
            return this;
        }
        public ModToolPredicate build(){
            return new ModToolPredicate(list);
        }
    }
}
