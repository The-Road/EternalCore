package com.road.eternalcore.compat.jei;

import com.road.eternalcore.client.gui.screen.inventory.HandcraftAssemblyScreen;
import com.road.eternalcore.client.gui.screen.inventory.PartCraftTableScreen;
import com.road.eternalcore.common.item.crafting.ModRecipeType;
import com.road.eternalcore.common.util.ModResourceLocation;
import com.road.eternalcore.compat.jei.category.CategoryConstant;
import com.road.eternalcore.compat.jei.category.PartCraftingRecipeCategory;
import com.road.eternalcore.compat.jei.category.ToolCraftingRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ModResourceLocation("jei");

    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new ToolCraftingRecipeCategory(guiHelper),
                new PartCraftingRecipeCategory(guiHelper)
        );
    }

    public void registerRecipes(IRecipeRegistration registration){
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        BiConsumer<IRecipeType, CategoryConstant> register = (recipeType, categoryConstant) -> registration.addRecipes(
                recipeManager.getAllRecipesFor(recipeType),
                categoryConstant.getUid()
        );
        register.accept(ModRecipeType.TOOL_CRAFTING, ToolCraftingRecipeCategory.CONSTANT);
        register.accept(ModRecipeType.PART_CRAFTING, PartCraftingRecipeCategory.CONSTANT);
    }

    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        Consumer<CategoryConstant> register = (categoryConstant) -> registration.addRecipeCatalyst(
                categoryConstant.getIconItemStack(),
                categoryConstant.getUid()
        );
        register.accept(ToolCraftingRecipeCategory.CONSTANT);
        register.accept(PartCraftingRecipeCategory.CONSTANT);
    }

    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(
                HandcraftAssemblyScreen.class, 118, 31, 17, 23,
                ToolCraftingRecipeCategory.CONSTANT.getUid()
        );
        registration.addRecipeClickArea(
                PartCraftTableScreen.class, 77, 49, 22, 15,
                PartCraftingRecipeCategory.CONSTANT.getUid()
        );
    }
    public void registerIngredients(IModIngredientRegistration registration) {

    }
}
