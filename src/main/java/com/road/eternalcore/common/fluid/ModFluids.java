package com.road.eternalcore.common.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraftforge.common.ForgeMod;

public class ModFluids {
    public static Fluid WATER = Fluids.WATER;
    public static Fluid LAVA = Fluids.LAVA;
    public static Fluid MILK = ForgeMod.MILK.get();
    public static Fluid STEAM = ModFluidRegistries.steam.get();
}
