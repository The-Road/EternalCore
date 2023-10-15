package com.road.eternalcore.data.recipes.builder;

import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public abstract class RecipeMaker<T extends RecipeBuilder> {
    // RecipeMaker是用来在RecipeProvider中生成配方的
    protected final T builder;
    public RecipeMaker(T builder) {
        this.builder = builder;
    }
    public void save(Consumer<IFinishedRecipe> consumer) {
        builder.save(consumer, ForgeRegistries.ITEMS.getKey(builder.result));
    }
    public void save(Consumer<IFinishedRecipe> consumer, String id) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(builder.result);
        if ((new ModResourceLocation(id)).equals(resourcelocation)) {
            throw new IllegalStateException("Shaped Recipe " + id + " should remove its 'save' argument");
        } else {
            builder.save(consumer, new ModResourceLocation(id));
        }
    }
}
