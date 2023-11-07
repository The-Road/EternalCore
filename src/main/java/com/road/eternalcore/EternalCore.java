package com.road.eternalcore;

import com.road.eternalcore.common.fluid.ModFluids;
import com.road.eternalcore.registries.ModRegistries;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.common.Mod;

@Mod(Utils.MOD_ID)
public class EternalCore {
    public EternalCore(){
        // 注册相关
        ModRegistries.register();
    }
}
