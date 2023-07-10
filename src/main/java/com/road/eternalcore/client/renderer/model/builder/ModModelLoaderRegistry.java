package com.road.eternalcore.client.renderer.model.builder;

import com.road.eternalcore.client.renderer.model.MachineModel;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class ModModelLoaderRegistry extends ModelLoaderRegistry {
    public static void init(){
        registerLoader(new ModResourceLocation("machine"), MachineModel.Loader.INSTANCE);
    }
}
