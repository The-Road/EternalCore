package com.road.eternalcore.common.fluid;

import com.road.eternalcore.common.util.ModResourceLocation;
import com.road.eternalcore.registries.FluidRegister;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModFluidRegistries {
    public static final FluidRegister FLUIDS = new FluidRegister();
    public static final RegistryObject<Fluid> steam = register("steam", builder ->
            builder.gaseous().temperature(450));

    public static RegistryObject<Fluid> register(String name, Consumer<FluidAttributes.Builder> consumer){
        FluidAttributes.Builder builder = FluidAttributes.builder(
                new ModResourceLocation("fluid/" + name),
                new ModResourceLocation("fluid/" + name)
        );
        consumer.accept(builder);
        PropertiesSupplier properties = new PropertiesSupplier();
        RegistryObject<Fluid> source = FLUIDS.register(name, () -> new ModFluid.Source(properties.get()));
        RegistryObject<Fluid> flowing = FLUIDS.register("flowing_" + name, () -> new ModFluid.Flowing(properties.get()));
        properties.set(new ForgeFlowingFluid.Properties(source, flowing, builder));
        return source;
    }

    public static void register(IEventBus bus){
        FLUIDS.register(bus);
    }

    private static class PropertiesSupplier implements Supplier<ForgeFlowingFluid.Properties> {
        private ForgeFlowingFluid.Properties properties;
        private void set(ForgeFlowingFluid.Properties properties){
            this.properties = properties;
        }
        public ForgeFlowingFluid.Properties get() {
            return properties;
        }
    }
}
