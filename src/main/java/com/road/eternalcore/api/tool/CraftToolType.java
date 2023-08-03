package com.road.eternalcore.api.tool;

import com.road.eternalcore.common.item.tool.ModToolType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

import java.util.*;

public class CraftToolType {
    public static final Map<String, CraftToolType> typeMap = new HashMap<>();

    public static final CraftToolType HAMMER = new CraftToolType("hammer", ModToolType.HAMMER);
    public static final CraftToolType WRENCH = new CraftToolType("wrench", ModToolType.WRENCH);
    private final String name;
    private final List<ToolType> list = new ArrayList<>();
    public CraftToolType(String name, ToolType... types) {
        if (typeMap.containsKey(name)){
            throw new IllegalArgumentException("Duplicate craft tool type: " + name);
        }else{
            typeMap.put(name, this);
        }
        this.name = name;
        add(types);
    }
    public static CraftToolType get(String name){
        return typeMap.get(name);
    }

    public void add(ToolType... types){
        list.addAll(Arrays.asList(types));
    }
    public String getName(){
        return name;
    }
    public List<ToolType> getList(){
        return list;
    }
    public boolean matchType(ToolType type){
        return list.contains(type);
    }
    public boolean match(ItemStack itemStack){
        return itemStack.getToolTypes().stream().anyMatch(this::matchType);
    }
}
