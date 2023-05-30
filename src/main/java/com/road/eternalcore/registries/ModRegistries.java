package com.road.eternalcore.registries;

import com.road.eternalcore.common.block.ModBlockRegistries;
import com.road.eternalcore.common.inventory.container.ModContainerRegistries;
import com.road.eternalcore.common.item.ModItemRegistries;
import com.road.eternalcore.common.item.crafting.IModRecipeSerializerRegistries;
import com.road.eternalcore.common.tileentity.ModTileEntityRegistries;
import com.road.eternalcore.common.world.generate.ScatteredOreGen;
import com.road.eternalcore.data.loot.functions.ModLootFunctionManager;
import com.road.eternalcore.data.loot.modifiers.GLMRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ModRegistries {
    // 用于模组的各种注册内容
    public static void register(){
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // bus.addListener(ClientSetupEventHandler::onSetupEvent);
        // 注册方块
        ModBlockRegistries.register(bus);
        // 注册方块实体
        ModTileEntityRegistries.register(bus);
        // 注册物品
        ModItemRegistries.register(bus);
        // 注册配方类型
        IModRecipeSerializerRegistries.register(bus);
        // 注册战利品
        ModLootFunctionManager.init();
        GLMRegistry.GLM.register(bus);
        // 注册容器
        ModContainerRegistries.register(bus);
        // 注册世界生成
        MinecraftForge.EVENT_BUS.register(new ModRegistries());
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event){
        // 注册散矿的生成
        ScatteredOreGen.register(event);
    }
}
