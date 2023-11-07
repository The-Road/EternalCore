package com.road.eternalcore.common.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;

public interface IFuelBurnTileEntity {
    // 燃烧燃料的TileEntity
    int getLitTime();
    int getLitDuration();
    void setLitTime(int litTime);
    void setLitDuration(int litDuration);
    default IIntArray createLitData(){
        return new IIntArray() {
            public int get(int i) {
                switch (i){
                    case 0:
                        return getLitTime();
                    case 1:
                        return getLitDuration();
                    default:
                        return 0;
                }
            }
            public void set(int i, int value) {
                switch (i){
                    case 0:
                        setLitTime(value);
                    case 1:
                        setLitDuration(value);
                }
            }
            public int getCount() {
                return 2;
            }
        };
    }
    default void saveLitNBT(CompoundNBT nbt){
        nbt.putInt("BurnTime", getLitTime());
        nbt.putInt("BurnDuration", getLitDuration());
    }
    default void loadLitNbt(CompoundNBT nbt){
        setLitTime(nbt.getInt("BurnTime"));
        setLitDuration(nbt.getInt("BurnDuration"));
    }
    default boolean isLit(){
        return getLitTime() > 0;
    }

    default void tickBurn(){
        int litTime = getLitTime();
        int litDuration = getLitDuration();
        if (litTime > 0){
            litTime--;
        }
        if (litTime <= 0 && canBurnNewFuel()){
            burnNewFuel();
        }
        setLitTime(litTime);
        setLitDuration(litDuration);
    }

    // 检查当前是否可以消耗新燃料
    boolean canBurnNewFuel();
    // 消耗新燃料
    void burnNewFuel();
}
