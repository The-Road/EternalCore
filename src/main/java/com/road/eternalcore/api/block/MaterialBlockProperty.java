package com.road.eternalcore.api.block;

import com.google.common.collect.ImmutableSet;
import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.api.material.Materials;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;

import java.util.*;
import java.util.stream.Collectors;

public class MaterialBlockProperty extends Property<StringConstant> {
    public static final String DEFAULT = "null";
    private final ImmutableSet<StringConstant> values;
    protected MaterialBlockProperty() {
        super("material", StringConstant.class);
        values = ImmutableSet.copyOf(getAllMaterials());
    }
    private Collection<StringConstant> getAllMaterials(){
        // 读取所有的MaterialBlockData对应的String
        List<String> allValues = new ArrayList<>();
        allValues.add(DEFAULT);
        allValues.addAll(MaterialBlockData.getData().keySet().stream().map(Materials::getName).collect(Collectors.toList()));
        return allValues.stream().map(StringConstant::of).collect(Collectors.toList());
    }

    public Collection<StringConstant> getPossibleValues() {
        return values;
    }

    public String getName(StringConstant constant) {
        return constant.value();
    }

    public Optional<StringConstant> getValue(String value) {
        StringConstant constant = StringConstant.of(value);
        return Optional.of(values.contains(constant) ? constant : StringConstant.of(DEFAULT));
    }

    public int generateHashCode() {
        return 31 * super.generateHashCode() + values.hashCode();
    }

    public static MaterialBlockProperty create(){
        return new MaterialBlockProperty();
    }
    public BlockState setBlockStateProperty(BlockState blockState, Materials material){
        StringConstant materialName = getValue(material.getName()).get();
        if (!values.contains(materialName)){
            materialName = StringConstant.of(DEFAULT);
        }
        return blockState.setValue(this, materialName);
    }
}
