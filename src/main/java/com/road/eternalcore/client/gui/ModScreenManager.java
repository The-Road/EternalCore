package com.road.eternalcore.client.gui;

import com.road.eternalcore.client.gui.screen.inventory.BatteryBufferScreen;
import com.road.eternalcore.client.gui.screen.inventory.HandcraftAssemblyScreen;
import com.road.eternalcore.client.gui.screen.inventory.MachineBlockScreen;
import com.road.eternalcore.client.gui.screen.inventory.SmithingTableScreen;
import com.road.eternalcore.common.inventory.container.ModContainerType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModScreenManager extends ScreenManager {
    public static void register(){
        register(ModContainerType.handcraftAssembly, HandcraftAssemblyScreen::new);
        register(ModContainerType.smithingTable, SmithingTableScreen::new);
        register(ModContainerType.machineBlock, MachineBlockScreen::new);
        register(ModContainerType.batteryBuffer, BatteryBufferScreen::new);
    }
}
