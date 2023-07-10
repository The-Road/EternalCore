package com.road.eternalcore.registries;

import com.road.eternalcore.Utils;
import com.road.eternalcore.client.gui.ModScreenManager;
import com.road.eternalcore.client.renderer.ModRenderType;
import com.road.eternalcore.client.renderer.model.builder.ModModelLoaderRegistry;
import com.road.eternalcore.common.item.ModItemModelsProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Utils.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onSetupEvent(FMLClientSetupEvent event) {
        // 不知为何enqueueWork中的内容不会执行
        ModItemModelsProperties.registerModelsProperties();
        ModScreenManager.register();
        ModRenderType.registerBlockRenderType();
        registerTER();
    }
    private static void registerTER(){
        // 暂时用不到TER
        //ClientRegistry.bindTileEntityRenderer(ModTileEntityType.machineBlock, MachineTER::new);
    }

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event){
        ModModelLoaderRegistry.init();
    }

    @SubscribeEvent
    public static void onModelBakeEvent(ModelBakeEvent event){

    }

}
