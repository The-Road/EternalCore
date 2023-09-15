package com.road.eternalcore.data.recipes.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class MultiResultRecipeBuilder extends RecipeBuilder{
    protected List<ResultData> results = new ArrayList<>();

    public MultiResultRecipeBuilder(IItemProvider item, int count, Consumer<CompoundNBT> nbtConsumer) {
        super(Items.AIR, 0);
        addResult(item, count, nbtConsumer);
    }

    public void addResult(IItemProvider item, int count, Consumer<CompoundNBT> nbtConsumer){
        results.add(new ResultData(item, count, nbtConsumer));
    }
    public static NonNullList<ItemStack> getResultsFromJson(JsonObject json){
        JsonArray jsonArray = JSONUtils.getAsJsonArray(json, "result");
        NonNullList<ItemStack> items = NonNullList.create();
        for(int i = 0; i < jsonArray.size(); ++i) {
            items.add(CraftingHelper.getItemStack(jsonArray.get(i).getAsJsonObject(), true));
        }
        return items;
    }

    public static class ResultData {
        public final Item item;
        public final CompoundNBT nbt = new CompoundNBT();
        public final int count;

        public ResultData(IItemProvider item, int count, Consumer<CompoundNBT> nbtConsumer){
            this.item = item.asItem();
            this.count = count;
            nbtConsumer.accept(this.nbt);
        }
    }
    protected static abstract class Result extends RecipeBuilder.Result{
        protected final List<ResultData> results;

        public Result(ResourceLocation id, List<ResultData> results, String group, Advancement.Builder advancement, ResourceLocation advancementId) {
            super(id, Items.AIR, new CompoundNBT(), 0, group, advancement, advancementId);
            this.results = results;
        }
        protected void serializeRecipeResult(JsonObject json){
            JsonArray resultArray = new JsonArray();
            for (ResultData data : results) {
                JsonObject resultJson = new JsonObject();
                resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(data.item).toString());
                if (!data.nbt.isEmpty()) {
                    resultJson.addProperty("nbt", data.nbt.toString());
                }
                if (data.count > 1) {
                    resultJson.addProperty("count", data.count);
                }
                resultArray.add(resultJson);
            }
            json.add("result", resultArray);
        }
    }
}
