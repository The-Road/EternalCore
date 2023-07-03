package com.road.eternalcore.client.renderer.model.data;

import com.road.eternalcore.api.material.Materials;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

public class MachineModelData extends ModelDataMap{
    public static final ModelProperty<Materials> MaterialProperty = new ModelProperty<>();
    public MachineModelData(Materials material){
        super();
        setData(MaterialProperty, material);
    }
}
