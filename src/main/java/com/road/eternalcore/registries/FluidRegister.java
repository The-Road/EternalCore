package com.road.eternalcore.registries;

import com.road.eternalcore.Utils;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FluidRegister {
    private static final Map<String, RegistryObject<Fluid>> fluids = new HashMap<>();
    private final DeferredRegister<Fluid> fluidRegister = DeferredRegister.create(ForgeRegistries.FLUIDS, Utils.MOD_ID);
    public RegistryObject<Fluid> register(final String name, final Supplier<? extends Fluid> sup){
        RegistryObject<Fluid> fluid = fluidRegister.register(name, sup);
        FluidRegister.fluids.put(name, fluid);
        return fluid;
    }
    public void register(IEventBus bus){
        fluidRegister.register(bus);
    }
    public static Map<String, RegistryObject<Fluid>> getFluids(){
        return fluids;
    }
}
