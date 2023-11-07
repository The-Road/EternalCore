package com.road.eternalcore.common.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;

public interface IRecipeProcessTileEntity {
    // 可以处理配方的机器

    int getWorkProgress();
    int getWorkTime();
    void setWorkProgress(int workProgress);
    void setWorkTime(int workTime);

    default IIntArray createWorkData(){
        return new IIntArray() {
            public int get(int i) {
                switch (i){
                    case 0:
                        return getWorkProgress();
                    case 1:
                        return getWorkTime();
                    default:
                        return 0;
                }
            }
            public void set(int i, int value) {
                switch (i){
                    case 0:
                        setWorkProgress(value);
                    case 1:
                        setWorkTime(value);
                }
            }
            public int getCount() {
                return 2;
            }
        };
    }
    default void saveWorkNBT(CompoundNBT nbt){
        nbt.putInt("WorkProgress", getWorkProgress());
        nbt.putInt("WorkTime", getWorkTime());
    }
    default void loadWorkNbt(CompoundNBT nbt){
        setWorkProgress(nbt.getInt("WorkProgress"));
        setWorkTime(nbt.getInt("WorkTime"));
    }

    default boolean isWorking(){
        return getWorkTime() > 0;
    }
    // 工作检测，返回是否需要setChanged()
    default boolean tickWork(){
        int workProgress = getWorkProgress();
        int workTime = getWorkTime();
        boolean blockChanged = false;
        if (workTime > 0) {
            workProgress++;
            if (workProgress >= workTime) {
                getWorkResult();
                workProgress = 0;
                workTime = 0;
            }
        }
        if (workTime == 0){
            blockChanged = tryNextWork();
        }
        setWorkProgress(workProgress);
        setWorkTime(workTime);
        return blockChanged;
    }
    // 获得配方产品
    void getWorkResult();
    // 检查是否可以进行下一次工作，如果开始下一次工作则返回true
    boolean tryNextWork();
}
