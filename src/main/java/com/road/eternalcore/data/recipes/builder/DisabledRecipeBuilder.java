package com.road.eternalcore.data.recipes.builder;

import com.google.gson.JsonObject;
import com.road.eternalcore.common.item.crafting.ModRecipeSerializer;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class DisabledRecipeBuilder{
    public static void disabled(Consumer<IFinishedRecipe> consumer, String id){
        consumer.accept(new IFinishedRecipe() {
            public void serializeRecipeData(JsonObject jsonObject) {
            }

            public IRecipeSerializer<?> getType() {
                return ModRecipeSerializer.disabledRecipe;
            }

            public ResourceLocation getId() {
                return new ResourceLocation(id);
            }

            @Nullable
            public JsonObject serializeAdvancement() {
                return null;
            }

            public ResourceLocation getAdvancementId() {
                return new ResourceLocation("");
            }
        });
    }
}
