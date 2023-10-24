package com.road.eternalcore.api.ore;

import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.MaterialSmeltData;
import com.road.eternalcore.api.material.Materials;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.LazyValue;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.road.eternalcore.api.material.Materials.*;

public class Ores {
    // 管理矿石种类的
    protected static final Map<Materials, Ores> ores = new LinkedHashMap<>();
    // 矿石名称即主产物名称，所以混合矿石需要在Materials里添加对应的材料（粉末类）

    // 原版矿石无需设置blockProperties，因为不会注册新方块
    public static final Ores COAL_ORE = addSimple(COAL);
    public static final Ores IRON_ORE = addSimple(IRON);
    public static final Ores GOLD_ORE = addSimple(GOLD);
    public static final Ores DIAMOND_ORE = addSimple(DIAMOND);
    public static final Ores REDSTONE_ORE = addSimple(REDSTONE);
    public static final Ores LAPIS_ORE = addSimple(LAPIS);
    public static final Ores COPPER_ORE = addSimple(COPPER, COBALT)
            .setBlockProperties(() -> defaultBlockProperties());
    public static final Ores TIN_ORE = addSimple(TIN)
            .setBlockProperties(() -> defaultBlockProperties());
    protected int productNum = 1;
    protected LazyValue<AbstractBlock.Properties> blockProperties;

    protected final Materials mainProduct;
    protected final OreByProducts products;
    public Ores(Materials mainProduct, OreByProducts products){
        if (ores.containsKey(mainProduct)){
            throw new IllegalArgumentException("Ore "+mainProduct.getName()+" has already existed!");
        }
        this.products = products;
        this.mainProduct = mainProduct;
        ores.put(mainProduct, this);
    }
    public static String getRegisterName(OreShape shape, Ores ore){
        return String.format(shape.registerName, ore.getName());
    }
    public static Ores get(Materials mainProduct){
        if (ores.containsKey(mainProduct)){
            return ores.get(mainProduct);
        }
        return null;
    }
    public static Collection<Ores> getAllOres(){
        return ores.values();
    }
    protected static Ores addSimple(Materials mainProduct){
        return addSimple(mainProduct, new OreByProducts(mainProduct));
    }
    protected static Ores addSimple(Materials mainProduct, Materials... byProducts){
        return addSimple(mainProduct, new OreByProducts(byProducts));
    }
    protected static Ores addSimple(Materials mainProduct, OreByProducts byProducts){
        Ores ore = new Ores(mainProduct, byProducts);
        return ore;
    }
    protected static AbstractBlock.Properties defaultBlockProperties(){
        // 原版矿都是(3.0, 3.0)
        return AbstractBlock.Properties.of(Material.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 3.0F)
                .harvestLevel(1);
    }

    public Materials getMainProduct() {
        return mainProduct;
    }
    public String getName(){
        return mainProduct.getName();
    }
    protected Ores setBlockProperties(Supplier<AbstractBlock.Properties> properties){
        this.blockProperties = new LazyValue<>(properties);
        return this;
    }
    public AbstractBlock.Properties getBlockProperties(){
        if (blockProperties != null) {
            return blockProperties.get();
        }
        return Ores.defaultBlockProperties();
    }
    public boolean canSmeltInFurnace(){
        return mainProduct.hasShape(MaterialShape.INGOT) && MaterialSmeltData.get(mainProduct) != null;
    }

    protected static class OreByProducts {
        public final Materials firstByProduct;
        public final Materials secondByProduct;
        public final Materials thirdByProduct;
        protected OreByProducts(Materials byProduct){
            this(byProduct, byProduct);
        }
        protected OreByProducts(Materials firstByProduct, Materials secondByProduct){
            this(firstByProduct, secondByProduct, secondByProduct);
        }
        protected OreByProducts(Materials firstByProduct, Materials secondByProduct, Materials thirdByProduct){
            this.firstByProduct = firstByProduct;
            this.secondByProduct = secondByProduct;
            this.thirdByProduct = thirdByProduct;
        }
        protected OreByProducts(Materials... byProducts){
            this.firstByProduct = byProducts[0];
            this.secondByProduct = byProducts.length > 1 ? byProducts[1] : firstByProduct;
            this.thirdByProduct = byProducts.length > 2 ? byProducts[2] : secondByProduct;
        }
    }
}
