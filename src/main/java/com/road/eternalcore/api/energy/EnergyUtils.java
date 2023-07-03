package com.road.eternalcore.api.energy;

import com.road.eternalcore.api.RGB;
import com.road.eternalcore.api.energy.eu.EUTier;
import com.road.eternalcore.api.energy.eu.IEUStorage;
import net.minecraft.util.text.*;

public class EnergyUtils {
    // 显示电量
    public static IFormattableTextComponent energyStorageText(int energy, int maxEnergy){
        double energyRate = maxEnergy == 0 ? 0 : 1.0 * energy / maxEnergy;
        String msg = String.format("%d / %d EU (%d%%)", energy, maxEnergy, (int)(energyRate * 100));
        return new StringTextComponent(msg)
                .withStyle(Style.EMPTY.withColor(EnergyUtils.energyRateColor(energyRate)));
    }
    public static IFormattableTextComponent energyStorageText(EUTier tier, int energy, int maxEnergy){
        return tier.getText().append(" ").append(energyStorageText(energy, maxEnergy));
    }
    // 显示电量的渐变色，没电时字体颜色是灰色，随着充电量逐渐变成绿色
    public static Color energyRateColor(double energyRate){
        return new RGB(TextFormatting.GRAY).transit(new RGB(TextFormatting.GREEN), energyRate).getColor();
    }
    // 电量交换（无损），单次输电量不超过输电方的最大电压等级，返回本次交换的电量
    public static int energyExchange(IEUStorage storageFrom, IEUStorage storageTo){
        return energyExchange(storageFrom, storageTo, false);
    }
    public static int energyExchange(IEUStorage storageFrom, IEUStorage storageTo, boolean simulate){
        // 先检查双方是否能输电
        if (!storageFrom.canExtract() || !storageTo.canReceive()){
            return 0;
        }
        // 不能从高压输送到低压
        if (storageFrom.getTier().higherThan(storageTo.getTier())){
            return 0;
        }
        int maxExchange = Math.min(storageFrom.getEnergyStored(), storageFrom.getTier().getMaxVoltage());
        int exchangeValue = storageTo.receiveEnergy(maxExchange, true);
        if (exchangeValue > 0 && !simulate){
            storageFrom.extractEnergy(exchangeValue, false);
            storageTo.receiveEnergy(exchangeValue, false);
        }
        return exchangeValue;
    }
}
