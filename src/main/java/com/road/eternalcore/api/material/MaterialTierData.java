package com.road.eternalcore.api.material;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraftforge.common.Tags;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MaterialTierData {
    // 记录材料的工具和锻造等级
    protected static final Map<Materials, MaterialTierData> materialTierData = new HashMap<>();
    public static final MaterialTierData NULL = new MaterialTierData(Materials.NULL).tier(
            2, 256, 6.0F, 2.0F, 14
    );
    public static final MaterialTierData IRON = setData(Materials.IRON).tier(
            2, 256, 6.0F, 2.0F, 14
    ).smith(2);
    public static final MaterialTierData COPPER = setData(Materials.COPPER).tier(
            1, 160, 5.0F, 1.5F, 18
    ).smith(1);
    public static final MaterialTierData GOLD = setData(Materials.GOLD).tier(
            0, 32, 12.0F, 0.0F, 22
    ).smith(1);
    public static final MaterialTierData BRONZE = setData(Materials.BRONZE).tier(
            2, 192, 6.0F, 2.0F, 14
    ).smith(1);
    public static final MaterialTierData NETHERITE = setData(Materials.NETHERITE).tier(
            4, 2560, 10.0F, 4.0F, 15
    ).smith(6);
    public static final MaterialTierData DIAMOND = setData(Materials.DIAMOND).tier(
            3, 1560, 8.0F, 3.0F, 10
    );
    public static final MaterialTierData WOOD = setData(Materials.WOOD).tier(
            0, 64, 2.0F, 0.0F, 15
    ).soft().byHand();
    public static final MaterialTierData STONE = setData(Materials.STONE).tier(
            1, 128, 4.0F, 1.0F, 5
    ).byHand();

    protected Materials material;
    protected ItemTier itemTier = null; // 工具等级，存储挖掘等级、耐久、基础挖掘速度、基础攻击力、附魔强度和基础材料
    protected int smithLevel = 0; // 锻造等级，在锻造台上加工零件至少需要上一级的锻造台和工具
    protected boolean soft = false; // 用于判断制作出来的锤子是锻造锤还是软锤
    protected boolean byHand = false; // 是否可以徒手制作（使用普通工作台配方而非工具合成配方，同时部分工具/零件不支持此类材料）
    private MaterialTierData(Materials material){
        this.material = material;
    }
    protected static MaterialTierData setData(Materials material){
        if (materialTierData.containsKey(material)){
            throw new IllegalStateException("MaterialTierData "+material+" has already existed!");
        }
        MaterialTierData data = new MaterialTierData(material);
        materialTierData.put(material, data);
        return data;
    }
    public static Map<Materials, MaterialTierData> getData(){
        return materialTierData;
    }
    public static MaterialTierData get(String name){
        return materialTierData.getOrDefault(Materials.get(name), NULL);
    }
    public static MaterialTierData get(Materials material){
       return materialTierData.getOrDefault(material, NULL);
    }
    public Materials getMaterial(){
        return material;
    }
    public ItemTier getItemTier() {
        return itemTier;
    }
    public int getSmithLevel() {
        return smithLevel;
    }
    public boolean isSoft() {
        return soft;
    }
    public boolean isByHand() {
        return byHand;
    }

    protected MaterialTierData tier(int level, int uses, float speed, float damage, int enchant){
        this.itemTier = new ItemTier(level, uses, speed, damage, enchant, () -> Ingredient.of(material.getIngredientTag()));
        return this;
    }
    protected MaterialTierData tier(int level, int uses, float speed, float damage, int enchant, Supplier<Ingredient> rodIngredient){
        this.itemTier = new ItemTier(level, uses, speed, damage, enchant, () -> Ingredient.of(material.getIngredientTag()), rodIngredient);
        return this;
    }
    protected MaterialTierData smith(int smithLevel){
        this.smithLevel = smithLevel;
        return this;
    }
    protected MaterialTierData soft(){
        this.soft = true;
        return this;
    }
    protected MaterialTierData byHand(){
        this.byHand = true;
        return this;
    }
    public static class ItemTier implements IItemTier {
        private final int level;
        private final int uses;
        private final float speed;
        private final float damage;
        private final int enchantmentValue;
        private final LazyValue<Ingredient> ingredient;
        private final LazyValue<Ingredient> rodIngredient;

        public ItemTier(int level, int uses, float speed, float damage, int enchant, Supplier<Ingredient> ingredient) {
            this(level, uses, speed, damage, enchant, ingredient, () -> Ingredient.of(Tags.Items.RODS_WOODEN));
        }

        public ItemTier(int level, int uses, float speed, float damage, int enchant, Supplier<Ingredient> ingredient, Supplier<Ingredient> rodIngredient){
            this.level = level;
            this.uses = uses;
            this.speed = speed;
            this.damage = damage;
            this.enchantmentValue = enchant;
            this.ingredient = new LazyValue<>(ingredient);
            this.rodIngredient = new LazyValue<>(rodIngredient);
        }
        public int getUses() {
            return this.uses;
        }

        public float getSpeed() {
            return this.speed;
        }

        public float getAttackDamageBonus() {
            return this.damage;
        }

        public int getLevel() {
            return this.level;
        }

        public int getEnchantmentValue() {
            return this.enchantmentValue;
        }

        public Ingredient getRepairIngredient() {
            return this.ingredient.get();
        }

        public Ingredient getRodIngredient(){
            return this.rodIngredient.get();
        }
    }
}
