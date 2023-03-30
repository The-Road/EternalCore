package com.road.eternalcore.common.util;

import com.road.eternalcore.Utils;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSoundEventsRegistries {
    public static final DeferredRegister<SoundEvent> SOUND = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Utils.MOD_ID);
    public static final RegistryObject<SoundEvent> locker_open = registerMachine("locker.open");
    public static final RegistryObject<SoundEvent> locker_close = registerMachine("locker.close");

    public static RegistryObject<SoundEvent> register(String name){
        return SOUND.register(name, () -> new SoundEvent(new ModResourceLocation(name)));
    }public static RegistryObject<SoundEvent> registerMachine(String name){
        return register("machine." + name);
    }
}
