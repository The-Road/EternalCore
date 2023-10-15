package com.road.eternalcore.common.world.pipenetwork;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.mojang.datafixers.util.Pair;
import com.road.eternalcore.api.energy.eu.EUTier;
import com.road.eternalcore.api.energy.network.IEnergyNetworkWire;
import com.road.eternalcore.common.tileentity.IEnergyProviderTileEntity;
import com.road.eternalcore.common.tileentity.IEnergyReceiverTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

public class EnergyNetwork extends PipeNetwork<IEnergyNetworkWire, IEnergyProviderTileEntity, IEnergyReceiverTileEntity>{
    // 管理电网的类，存储电网中所有电线的连接状态以及所有接入电网的机器
    // 记录电流负载情况
    private final Map<BlockPos, Integer> wireLoad = new HashMap<>();
    public EnergyNetwork(ServerWorld world){
        super(world);
    }
    public int getWireLoadAt(BlockPos pos){
        return wireLoad.getOrDefault(pos, 0);
    }
    public EnergyNetwork splitByPoses(Set<BlockPos> poses){
        EnergyNetwork newNetwork = new EnergyNetwork(world);
        splitByPoses(newNetwork, poses);
        return newNetwork;
    }
    // -----电流相关-----
    public List<EnergyCurrent.WithMachine> getCurrents(IEnergyProviderTileEntity provider, BlockPos startPos, Set<BlockPos> receiverPoses){
        SetMultimap<IEnergyReceiverTileEntity, BlockPos> receiverEndPoses = HashMultimap.create();
        receivers.forEach((pos, receiver) -> {
            if (receiverPoses.contains(pos)){
                for (Direction direction : Direction.values()){
                    if (receiver.canReceive(direction)){
                        BlockPos receivePos = pos.relative(direction);
                        if (containsPos(receivePos)) {
                            receiverEndPoses.put(receiver, receivePos);
                        }
                    }
                }
            }
        });
        PathFind pathFind = new PathFind(startPos);
        List<EnergyCurrent.WithMachine> currentList = new ArrayList<>();
        for (IEnergyReceiverTileEntity receiver : receiverEndPoses.keySet()){
            // 以该机器的所有相邻位置作为终点获取电流路径，并选择其中线损最小的
            EnergyCurrent bestCurrent = receiverEndPoses.get(receiver).stream()
                    .map(pathFind::findCurrent)
                    .min(Comparator.comparingInt(EnergyCurrent::getLineLoss))
                    .orElse(new EnergyCurrent.Empty(this));
            if (bestCurrent.isValid()) {
                currentList.add(new EnergyCurrent.WithMachine(provider, receiver, bestCurrent));
            }
        }
        return currentList;
    }
    public void clearWireLoad(){
        wireLoad.clear();
    }

    public boolean checkWireLoad(BlockPos pos, EUTier tier){
        // 检查电压过载
        IEnergyNetworkWire wire = getNode(pos);
        if (tier.tierHigherThan(wire)){
            wire.burn(world, pos);
            return true;
        }
        // 检查电流过载
        int wireCurrent = wireLoad.getOrDefault(pos, 0) + 1;
        if (wireCurrent > wire.getMaxCurrent()){
            wire.burn(world, pos);
            return true;
        }
        wireLoad.put(pos, wireCurrent);
        return false;
    }

    private class PathFind{
        // 记录每一个位置的电流来源方向和总电阻（不包括当前格，便于计算）
        private final Map<BlockPos, Pair<Direction, Integer>> searchMap = new HashMap<>();
        private final Set<BlockPos> workingPoses = new HashSet<>();
        private final Set<BlockPos> finishedPoses = new HashSet<>();
        private final BlockPos startPos;
        private PathFind(BlockPos startPos){
            this.startPos = startPos;
            workingPoses.add(startPos);
            searchMap.put(startPos, Pair.of(null, 0));
        }

        private EnergyCurrent findCurrent(BlockPos endPos){
            while (!workingPoses.isEmpty()){
                BlockPos workPos = workingPoses.stream()
                        .min(Comparator.comparingInt(pos -> searchMap.get(pos).getSecond())).get();
                finishedPoses.add(workPos);
                workingPoses.remove(workPos);
                if (finishedPoses.contains(endPos)){
                    return getResult(endPos);
                }
                int lineLoss = searchMap.get(workPos).getSecond() + getNode(workPos).getLineLoss();
                for (Direction direction : connections.get(workPos)){
                    BlockPos connectedPos = workPos.relative(direction);
                    if (finishedPoses.contains(connectedPos)){
                        continue;
                    }
                    if (searchMap.containsKey(connectedPos)){
                        if (searchMap.get(connectedPos).getSecond() > lineLoss){
                            searchMap.put(connectedPos, Pair.of(direction.getOpposite(), lineLoss));
                        }
                    } else if (containsPos(connectedPos)){
                        searchMap.put(connectedPos, Pair.of(direction.getOpposite(), lineLoss));
                        workingPoses.add(connectedPos);
                    }
                }
            }
            return new EnergyCurrent.Empty(EnergyNetwork.this);
        }
        private EnergyCurrent getResult(BlockPos endPos){
            EnergyCurrent current = new EnergyCurrent(EnergyNetwork.this);
            current.setLineLoss(searchMap.get(endPos).getSecond() + getNode(endPos).getLineLoss());
            BlockPos pos = endPos;
            current.addPath(pos);
            while (!startPos.equals(pos)){
                pos = pos.relative(searchMap.get(pos).getFirst());
                current.addPath(pos);
            }
            return current;
        }
    }
}
