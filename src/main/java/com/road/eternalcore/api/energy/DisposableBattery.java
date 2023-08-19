package com.road.eternalcore.api.energy;

import com.road.eternalcore.api.energy.eu.EUTier;
import com.road.eternalcore.api.energy.eu.IEUTier;
import com.road.eternalcore.api.material.MaterialShape;
import com.road.eternalcore.api.material.Materials;
import com.road.eternalcore.common.item.material.MaterialItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class DisposableBattery implements IEUTier {
    // 管理一次性电池的类，这些电池可以放在机器的电池槽中并给机器充电
    protected static final Map<Item, DisposableBattery> disposableBatteries = new HashMap<>();
    public static final DisposableBattery REDSTONE = new DisposableBattery(
            Items.REDSTONE, EUTier.LV, 800);
    public static final DisposableBattery REDSTONE_SMALL = new DisposableBattery(
            MaterialItems.get(MaterialShape.SMALL_DUST, Materials.REDSTONE), EUTier.LV, 200);
    public static final DisposableBattery REDSTONE_TINY = new DisposableBattery(
            MaterialItems.get(MaterialShape.TINY_DUST, Materials.REDSTONE), EUTier.LV, 88);

    protected final Item item;
    protected final EUTier euTier;
    protected final int energy;
    public DisposableBattery(Item item, EUTier euTier, int energy){
        if (disposableBatteries.containsKey(item)){
            throw new IllegalStateException("DisposableBattery "+item+" has already existed!");
        }
        this.item = item;
        this.euTier = euTier;
        this.energy = energy;
        disposableBatteries.put(item, this);
    }
    public static DisposableBattery get(Item item){
        return disposableBatteries.getOrDefault(item, null);
    }

    public EUTier getTier() {
        return euTier;
    }
    public int getEnergy() {
        return energy;
    }
}
