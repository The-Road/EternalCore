package com.road.eternalcore.common.util;

import com.road.eternalcore.Utils;
import net.minecraft.util.ResourceLocation;

public class ModResourceLocation extends ResourceLocation {
    public ModResourceLocation(String name){
        super(Utils.MOD_ID, name);
    }
}
