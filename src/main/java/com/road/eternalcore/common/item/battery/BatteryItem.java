package com.road.eternalcore.common.item.battery;

import com.road.eternalcore.api.RGB;
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
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class BatteryItem extends Item {
    // 储电量最好不要超过4亿，因为计算电量百分比的公式是5*电量/上限
    private int capacity;
    private int voltage;

    public BatteryItem(int capacity, int voltage, Properties properties) {
        super(properties.tab(ModGroup.toolGroup).stacksTo(1));
        this.capacity = capacity;
        this.voltage = voltage;
    }
    public static int getEnergyLevel(ItemStack itemStack){
        AtomicInteger result = new AtomicInteger();
        itemStack.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
            int energyStored = storage.getEnergyStored();
            int maxEnergy = storage.getMaxEnergyStored();
            if (energyStored == maxEnergy){
                result.set(5);
            } else if (energyStored == 0) {
                result.set(0);
            } else {
                result.set(1 + 4 * energyStored / maxEnergy);
            }
        });
        return result.get();
    }

    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemEUProvider(() -> new ItemEUStorage(stack).set(capacity, voltage));
    }
    // 显示电量，没电时字体颜色是灰色，随着充电量逐渐变成绿色
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag iTooltipFlag) {
        itemStack.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
            int energy = storage.getEnergyStored();
            int maxEnergy = storage.getMaxEnergyStored();
            double energyRate = 1.0 * energy / maxEnergy;
            String msg = String.format("%d / %d EU (%d%%)", energy, maxEnergy, (int)(energyRate * 100));
            RGB EUColor = new RGB(TextFormatting.GRAY).transit(new RGB(TextFormatting.GREEN), energyRate);
            list.add(new StringTextComponent(msg).withStyle(Style.EMPTY.withColor(EUColor.getColor())));
        });
    }
    // 添加没电和满电两种电池
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> itemStacks){
        super.fillItemCategory(group, itemStacks);
        if (this.allowdedIn(group)){
            ItemStack itemFullEnergy = new ItemStack(this);
            itemFullEnergy.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage ->{
                storage.receiveEnergy(storage.getMaxEnergyStored(), false);
            });
            itemStacks.add(itemFullEnergy);
        }
    }
    // 耐久条改为电量条
    public boolean showDurabilityBar(ItemStack stack){
        return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }
    public double getDurabilityForDisplay(ItemStack stack){
        AtomicReference<Double> energyRate = new AtomicReference<>(0.0);
        stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
            energyRate.set(1.0 * storage.getEnergyStored() / storage.getMaxEnergyStored());
        });
        return 1.0 - energyRate.get();
    }
    public int getRGBDurabilityForDisplay(ItemStack stack){
        return new RGB(0, 170, 255).getColorValue();
    }
    // 添加充电过程的材质变化
    public static final String EnergyLevel = "EnergyLevel";
    public static final IItemPropertyGetter GetEnergyLevel = (itemStack, clientWorld, livingEntity) -> getEnergyLevel(itemStack);
}
