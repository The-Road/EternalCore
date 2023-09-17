package com.road.eternalcore.api.tool;

import com.road.eternalcore.common.item.ModItems;
import com.road.eternalcore.common.item.tool.ModToolType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.ToolType;

import java.util.*;

public class CraftToolType {
    public static final Map<String, CraftToolType> typeMap = new HashMap<>();

    public static final CraftToolType HAMMER = new CraftToolType("hammer", ModItems.hammer, ModToolType.HAMMER);
    public static final CraftToolType WRENCH = new CraftToolType("wrench", ModItems.wrench, ModToolType.WRENCH);
    private final String name;
    private final List<ToolType> list = new ArrayList<>();
    private final IItemProvider item;
    public CraftToolType(String name, IItemProvider item, ToolType... types) {
        if (typeMap.containsKey(name)){
            throw new IllegalArgumentException("Duplicate craft tool type: " + name);
        }else{
            typeMap.put(name, this);
        }
        this.name = name;
        this.item = item;
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
    public IItemProvider getItem(){
        return item;
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
