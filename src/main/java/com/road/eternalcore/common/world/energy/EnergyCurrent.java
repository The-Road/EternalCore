package com.road.eternalcore.common.world.energy;

import com.road.eternalcore.api.energy.eu.EUTier;
import com.road.eternalcore.common.tileentity.IEnergyProviderTileEntity;
import com.road.eternalcore.common.tileentity.IEnergyReceiverTileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class EnergyCurrent {
    // 电流，记录电网中的一条能量
    private final EnergyNetwork network;
    private final Set<BlockPos> path = new HashSet<>();
    private int lineLoss = 0;

    public EnergyCurrent(EnergyNetwork network){
        this.network = network;
    }
    public void addPath(BlockPos pos){
        path.add(pos);
    }
    public void setLineLoss(int lineLoss){
        this.lineLoss = lineLoss;
    }
    public int getLineLoss(){
        return lineLoss;
    }
    public boolean isValid(){
        return true;
    }

    public boolean workAndCheck(EUTier tier){
        // 检查电线是否过载(超过电线的最大承受电压或电流）
        boolean result = false;
        for (BlockPos pos : path){
            result = network.checkWireLoad(pos, tier) || result;
        }
        return result;
    }

    public static class Empty extends EnergyCurrent{
        public Empty(EnergyNetwork network) {
            super(network);
        }
        public int getLineLoss(){
            return Integer.MAX_VALUE;
        }

        public boolean isValid(){
            return false;
        }
    }
    public static class WithMachine{
        public final IEnergyProviderTileEntity provider;
        public final IEnergyReceiverTileEntity receiver;
        public final EnergyCurrent current;
        public WithMachine(IEnergyProviderTileEntity provider, IEnergyReceiverTileEntity receiver, EnergyCurrent current){
            this.provider = provider;
            this.receiver = receiver;
            this.current = current;
        }
    }
}
