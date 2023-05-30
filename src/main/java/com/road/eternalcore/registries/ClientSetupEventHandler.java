package com.road.eternalcore.registries;

import com.road.eternalcore.Utils;
import com.road.eternalcore.client.gui.ModScreenManager;
import com.road.eternalcore.common.item.ModItemModelsProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Utils.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEventHandler {
    @SubscribeEvent
    public static void onSetupEvent(FMLClientSetupEvent event){
        // 不知为何enqueueWork没有工作
        ModItemModelsProperties.registerModelsProperties();
        ModScreenManager.register();
    }
}
