package com.road.eternalcore.common.tileentity.data;

import com.road.eternalcore.common.tileentity.MachineTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

import java.util.HashMap;
import java.util.Map;

public class MachineTileEntityCoverData {
    private Map<Direction, MachineCover> covers = new HashMap<>();
    private Direction face;
    protected MachineTileEntity tileEntity;

    public MachineTileEntityCoverData(MachineTileEntity tileEntity){
        this.tileEntity = tileEntity;
    }
    public MachineCover getCover(Direction direction){
        return covers.getOrDefault(direction, MachineCover.NULL);
    }
    public void setCover(Direction direction, MachineCover cover){
        if (getCover(direction) != MachineCover.NULL){
            // todo: 掉落原有的覆盖板

        }
        if (direction != getFace()) {
            covers.put(direction, cover);
        }
    }
    public Direction getFace(){
        if (face == null){
            syncFace();
        }
        return face;
    }
    // 同步BlockState和TileEntity的正面状态
    public void syncFace(){
        Direction face = null;
        DirectionProperty property = null;
        BlockState blockState = tileEntity.getBlockState();
        if (blockState.hasProperty(BlockStateProperties.FACING)){
            face = blockState.getValue(BlockStateProperties.FACING);
            property = BlockStateProperties.FACING;
        } else if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)){
            face = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            property = BlockStateProperties.HORIZONTAL_FACING;
        }
        if (face != null) {
            if (this.face != null) {
                blockState.setValue(property, this.face);
            } else {
                this.face = face;
                setCover(face, MachineCover.NULL);
            }
        }
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
