package com.road.eternalcore.common.tileentity.data;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class MachineCover {
    private static final Map<String, MachineCover> machineCover = new HashMap<>();
    // 处理覆盖板数据的类
    public static final MachineCover NULL = new MachineCover(""); // 无覆盖板
    public static final MachineCover FACE = new MachineCover("face"); // 机器正面

    private final String name;
    private final Item item;
    public MachineCover(String name){
        this(name, Items.AIR);
    }
    public MachineCover(String name, Item item){
        if (machineCover.containsKey(name)){
            throw new IllegalStateException("MachineCover "+name+" has already existed!");
        }
        this.name = name;
        this.item = item;
        machineCover.put(name, this);
    }
    public static MachineCover get(String name){
        return machineCover.getOrDefault(name, NULL);
    }

    public String getName(){
        return name;
    }
    public ItemStack getItemStack(){
        return new ItemStack(item);
    }
}
