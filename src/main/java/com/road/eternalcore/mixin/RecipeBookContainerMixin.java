package com.road.eternalcore.mixin;

import com.road.eternalcore.common.item.crafting.ServerRecipePlacer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.item.crafting.IRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RecipeBookContainer.class)
public abstract class RecipeBookContainerMixin<C extends IInventory> {
    /**
     * @author
     * Road
     * @reason
     * 修复配方书吞物品的BUG
     */
    @Overwrite
    public void handlePlacement(boolean p_217056_1_, IRecipe<?> p_217056_2_, ServerPlayerEntity p_217056_3_) {
        (new ServerRecipePlacer<>((RecipeBookContainer<C>) (Object)this)).recipeClicked(p_217056_3_, (IRecipe<C>)p_217056_2_, p_217056_1_);
    }
}
