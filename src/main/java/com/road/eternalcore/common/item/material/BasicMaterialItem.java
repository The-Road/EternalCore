package com.road.eternalcore.common.item.material;

import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.item.ModItem;
import com.road.eternalcore.common.item.group.ModGroup;
import com.road.eternalcore.data.tags.ModTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags;

public class BasicMaterialItem extends ModItem {
    // 基础材料类
    private final Materials material;
    private final MaterialShape shape;
    public BasicMaterialItem(Materials material, MaterialShape shape, Properties properties){
        super(properties.tab(ModGroup.materialGroup));
        this.material = material;
        this.shape = shape;
    }

    public Tags.IOptionalNamedTag<Item> getMaterialTag(){
        return ModTags.Items.getMaterialTag(shape, material);
    }

    public ITextComponent customItemName(ItemStack itemStack) {
        return new TranslationTextComponent(shape.getDescriptionId(), material.getText());
    }
}
