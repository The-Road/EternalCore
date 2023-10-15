package com.road.eternalcore.common.item.crafting;

import com.road.eternalcore.Utils;
import com.road.eternalcore.common.item.crafting.recipe.DisabledRecipe;
import com.road.eternalcore.common.item.crafting.recipe.PartCraftingRecipe;
import com.road.eternalcore.common.item.crafting.recipe.ToolShapedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeSerializerRegistries {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Utils.MOD_ID);
    public static final RegistryObject<IRecipeSerializer<DisabledRecipe>> disabledRecipe = RECIPE_SERIALIZERS.register(
            "disabled", () -> new SpecialRecipeSerializer<>(DisabledRecipe::new));
    public static final RegistryObject<IRecipeSerializer<ToolShapedRecipe>> toolShapedRecipe = RECIPE_SERIALIZERS.register(
            "tool_crafting_shaped", ToolShapedRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<PartCraftingRecipe>> smithingRecipe = RECIPE_SERIALIZERS.register(
            "part_crafting", PartCraftingRecipe.Serializer::new);

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }
}
