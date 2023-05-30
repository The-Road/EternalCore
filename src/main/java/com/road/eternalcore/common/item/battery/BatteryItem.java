package com.road.eternalcore.common.item.battery;

import com.road.eternalcore.api.RGB;
import com.road.eternalcore.api.energy.EnergyUtils;
import com.road.eternalcore.api.energy.eu.EUTier;
import com.road.eternalcore.api.energy.eu.ItemEUProvider;
import com.road.eternalcore.api.energy.eu.ItemEUStorage;
import com.road.eternalcore.common.item.group.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import com.road.eternalcore.api.energy.CapEnergy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class BatteryItem extends Item {
    private int maxEnergy;
    private EUTier euTier;
    // 充电过程的材质变化
    public static final String ChargeLevel = "charge_level";
    public static final IItemPropertyGetter GetChargeLevel = (itemStack, clientWorld, livingEntity) -> getChargeLevel(itemStack);

    public BatteryItem(int maxEnergy, EUTier euTier, Properties properties) {
        super(properties.tab(ModGroup.toolGroup).stacksTo(1));
        this.maxEnergy = maxEnergy;
        this.euTier = euTier;
    }
    // 获取当前充电程度
    public static int getChargeLevel(ItemStack itemStack){
        AtomicInteger result = new AtomicInteger();
        itemStack.getCapability(CapEnergy.EU).ifPresent(storage -> {
            int energyStored = storage.getEnergyStored();
            int maxEnergy = storage.getMaxEnergyStored();
            if (energyStored == maxEnergy){
                result.set(5);
            } else if (energyStored == 0) {
                result.set(0);
            } else {
                result.set(1 + (int)(4.0 * energyStored / maxEnergy));
            }
        });
        return result.get();
    }

    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemEUProvider(() -> new ItemEUStorage(stack).set(this.maxEnergy, this.euTier));
    }
    // 显示电量
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag iTooltipFlag) {
        itemStack.getCapability(CapEnergy.EU).ifPresent(storage -> {
            int energy = storage.getEnergyStored();
            int maxEnergy = storage.getMaxEnergyStored();
            list.add(EnergyUtils.energyStorageText(energy, maxEnergy));
        });
    }
    // 添加没电和满电两种电池
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> itemStacks){
        super.fillItemCategory(group, itemStacks);
        if (this.allowdedIn(group)){
            ItemStack itemFullEnergy = new ItemStack(this);
            itemFullEnergy.getCapability(CapEnergy.EU).ifPresent(storage ->{
                storage.receiveEnergy(storage.getMaxEnergyStored(), false);
            });
            itemStacks.add(itemFullEnergy);
        }
    }
    // 耐久条改为电量条
    public boolean showDurabilityBar(ItemStack stack){
        return stack.getCapability(CapEnergy.EU).isPresent();
    }
    public double getDurabilityForDisplay(ItemStack stack){
        AtomicReference<Double> energyRate = new AtomicReference<>(0.0);
        stack.getCapability(CapEnergy.EU).ifPresent(storage -> {
            energyRate.set(1.0 - 1.0 * storage.getEnergyStored() / storage.getMaxEnergyStored());
        });
        return energyRate.get();
    }
    public int getRGBDurabilityForDisplay(ItemStack stack){
        return new RGB(0, 170, 255).getColorValue();
    }
}
