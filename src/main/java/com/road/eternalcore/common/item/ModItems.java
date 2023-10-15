package com.road.eternalcore.common.item;

import com.road.eternalcore.common.item.tool.ToolItems;
import com.road.eternalcore.registries.ItemRegister;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;
import java.util.stream.Collectors;

public class ModItems {
    // 工具
    public static final Item debugTool = ToolItems.debugTool.get();
    public static final Item pickaxe = ToolItems.pickaxe.get();
    public static final Item axe = ToolItems.axe.get();
    public static final Item shovel = ToolItems.shovel.get();
    public static final Item hoe = ToolItems.hoe.get();
    public static final Item sword = ToolItems.sword.get();
    public static final Item knife = ToolItems.knife.get();
    public static final Item hammer = ToolItems.hammer.get();
    public static final Item softMallet = ToolItems.softMallet.get();
    public static final Item wrench = ToolItems.wrench.get();
    public static final Item file = ToolItems.file.get();
    public static final Item wireCutter = ToolItems.wireCutter.get();
    public static final Item screwdriver = ToolItems.screwdriver.get();
    public static final Item crowbar = ToolItems.crowbar.get();
    public static final Item saw = ToolItems.saw.get();

    public static Collection<Item> getAll(){
        return ItemRegister.getItems().values().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
    public static Item get(String name){
        RegistryObject<Item> item = ItemRegister.getItems().get(name);
        if (item != null){
            return item.get();
        } else {
            return null;
        }
    }
}
