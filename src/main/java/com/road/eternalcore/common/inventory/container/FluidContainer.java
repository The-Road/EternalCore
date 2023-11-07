package com.road.eternalcore.common.inventory.container;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nullable;

public abstract class FluidContainer extends Container {
    protected FluidContainer(@Nullable ContainerType<?> containerType, int containerId) {
        super(containerType, containerId);
    }
}
