package com.road.eternalcore.common.item.tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.road.eternalcore.ModConstant;
import com.road.eternalcore.TranslationUtils;
import com.road.eternalcore.api.material.MaterialTierData;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.item.ModItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CustomTierItem extends ModItem {
    // 自定义工具和武器类
    // 通过StringNBT标签material表示工具的材料，工具的各项属性都通过该标签来获取
    protected float atkDamage; // 基础攻击，默认伤害计算方式为基础攻击+材料攻击
    protected float atkSpeed; // 面板攻速（表示每秒的攻击次数），MC原版获取物品的getAttackSpeed的时候会-4

    protected boolean onlyForCrafting = false; // 不可用于破坏方块或打怪的工具，这类工具在挖掘或攻击时不会掉耐久，也不会显示挖掘等级、挖掘速度、攻击力和攻速
    public static final int DEFAULT_DURABILITY_SUBDIVIDE = 20; // 每一点耐久的细分程度

    public CustomTierItem(Properties itemProperties){
        this(itemProperties, 0, 4);
    }
    public CustomTierItem(Properties itemProperties, float atkDamage, float atkSpeed){
        super(itemProperties.defaultDurability(0).setNoRepair());
        this.atkDamage = atkDamage;
        this.atkSpeed = atkSpeed;
    }
    public static int addItemDamage(ItemStack itemStack, int damageDecimal){
        // 用于给CustomTierItem增加耐久损耗（可以让耐久精确到0.05）
        // 其实只要有耐久的物品都可以用
        int damage = 0;
        CompoundNBT tag = itemStack.getOrCreateTag();
        int lastDecimal = tag.getInt(ModConstant.Durability_decimal);
        int binary = getBinary(itemStack);
        lastDecimal += damageDecimal;
        damage = lastDecimal / binary;
        lastDecimal %= binary;
        tag.putInt(ModConstant.Durability_decimal, lastDecimal);
        return damage;
    }
    public static void addItemDamageNoUpdate(ItemStack itemStack, int damageDecimal){
        // 只增加耐久尾数，不刷新耐久度
        CompoundNBT tag = itemStack.getOrCreateTag();
        int lastDecimal = tag.getInt(ModConstant.Durability_decimal);
        lastDecimal += damageDecimal;
        tag.putInt(ModConstant.Durability_decimal, lastDecimal);
    }
    public static int getBinary(ItemStack itemStack){
        return DEFAULT_DURABILITY_SUBDIVIDE;
    }
    public static MaterialTierData getMaterialData(ItemStack itemStack){
        CompoundNBT itemNBT = itemStack.getTag();
        if (itemNBT != null && itemNBT.contains(ModConstant.Material, 8)){
            return MaterialTierData.get(Materials.get(itemNBT.getString(ModConstant.Material)));
        }
        return MaterialTierData.NULL;
    }

    protected static IItemTier getTier(ItemStack itemStack){
        // 从NBT获取物品的材料
        MaterialTierData materialTierData = getMaterialData(itemStack);
        if (materialTierData != null && materialTierData.getItemTier() != null){
            return materialTierData.getItemTier();
        }
        return MaterialTierData.NULL.getItemTier();
    }

    public final int getMaxDamage(ItemStack itemStack){
        return getTier(itemStack).getUses();
    }

    public boolean canBeDepleted(){
        return true;
    }

    public boolean hurtEnemy(ItemStack itemStack, LivingEntity enemyEntity, LivingEntity playerEntity) {
        if (this.onlyForCrafting){
            return super.hurtEnemy(itemStack, enemyEntity, playerEntity);
        }
        itemStack.hurtAndBreak(getHurtEnemyDamage(itemStack, enemyEntity), playerEntity, (player) -> {
            player.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
        });
        return true;
    }
    protected int getHurtEnemyDamage(ItemStack itemStack, LivingEntity enemyEntity){
        // 获取攻击时损失的耐久度
        return 1;
    }
    public boolean mineBlock(ItemStack itemStack, World world, BlockState blockState, BlockPos blockPos, LivingEntity playerEntity) {
        if (this.onlyForCrafting){
            return super.mineBlock(itemStack, world, blockState, blockPos, playerEntity);
        }
        if (!world.isClientSide() && blockState.getDestroySpeed(world, blockPos) != 0.0F) {
            itemStack.hurtAndBreak(getMineBlockDamage(itemStack, world, blockState, blockPos), playerEntity, (player) -> {
                player.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            });
        }
        return true;
    }
    protected int getMineBlockDamage(ItemStack itemStack, World world, BlockState blockState, BlockPos blockPos){
        // 获取挖掘方块时损失的耐久度
        // 默认规则：损失方块硬度*0.64的耐久（向上取整到0.05）
        int damage = 0;
        float destroySpeed = blockState.getDestroySpeed(world, blockPos);
        if (destroySpeed != 0.0F){
            damage = addItemDamage(itemStack, (int) Math.ceil(destroySpeed * 0.64 * DEFAULT_DURABILITY_SUBDIVIDE));
        }
        return damage;
    }

    protected int getTierLevel(ItemStack itemStack){
        // 获取挖掘等级
        return getTier(itemStack).getLevel();
    }
    protected int getSmithLevel(ItemStack itemStack){
        // 获取锻造等级
        return getMaterialData(itemStack).getSmithLevel();
    }

    public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        int i = super.getHarvestLevel(stack, tool, player, blockState);
        if (i >= 0){
            i = getTierLevel(stack);
        }
        return i;
    }
    protected float getMineSpeed(ItemStack itemStack){
        // 获取挖掘速度
        return getTier(itemStack).getSpeed();
    }
    public ITextComponent getName(ItemStack itemStack) {
        MaterialTierData data = getMaterialData(itemStack);
        return new TranslationTextComponent(this.getDescriptionId(itemStack), data.getMaterial().getText());
    }

    protected void addSmithLevelText(ItemStack itemStack, List<ITextComponent> list){
        list.add(TranslationUtils.toolSmithLevel(getSmithLevel(itemStack)));
    }
    protected void addDurabilityText(ItemStack itemStack, List<ITextComponent> list, ITooltipFlag tooltipFlag){
        if (!itemStack.isDamaged() || !tooltipFlag.isAdvanced()) {
            list.add(TranslationUtils.toolDurability(getMaxDamage(itemStack)));
        }
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack){
        // 获取攻击和攻速属性
        if (!this.onlyForCrafting && slot == EquipmentSlotType.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "CustomTier modifier", getBasicAttackDamage(stack), AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "CustomTier modifier", getAttackSpeed(stack), AttributeModifier.Operation.ADDITION));
            return builder.build();
        }else{
            return super.getAttributeModifiers(slot, stack);
        }
    }
    protected double getBasicAttackDamage(ItemStack itemStack){
        // 获取材料的基础攻击
        return this.atkDamage + getTier(itemStack).getAttackDamageBonus();
    }
    protected double getAttackSpeed(ItemStack itemStack){
        return this.atkSpeed - 4.0F;
    }
    public float getMineSpeedRate(){
        return 1.0F;
    }

}
