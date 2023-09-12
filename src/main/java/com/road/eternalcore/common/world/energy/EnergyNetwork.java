package com.road.eternalcore.common.world.energy;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
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

public class EnergyNetwork {
    // 管理电网的类，存储电网中所有电线的连接状态以及所有接入电网的机器
    // 注意：所有电线必须是双向连接的
    private final ServerWorld world;
    private final Map<BlockPos, IEnergyNetworkWire> wires = new HashMap<>();
    private final SetMultimap<BlockPos, Direction> connections = Multimaps.newSetMultimap(new HashMap<>(), () -> EnumSet.noneOf(Direction.class));
    // 记录和电网连接到的机器，用于检测电网是否有必要存在
    private final Map<BlockPos, IEnergyProviderTileEntity> providers = new HashMap<>();
    private final Map<BlockPos, IEnergyReceiverTileEntity> receivers = new HashMap<>();
    // 记录电流负载情况
    private final Map<BlockPos, Integer> wireLoad = new HashMap<>();
    public EnergyNetwork(ServerWorld world){
        this.world = world;
    }

    public boolean containsPos(BlockPos pos){
        return connections.containsKey(pos);
    }
    public boolean containsConnection(BlockPos pos, Direction direction){
        return connections.containsEntry(pos, direction);
    }
    public IEnergyProviderTileEntity getProviderAt(BlockPos pos){
        return providers.getOrDefault(pos, null);
    }
    public IEnergyReceiverTileEntity getReceiverAt(BlockPos pos){
        return receivers.getOrDefault(pos, null);
    }
    public int getWireLoadAt(BlockPos pos){
        return wireLoad.getOrDefault(pos, 0);
    }
    public void addPos(BlockPos pos, IEnergyNetworkWire wire, Set<Direction> directions){
        wires.put(pos, wire);
        connections.putAll(pos, directions);
    }
    public void addWire(BlockPos pos, IEnergyNetworkWire wire){
        wires.put(pos, wire);
    }
    public void addConnection(BlockPos pos, Direction direction){
        connections.put(pos, direction);
    }
    public IEnergyNetworkWire getWire(BlockPos pos){
        return wires.get(pos);
    }
    public Set<Direction> getConnectionsAt(BlockPos pos){
        return connections.get(pos);
    }
    public void addProvider(BlockPos pos, IEnergyProviderTileEntity machine){
        providers.put(pos, machine);
    }
    public void addReceiver(BlockPos pos, IEnergyReceiverTileEntity machine){
        receivers.put(pos, machine);
    }
    public void deleteWire(BlockPos pos){
        wires.remove(pos);
    }
    public void deleteConnection(BlockPos pos, Direction direction, boolean checkMachine){
        if (connections.remove(pos, direction) && checkMachine) {
            checkMachineConnections(pos.relative(direction));
        }
    }

    protected void checkMachineConnections(BlockPos pos){
        // 如果该节点的机器无法连接到电网，则从电网中删除
        IEnergyProviderTileEntity provider = getProviderAt(pos);
        if (provider != null){
            if (Arrays.stream(Direction.values()).noneMatch(
                    (direction -> containsPos(pos.relative(direction))))
            ){
                providers.remove(pos);
            }
        }
        IEnergyReceiverTileEntity receiver = getReceiverAt(pos);
        if (receiver != null){
            if (Arrays.stream(Direction.values()).noneMatch(
                    (direction -> containsPos(pos.relative(direction))))
            ){
                receivers.remove(pos);
            }
        }
    }

    public void combine(EnergyNetwork otherNetwork){
        wires.putAll(otherNetwork.wires);
        connections.putAll(otherNetwork.connections);
        providers.putAll(otherNetwork.providers);
        receivers.putAll(otherNetwork.receivers);
    }

    public EnergyNetwork splitByPoses(Set<BlockPos> poses){
        EnergyNetwork newNetwork = new EnergyNetwork(world);
        // 分离导线
        for (BlockPos pos : poses){
            newNetwork.addPos(pos, getWire(pos), getConnectionsAt(pos));
            deleteWire(pos);
            connections.removeAll(pos);
        }
        // 重新分配输出端
        for (BlockPos pos : new HashSet<>(providers.keySet())){
            IEnergyProviderTileEntity machine = providers.get(pos);
            boolean inThis = false;
            boolean inNew = false;
            for (Direction direction : Direction.values()){
                BlockPos connectedPos = pos.relative(direction);
                if (containsPos(connectedPos)) {
                    inThis = true;
                }
                if (newNetwork.containsPos(connectedPos)){
                    inNew = true;
                }
            }
            if (!inThis){
                providers.remove(pos);
            }
            if (inNew){
                newNetwork.addProvider(pos, machine);
            }
        }
        // 重新分配输入端
        for (BlockPos pos : new HashSet<>(receivers.keySet())){
            IEnergyReceiverTileEntity machine = receivers.get(pos);
            boolean inThis = false;
            boolean inNew = false;
            for (Direction direction : Direction.values()){
                BlockPos connectedPos = pos.relative(direction);
                if (containsPos(connectedPos)) {
                    inThis = true;
                }
                if (newNetwork.containsPos(connectedPos)){
                    inNew = true;
                }
            }
            if (!inThis){
                receivers.remove(pos);
            }
            if (inNew){
                newNetwork.addReceiver(pos, machine);
            }
        }
        return newNetwork;
    }
    public boolean hasNoMachine(){
        return providers.isEmpty() && receivers.isEmpty();
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
        IEnergyNetworkWire wire = getWire(pos);
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
                int lineLoss = searchMap.get(workPos).getSecond() + getWire(workPos).getLineLoss();
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
            current.setLineLoss(searchMap.get(endPos).getSecond() + getWire(endPos).getLineLoss());
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
