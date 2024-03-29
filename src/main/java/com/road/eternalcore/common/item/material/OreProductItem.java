package com.road.eternalcore.common.item.material;

import com.road.eternalcore.api.ore.OreShape;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.common.item.ModItem;
import com.road.eternalcore.common.item.group.ModGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class OreProductItem extends ModItem {
    private final Ores ore;
    private final OreShape shape;
    public OreProductItem(Ores ore, OreShape shape){
        super(new Item.Properties().tab(ModGroup.materialGroup));
        this.ore = ore;
        this.shape = shape;
    }
    public ITextComponent customItemName(ItemStack itemStack) {
        return new TranslationTextComponent(shape.getDescriptionId(), ore.getMainProduct().getText());
    }
}
