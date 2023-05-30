package com.road.eternalcore.common.item.battery;

import com.road.eternalcore.api.energy.eu.EUTier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import com.road.eternalcore.api.energy.CapEnergy;

public class DebugBatteryItem extends BatteryItem{
    public DebugBatteryItem() {
        super(1_0000_0000, EUTier.LV, new Properties());
    }
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        itemStack.getCapability(CapEnergy.EU).ifPresent(storage -> {
            storage.receiveEnergy(1000_0000, false);
        });
        return ActionResult.success(itemStack);
    }
}
