package com.road.eternalcore.common.tileentity.data;

import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;

import java.util.HashMap;
import java.util.Map;

public class MachineTileEntityCoverData {
    private Map<Direction, MachineCover> covers = new HashMap<>();

    public MachineTileEntityCoverData(){}
    public MachineCover getCover(Direction direction){
        return covers.getOrDefault(direction, MachineCover.NULL);
    }
    public void setCover(Direction direction, MachineCover cover){
        covers.put(direction, cover);
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
