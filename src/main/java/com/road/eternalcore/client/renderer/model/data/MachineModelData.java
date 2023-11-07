package com.road.eternalcore.client.renderer.model.data;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

public class MachineModelData extends ModelDataMap{
    // 机器模型的数据，存储了用来渲染机器外壳的BlockState
    public static final ModelProperty<BlockState> CasingProperty = new ModelProperty<>();
    public MachineModelData(Block block){
        this(block.defaultBlockState());
    }
    public MachineModelData(BlockState blockState) {
        super();
        setData(CasingProperty, blockState);
    }
}
