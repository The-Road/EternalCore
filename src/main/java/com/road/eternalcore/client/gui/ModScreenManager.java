package com.road.eternalcore.client.gui;

import com.road.eternalcore.client.gui.screen.inventory.HandcraftAssemblyScreen;
import com.road.eternalcore.client.gui.screen.inventory.SmithingTableScreen;
import com.road.eternalcore.common.inventory.container.ModContainerType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ModScreenManager extends ScreenManager {
    public static void init(FMLClientSetupEvent event){
        register(ModContainerType.handcraftAssembly, HandcraftAssemblyScreen::new);
        register(ModContainerType.smithingTable, SmithingTableScreen::new);
    }
}
