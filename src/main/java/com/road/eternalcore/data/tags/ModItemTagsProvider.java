package com.road.eternalcore.data.tags;

import com.road.eternalcore.Utils;
import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.api.ore.OreShape;
import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.common.item.material.MaterialItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper){
        super(gen, blockTagProvider, Utils.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags(){
        addMaterialTags();
        addOreTags();
    }
    private void addMaterialTags(){
        // 通过MaterialShape和Materials批量添加标签
        for(Materials material : Materials.getAllMaterials()){
            for(MaterialShape shape : material.getShapes()) {
                Item item = MaterialItems.get(shape, material);
                if (item != null) {
                    Tags.IOptionalNamedTag<Item> itemTag = ModTags.Items.getMaterialTag(shape, material);
                    tag(ModTags.Items.getMaterialShapeTag(shape)).addTag(itemTag);
                    tag(itemTag).add(item);
                }
            }
        }
    }
    private void addOreTags(){
        for (OreShape shape : OreShape.getAllShapes()){
            Tags.IOptionalNamedTag<Item> shapeTag = ModTags.Items.getOreShapeTag(shape);
            for (Ores ore : Ores.getAllOres()){
                Item item = MaterialItems.get(shape, ore);
                if (item != null) {
                    Tags.IOptionalNamedTag<Item> itemTag = ModTags.Items.getOreTag(shape, ore);
                    tag(shapeTag).addTag(itemTag);
                    if (shape == OreShape.ORE) {
                        copy(ModTags.Blocks.getOreTag(ore), itemTag);
                    } else {
                        tag(itemTag).add(item);
                    }
                }
            }
        }
    }
}
