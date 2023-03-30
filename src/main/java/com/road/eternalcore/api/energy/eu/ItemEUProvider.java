package com.road.eternalcore.api.energy.eu;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ItemEUProvider implements ICapabilityProvider {
    private LazyOptional<ItemEUStorage> storage;

    public ItemEUProvider(NonNullSupplier<ItemEUStorage> storage){
        this.storage = LazyOptional.of(storage);
    }
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (Objects.equals(cap, CapabilityEnergy.ENERGY)){
            return storage.cast();
        }else {
            return LazyOptional.empty();
        }
    }
}
