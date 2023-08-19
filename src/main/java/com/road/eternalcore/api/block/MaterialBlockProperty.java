package com.road.eternalcore.api.block;

import com.google.common.collect.ImmutableSet;
import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.api.material.Materials;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MaterialBlockProperty extends Property<String> {
    public static final String DefaultValue = "null";
    private final ImmutableSet<String> values;
    protected MaterialBlockProperty() {
        super("material", String.class);
        values = ImmutableSet.copyOf(getAllMaterials());
    }
    private Collection<String> getAllMaterials(){
        // 读取所有的MaterialBlockData对应的String
        List<String> allValues = new ArrayList<>();
        allValues.add(DefaultValue);
        allValues.addAll(MaterialBlockData.getData().keySet().stream().map(Materials::getName).collect(Collectors.toList()));
        return allValues;
    }

    public Collection<String> getPossibleValues() {
        return values;
    }

    public String getName(String value) {
        return value;
    }

    public Optional<String> getValue(String value) {
        return Optional.of(values.contains(value) ? value : DefaultValue);
    }

    public int generateHashCode() {
        return 31 * super.generateHashCode() + values.hashCode();
    }

    public static MaterialBlockProperty create(){
        return new MaterialBlockProperty();
    }
    public BlockState setBlockStateProperty(BlockState blockState, Materials material){
        String materialName = getValue(material.getName()).get();
        if (!values.contains(materialName)){
            materialName = DefaultValue;
        }
        return blockState.setValue(this, materialName);
    }
}
