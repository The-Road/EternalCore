package com.road.eternalcore.data.tags;

import com.road.eternalcore.api.ore.Ores;
import com.road.eternalcore.common.block.ore.OreBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;

import static com.road.eternalcore.data.tags.ModTags.Blocks.ORES;

public class ModBlockTagsProvider extends ForgeBlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator gen, ExistingFileHelper existingFileHelper){
        super(gen, existingFileHelper);
    }
    @Override
    public void addTags(){
        addOreTags();
    }
    private void addOreTags(){
        for (Ores ore : Ores.getAllOres()){
            Tags.IOptionalNamedTag<Block> oreTag = ModTags.Blocks.getOreTag(ore);
            tag(ORES).addTag(oreTag);
            tag(oreTag).add(OreBlocks.get(ore));
        }
    }
}
