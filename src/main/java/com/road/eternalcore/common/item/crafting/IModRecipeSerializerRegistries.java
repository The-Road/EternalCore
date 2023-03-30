package com.road.eternalcore.common.item.crafting;

import com.road.eternalcore.Utils;
import com.road.eternalcore.common.item.crafting.recipe.SmithingRecipe;
import com.road.eternalcore.common.item.crafting.recipe.ToolShapedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class IModRecipeSerializerRegistries {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Utils.MOD_ID);
    public static final RegistryObject<IRecipeSerializer<ToolShapedRecipe>> toolShapedRecipe = RECIPE_SERIALIZERS.register(
            "toolcrafting_shaped", ToolShapedRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<SmithingRecipe>> smithingRecipe = RECIPE_SERIALIZERS.register(
            "smithing", SmithingRecipe.Serializer::new);

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }
}
