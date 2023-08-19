package com.road.eternalcore.client.gui.screen.inventory;

import com.road.eternalcore.common.inventory.container.machine.BatteryBufferContainer;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BatteryBufferScreen extends MachineScreen<BatteryBufferContainer>{
    private static final ResourceLocation GUI_LOCATION = new ModResourceLocation("textures/gui/container/battery_buffer.png");
    public BatteryBufferScreen(BatteryBufferContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    protected ResourceLocation guiLocation() {
        return GUI_LOCATION;
    }
}
