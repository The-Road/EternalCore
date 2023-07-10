package com.road.eternalcore.client.renderer.model.builder;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.road.eternalcore.common.util.ModResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.client.model.MultiLayerModel;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.LinkedHashMap;
import java.util.Map;

public class MachineModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
    // 仿照MultiLayerModelBuilder
    public static <T extends ModelBuilder<T>> MachineModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper)
    {
        return new MachineModelBuilder<>(parent, existingFileHelper);
    }
    private final Map<String, T> childModels = new LinkedHashMap<>();

    protected MachineModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(new ModResourceLocation("machine"), parent, existingFileHelper);
    }

    public MachineModelBuilder<T> submodel(RenderType layer, T modelBuilder)
    {
        Preconditions.checkNotNull(layer, "layer must not be null");
        Preconditions.checkArgument(MultiLayerModel.Loader.BLOCK_LAYERS.containsValue(layer), "layer must be supported by MultiLayerModel");
        Preconditions.checkNotNull(modelBuilder, "modelBuilder must not be null");
        childModels.put(MultiLayerModel.Loader.BLOCK_LAYERS.inverse().get(layer), modelBuilder);
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json)
    {
        json = super.toJson(json);

        JsonObject parts = new JsonObject();
        for(Map.Entry<String, T> entry : childModels.entrySet())
        {
            parts.add(entry.getKey(), entry.getValue().toJson());
        }
        json.add("layers", parts);

        return json;
    }
}
