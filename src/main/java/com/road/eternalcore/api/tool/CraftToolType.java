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
    public static final CraftToolType FILE = new CraftToolType("file", ModItems.file, ModToolType.FILE);
    public static final CraftToolType WIRE_CUTTER = new CraftToolType("wire_cutter", ModItems.wireCutter, ModToolType.WIRE_CUTTER);
    public static final CraftToolType SCREWDRIVER = new CraftToolType("screwdriver", ModItems.screwdriver, ModToolType.SCREWDRIVER);
    private final String name;
    private final List<ToolType> toolTypes = new ArrayList<>();
    private final IItemProvider iconItem;
    public CraftToolType(String name, IItemProvider iconItem, ToolType... types) {
        if (typeMap.containsKey(name)){
            throw new IllegalArgumentException("Duplicate craft tool type: " + name);
        }else{
            typeMap.put(name, this);
        }
        this.name = name;
        this.iconItem = iconItem;
        add(types);
    }
    public static CraftToolType get(String name){
        return typeMap.get(name);
    }
    public static boolean isCraftTool(ItemStack itemStack){
        return typeMap.values().stream().anyMatch(type -> type.match(itemStack));
    }

    public void add(ToolType... types){
        toolTypes.addAll(Arrays.asList(types));
    }
    public String getName(){
        return name;
    }
    public IItemProvider getIconItem(){
        return iconItem;
    }
    public List<ToolType> getToolTypes(){
        return toolTypes;
    }
    public boolean matchType(ToolType type){
        return toolTypes.contains(type);
    }
    public boolean match(ItemStack itemStack){
        return itemStack.getToolTypes().stream().anyMatch(this::matchType);
    }
}
