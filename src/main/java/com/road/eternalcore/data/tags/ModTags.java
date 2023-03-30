package com.road.eternalcore.data.tags;

import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.api.ore.OreShape;
import com.road.eternalcore.api.ore.Ores;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.HashMap;
import java.util.Map;

public class ModTags extends Tags {
    public static class Blocks extends Tags.Blocks {
        private static void init(){
            createOreTags();
        }
        private static final Map<Ores, IOptionalNamedTag<Block>> OreTagsMap = new HashMap<>();
        private static void createOreTags(){
            for(Ores ore : Ores.getAllOres()){
                OreTagsMap.put(ore, forgeTag("ores/" + ore.getName()));
            }
        }
        public static IOptionalNamedTag<Block> getOreTag(Ores ore){
            return OreTagsMap.get(ore);
        }

        private static IOptionalNamedTag<Block> forgeTag(String name)
        {
            return BlockTags.createOptional(new ResourceLocation("forge", name));
        }
        static{init();}
    }
    public static class Items extends Tags.Items{
        private static void init(){
            createMaterialTags();
            createOreTags();
        }
        // 用于获取标签的Map
        private static final Map<MaterialShape, IOptionalNamedTag<Item>> MaterialShapeTagsMap = new HashMap<>();
        private static final Map<MaterialShape, Map<Materials, IOptionalNamedTag<Item>>> MaterialTagsMap = new HashMap<>();
        private static final Map<OreShape, IOptionalNamedTag<Item>> OreShapeTagsMap = new HashMap<>();
        private static final Map<OreShape, Map<Ores, IOptionalNamedTag<Item>>> OreTagsMap = new HashMap<>();

        private static String addS(String str){
            if (str.charAt(str.length() - 1) != 's'){
                return str + "s";
            }else{
                return str;
            }
        }
        private static void createMaterialTags(){
            // 通过MaterialShape和Materials批量注册标签
            // MINERAL类型的材料标签为材料本身
            for(MaterialShape shape : MaterialShape.getAllShapes()){
                String shapeName = addS(shape.getName());
                MaterialShapeTagsMap.put(shape, forgeTag(shapeName));
                MaterialTagsMap.put(shape, new HashMap<>());
            }
            for(Materials material : Materials.getAllMaterials()) {
                String materialName = material.getName();
                for (MaterialShape shape : material.getShapes()) {
                    if (shape == MaterialShape.MINERAL){
                        MaterialTagsMap.get(shape).put(material, forgeTag(materialName));
                    }else {
                        String shapeName = addS(shape.getName());
                        MaterialTagsMap.get(shape).put(material, forgeTag(shapeName + "/" + materialName));
                    }
                }
            }
        }
        public static IOptionalNamedTag<Item> getMaterialShapeTag(MaterialShape shape){
            return MaterialShapeTagsMap.get(shape);
        }
        public static IOptionalNamedTag<Item> getMaterialTag(MaterialShape shape, Materials material){
            return MaterialTagsMap.get(shape).get(material);
        }
        private static void createOreTags(){
            for (OreShape shape : OreShape.getAllShapes()){
                String shapeName = addS(shape.getName());
                OreShapeTagsMap.put(shape, forgeTag(shapeName));
                OreTagsMap.put(shape, new HashMap<>());
                for (Ores ore : Ores.getAllOres()){
                    String oreName = ore.getName();
                    OreTagsMap.get(shape).put(ore, forgeTag(shapeName + "/" + oreName));
                }
            }
        }
        public static IOptionalNamedTag<Item> getOreShapeTag(OreShape shape){
            return OreShapeTagsMap.get(shape);
        }
        public static IOptionalNamedTag<Item> getOreTag(OreShape shape, Ores ore){
            return OreTagsMap.get(shape).get(ore);
        }

        private static IOptionalNamedTag<Item> forgeTag(String name) {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }
        static{init();}
    }
    public static class Fluids extends Tags.Fluids {
        private static void init(){}
        static{init();}
    }
}
