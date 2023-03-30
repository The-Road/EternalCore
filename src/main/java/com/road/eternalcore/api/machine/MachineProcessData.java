package com.road.eternalcore.api.machine;

public class MachineProcessData {
    // 机器处理数据
    private final int voltage;
    private final int current;
    private final int time;
    public MachineProcessData(int voltage, int current, int time){
        this.voltage = voltage;
        this.current = current;
        this.time = time;
    }
    public int getVoltage(){
        return voltage;
    }
    public int getCurrent() {
        return current;
    }
    public int getTime() {
        return time;
    }
}
