package com.road.eternalcore.common.entity.player;

import com.road.eternalcore.common.item.tool.CustomTierItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event){
        // 应用工具的挖掘速度倍率
        Item mainHandItem = event.getPlayer().getMainHandItem().getItem();
        if (mainHandItem instanceof CustomTierItem){
            event.setNewSpeed(event.getOriginalSpeed() * ((CustomTierItem) mainHandItem).getMineSpeedRate());
        }
    }
}
