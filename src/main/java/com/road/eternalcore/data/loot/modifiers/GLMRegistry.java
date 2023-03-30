package com.road.eternalcore.data.loot.modifiers;

import com.road.eternalcore.Utils;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GLMRegistry {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Utils.MOD_ID);
    public static final RegistryObject<HammerSmashModifier.Serializer> hammerSmash = GLM.register("hammer_smash", HammerSmashModifier.Serializer::new);
}
