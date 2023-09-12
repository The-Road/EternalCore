package com.road.eternalcore.common.world;

import com.road.eternalcore.common.world.energy.EnergyNetworkManager;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldEventManager {

    @SubscribeEvent
    public static void onServerWorldTick(TickEvent.WorldTickEvent event){
        World world = event.world;
        if (Objects.equals(event.side, LogicalSide.SERVER)){
            switch (event.phase){
                case START: {
                    EnergyNetworkManager.get(world).tickStart();
                    break;
                }
                case END: {
                    EnergyNetworkManager.get(world).tickEnd();
                    break;
                }
            }
        }
    }
}
