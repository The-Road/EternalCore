package com.road.eternalcore.common.item;

import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.item.material.MaterialItems;
import com.road.eternalcore.common.item.tool.ToolItems;
import com.road.eternalcore.registries.ItemRegister;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;
import java.util.stream.Collectors;

public class ModItems {
    public static final Item stoneDust = MaterialItems.get(MaterialShape.DUST, Materials.STONE);
    // 工具
    public static final Item debugTool = ToolItems.debugTool.get();
    public static final Item pickaxe = ToolItems.pickaxe.get();
    public static final Item hammer = ToolItems.hammer.get();
    public static final Item softHammer = ToolItems.softHammer.get();
    public static final Item wrench = ToolItems.wrench.get();

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
