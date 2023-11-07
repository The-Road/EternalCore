package com.road.eternalcore.registries;

import com.road.eternalcore.common.block.ModBlockRegistries;
import com.road.eternalcore.common.fluid.ModFluidRegistries;
import com.road.eternalcore.common.inventory.container.ModContainerRegistries;
import com.road.eternalcore.common.item.ModItemRegistries;
import com.road.eternalcore.common.item.crafting.ModRecipeSerializerRegistries;
import com.road.eternalcore.common.tileentity.ModTileEntityRegistries;
import com.road.eternalcore.common.world.generate.ScatteredOreGen;
import com.road.eternalcore.data.loot.conditions.ModLootConditionManager;
import com.road.eternalcore.data.loot.functions.ModLootFunctionManager;
import com.road.eternalcore.data.loot.modifiers.GLMRegistry;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModRegistries {
    // 用于模组的各种注册内容
    public static void register(){
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // 注册方块
        ModBlockRegistries.register(bus);
        // 注册方块实体
        ModTileEntityRegistries.register(bus);
        // 注册流体
        ForgeMod.enableMilkFluid();
        ModFluidRegistries.register(bus);
        // 注册物品
        ModItemRegistries.register(bus);
        // 注册配方类型
        ModRecipeSerializerRegistries.register(bus);
        // 注册战利品
        ModLootConditionManager.init();
        ModLootFunctionManager.init();
        GLMRegistry.GLM.register(bus);
        // 注册容器
        ModContainerRegistries.register(bus);
    }

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event){
        // 注册散矿的生成
        ScatteredOreGen.register(event);
    }
}
