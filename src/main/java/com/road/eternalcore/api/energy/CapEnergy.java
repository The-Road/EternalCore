package com.road.eternalcore.api.energy;

import com.road.eternalcore.api.energy.eu.IEUStorage;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.energy.IEnergyStorage;

public class CapEnergy {
    @CapabilityInject(IEnergyStorage.class)
    public static Capability<IEnergyStorage> FE;
    @CapabilityInject(IEUStorage.class)
    public static Capability<IEUStorage> EU;

    public static void register(){
        // 注册EU
        CapabilityManager.INSTANCE.register(IEUStorage.class, new Capability.IStorage<IEUStorage>() {
            @Override
            public INBT writeNBT(Capability<IEUStorage> capability, IEUStorage instance, Direction side) {
                return IntNBT.valueOf(instance.getEnergyStored());
            }
            @Override
            public void readNBT(Capability<IEUStorage> capability, IEUStorage instance, Direction side, INBT nbt) {
                instance.saveEnergy(((IntNBT)nbt).getAsInt());
            }
            }, () -> null
        );
    }
}
