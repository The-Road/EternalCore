package com.road.eternalcore.common.tileentity.data;

import com.road.eternalcore.common.tileentity.MachineTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

import java.util.HashMap;
import java.util.Map;

public class MachineTileEntityCoverData {
    private Map<Direction, MachineCover> covers = new HashMap<>();
    private final MachineTileEntity tileEntity;

    public MachineTileEntityCoverData(MachineTileEntity tileEntity){
        this.tileEntity = tileEntity;
    }
    public MachineCover getCover(Direction direction){
        return covers.getOrDefault(direction, MachineCover.NULL);
    }
    public void setCover(Direction direction, MachineCover cover){
        if (direction != checkFace()){
            covers.put(direction, cover);
        }
    }
    private Direction checkFace(){
        // 返回机器正面的朝向
        BlockState blockState = tileEntity.getBlockState();
        Direction facing = null;
        if (blockState.getProperties().contains(BlockStateProperties.FACING)){
            facing = blockState.getValue(BlockStateProperties.FACING);
        } else if (blockState.getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)){
            facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        }
        if (facing != null) {
            if (getCover(facing) != MachineCover.FACE){
                for (Map.Entry<Direction, MachineCover> entry : covers.entrySet()){
                    if (entry.getValue() == MachineCover.FACE){
                        covers.remove(entry.getKey());
                    }
                }
                covers.put(facing, MachineCover.FACE);
            }
        }
        return facing;
    }

    public void load(ListNBT listNBT){
        for (int i=0; i<6; i++){
            String str = listNBT.getString(i);
            if (!str.isEmpty()) {
                covers.put(Direction.from3DDataValue(i), MachineCover.get(str));
            }
        }
    }
    public ListNBT save(){
        ListNBT listNBT = new ListNBT();
        for (int i=0; i<6; i++){
            listNBT.add(StringNBT.valueOf(getCover(Direction.from3DDataValue(i)).getName()));
        }
        return listNBT;
    }
}
