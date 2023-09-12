package com.road.eternalcore.api.energy;

import com.road.eternalcore.api.RGB;
import com.road.eternalcore.api.energy.eu.EUTier;
import com.road.eternalcore.api.energy.eu.IEUStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;

public class EnergyUtils {
    // 显示电量
    public static IFormattableTextComponent energyStorageText(int energy, int maxEnergy){
        return energyStorageText(energy, maxEnergy, true);
    }
    public static IFormattableTextComponent energyStorageText(int energy, int maxEnergy, boolean showMaxEnergy){
        double energyRate = maxEnergy == 0 ? 0 : Math.min(1.0 * energy / maxEnergy, 1.0);
        String msg;
        if (showMaxEnergy) {
            msg = String.format("%d / %d EU (%d%%)", energy, maxEnergy, (int) (energyRate * 100));
        } else {
            msg = String.format("%d EU", energy);
        }
        return new StringTextComponent(msg)
                .withStyle(Style.EMPTY.withColor(EnergyUtils.energyRateColor(energyRate)));
    }
    public static IFormattableTextComponent energyStorageText(EUTier tier, int energy, int maxEnergy){
        return energyStorageText(tier, energy, maxEnergy, true);
    }
    public static IFormattableTextComponent energyStorageText(EUTier tier, int energy, int maxEnergy, boolean showMaxEnergy){
        return tier.getText().append(" ").append(energyStorageText(energy, maxEnergy, showMaxEnergy));
    }
    // 显示电量的渐变色，没电时字体颜色是灰色，随着充电量逐渐变成绿色
    public static Color energyRateColor(double energyRate){
        return new RGB(TextFormatting.GRAY).transit(new RGB(TextFormatting.GREEN), energyRate).getColor();
    }
    // 电量交换（无损），单次输电量不超过输电方的最大电压等级，返回本次交换的电量
    public static int energyExchange(IEUStorage storageFrom, IEUStorage storageTo, boolean simulate){
        return energyExchange(storageFrom, storageTo, -1, simulate);
    }
    public static int energyExchange(IEUStorage storageFrom, IEUStorage storageTo, int amount, boolean simulate){
        // 先检查双方是否能输电
        if (!storageFrom.canExtract() || !storageTo.canReceive()){
            return 0;
        }
        // 不能从高压输送到低压
        if (storageFrom.tierHigherThan(storageTo)){
            return 0;
        }
        int maxExchange = amount < 0 ?
                Math.min(storageFrom.getEnergyStored(), storageFrom.getTier().getMaxVoltage()) : amount;
        int exchangeValue = storageTo.receiveEnergy(maxExchange, true);
        if (exchangeValue > 0 && !simulate){
            storageFrom.extractEnergy(exchangeValue, false);
            storageTo.receiveEnergy(exchangeValue, false);
        }
        return exchangeValue;
    }
    // 电池格合法检查（电池不超过指定电压）
    public static boolean checkBatteryValid(ItemStack itemStack, int tierLevel){
        IEUStorage storage = itemStack.getCapability(CapEnergy.EU).orElse(null);
        if (storage != null) {
            return !(storage.tierHigherThan(tierLevel));
        }
        DisposableBattery battery = DisposableBattery.get(itemStack.getItem());
        return battery != null && !(battery.tierHigherThan(tierLevel));
    }
    // 可充电合法检查，不能提供一次性电池
    public static boolean checkChargeableBatteryValid(ItemStack itemStack, int tierLevel){
        IEUStorage storage = itemStack.getCapability(CapEnergy.EU).orElse(null);
        return storage != null && !(storage.tierHigherThan(tierLevel));
    }
    // 可输电合法检查，不能提供电动工具
    public static boolean checkExtractableBatteryValid(ItemStack itemStack, int tierLevel){
        IEUStorage storage = itemStack.getCapability(CapEnergy.EU).orElse(null);
        if (storage != null) {
            return !(storage.tierHigherThan(tierLevel)) && storage.canExtract();
        }
        DisposableBattery battery = DisposableBattery.get(itemStack.getItem());
        return battery != null && !(battery.tierHigherThan(tierLevel));
    }
}
