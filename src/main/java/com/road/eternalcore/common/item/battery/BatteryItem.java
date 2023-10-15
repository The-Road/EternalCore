package com.road.eternalcore.common.item.battery;

import com.road.eternalcore.api.RGB;
import com.road.eternalcore.api.energy.CapEnergy;
import com.road.eternalcore.api.energy.EnergyUtils;
import com.road.eternalcore.api.energy.eu.EUTier;
import com.road.eternalcore.api.energy.eu.IEUStorage;
import com.road.eternalcore.api.energy.eu.ItemEUProvider;
import com.road.eternalcore.api.energy.eu.ItemEUStorage;
import com.road.eternalcore.common.item.ModItem;
import com.road.eternalcore.common.item.group.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

public class BatteryItem extends ModItem {
    private final int maxEnergy;
    private final EUTier euTier;
    public static final IItemPropertyGetter GetChargeLevel = (itemStack, clientWorld, livingEntity) -> getChargeLevel(itemStack);

    public BatteryItem(int maxEnergy, EUTier euTier, Properties properties) {
        super(properties.tab(ModGroup.toolGroup).stacksTo(1));
        this.maxEnergy = maxEnergy;
        this.euTier = euTier;
    }
    // 获取当前充电程度
    public static int getChargeLevel(ItemStack itemStack){
        IEUStorage storage = itemStack.getCapability(CapEnergy.EU).orElse(null);
        if (storage != null){
            int energyStored = storage.getEnergyStored();
            int maxEnergy = storage.getMaxEnergyStored();
            if (energyStored == maxEnergy){
                return 5;
            } else if (energyStored == 0) {
                return 0;
            } else {
                return 1 + (int)(4.0 * energyStored / maxEnergy);
            }
        }
        return 0;
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
            list.add(EnergyUtils.energyStorageText(storage.getTier(), energy, maxEnergy));
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
        IEUStorage storage = stack.getCapability(CapEnergy.EU).orElse(null);
        if (storage != null){
            return 1.0 - 1.0 * storage.getEnergyStored() / storage.getMaxEnergyStored();
        }
        return 0;
    }
    public int getRGBDurabilityForDisplay(ItemStack stack){
        return new RGB(0, 170, 255).getColorValue();
    }
}
