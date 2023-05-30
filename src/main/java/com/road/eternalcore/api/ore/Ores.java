package com.road.eternalcore.api.ore;

import com.road.eternalcore.api.material.MaterialSmeltData;
import com.road.eternalcore.api.material.Materials;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.LazyValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.road.eternalcore.api.material.Materials.*;

public class Ores {
    // 管理矿石种类的
    protected static final Map<Materials, Ores> ores = new HashMap<>();
    // 矿石名称即主产物名称，所以混合矿石需要在Materials里添加对应的材料（粉末类）
    public static final Ores COPPER_ORE = addSimple(COPPER,
            new OreByProducts(COBALT))
            .setBlockProperties(() -> defaultBlockProperties());
    public static final Ores TIN_ORE = addSimple(TIN,
            new OreByProducts(TIN))
            .setBlockProperties(() -> defaultBlockProperties());
    protected Materials smeltResult = null;
    protected int productNum = 1;
    protected LazyValue<AbstractBlock.Properties> blockProperties;

    protected final Materials mainProduct;
    protected final OreByProducts products;
    public Ores(Materials mainProduct, OreByProducts products){
        if (ores.containsKey(mainProduct)){
            throw new IllegalStateException("Ore "+mainProduct.getName()+" has already existed!");
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
    protected static Ores addSimple(Materials mainProduct, OreByProducts products){
        Ores ore = new Ores(mainProduct, products);
        if (mainProduct.getType() == Type.SOLID) {
            if (MaterialSmeltData.get(mainProduct).getBlastFurnaceData() == null) {
                ore.smeltResult = mainProduct;
            }
        }
        return ore;
    }
    protected static AbstractBlock.Properties defaultBlockProperties(){
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
    public Materials getSmeltResult(){
        return smeltResult;
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
    }
}
