package com.road.eternalcore.api;

import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;

public class RGB {
    private int red;
    private int green;
    private int blue;
    public RGB(int r, int g, int b){
        this.red = Math.max(0, Math.min(255, r));
        this.green = Math.max(0, Math.min(255, g));
        this.blue = Math.max(0, Math.min(255, b));
    }
    public RGB(int colorValue){
        this(colorValue >> 16 & 255, colorValue >> 8 & 255, colorValue & 255);
    }
    public RGB(TextFormatting textFormatting){
        this(textFormatting.getColor());
    }

    public RGB transit(RGB other, double rate){
        return new RGB(
                (int)(this.red * (1-rate) + other.red * rate),
                (int)(this.green * (1-rate) + other.green * rate),
                (int)(this.blue * (1-rate) + other.blue * rate)
        );
    }

    public int getColorValue(){
        return (this.red << 16) + (this.green << 8) + this.blue;
    }
    public Color getColor(){
        return Color.fromRgb(getColorValue());
    }
}
