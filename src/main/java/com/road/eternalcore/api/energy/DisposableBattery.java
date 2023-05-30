package com.road.eternalcore.api.energy;

import com.road.eternalcore.api.energy.eu.EUTier;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class DisposableBattery {
    // 管理一次性电池的类，这些电池可以放在机器的电池槽中并给机器充电
    protected static final Map<Item, DisposableBattery> disposableBatteries = new HashMap<>();
    public static final DisposableBattery REDSTONE = new DisposableBattery(Items.REDSTONE, EUTier.LV, 800);

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
