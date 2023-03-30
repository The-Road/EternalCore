package com.road.eternalcore.api.machine;

public class BlastFurnaceProcessData extends MachineProcessData{
    private final int temperature;
    public BlastFurnaceProcessData(int voltage, int current, int time, int temperature) {
        super(voltage, current, time);
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }
}
