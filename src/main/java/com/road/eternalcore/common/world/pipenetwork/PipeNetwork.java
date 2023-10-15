package com.road.eternalcore.common.world.pipenetwork;

import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

public abstract class PipeNetwork<N, P, R> {
    // 管道网络的类，N, P, R分别表示节点、提供端和接受端
    protected final ServerWorld world;
    protected final Map<BlockPos, N> nodes = new HashMap<>();
    protected final Map<BlockPos, P> providers = new HashMap<>();
    protected final Map<BlockPos, R> receivers = new HashMap<>();
    protected final SetMultimap<BlockPos, Direction> connections = Multimaps.newSetMultimap(
            new HashMap<>(), () -> EnumSet.noneOf(Direction.class));

    public PipeNetwork(ServerWorld world){
        this.world = world;
    }
    public boolean containsPos(BlockPos pos){
        return connections.containsKey(pos);
    }
    public boolean containsConnection(BlockPos pos, Direction direction){
        return connections.containsEntry(pos, direction);
    }
    public P getProviderAt(BlockPos pos){
        return providers.getOrDefault(pos, null);
    }
    public R getReceiverAt(BlockPos pos){
        return receivers.getOrDefault(pos, null);
    }

    public void addPos(BlockPos pos, N wire, Set<Direction> directions){
        nodes.put(pos, wire);
        connections.putAll(pos, directions);
    }
    public void addNode(BlockPos pos, N wire){
        nodes.put(pos, wire);
    }
    public void addConnection(BlockPos pos, Direction direction){
        connections.put(pos, direction);
    }
    public void addProvider(BlockPos pos, P provider){
        providers.put(pos, provider);
    }
    public void addReceiver(BlockPos pos, R receiver){
        receivers.put(pos, receiver);
    }

    public N getNode(BlockPos pos){
        return nodes.get(pos);
    }
    public Set<Direction> getConnectionsAt(BlockPos pos){
        return connections.get(pos);
    }
    public void deleteWire(BlockPos pos){
        nodes.remove(pos);
    }
    public void deleteConnection(BlockPos pos, Direction direction, boolean checkMachine){
        if (connections.remove(pos, direction) && checkMachine) {
            checkMachineConnections(pos.relative(direction));
        }
    }
    protected void checkMachineConnections(BlockPos pos){
        // 如果该节点的机器无法连接到电网，则从电网中删除
        P provider = getProviderAt(pos);
        if (provider != null){
            if (Arrays.stream(Direction.values()).noneMatch(
                    (direction -> containsPos(pos.relative(direction))))
            ){
                providers.remove(pos);
            }
        }
        R receiver = getReceiverAt(pos);
        if (receiver != null){
            if (Arrays.stream(Direction.values()).noneMatch(
                    (direction -> containsPos(pos.relative(direction))))
            ){
                receivers.remove(pos);
            }
        }
    }

    public void combine(PipeNetwork<N, P, R> otherNetwork){
        nodes.putAll(otherNetwork.nodes);
        connections.putAll(otherNetwork.connections);
        providers.putAll(otherNetwork.providers);
        receivers.putAll(otherNetwork.receivers);
    }
    public abstract PipeNetwork<N, P, R> splitByPoses(Set<BlockPos> poses);
    protected void splitByPoses(PipeNetwork<N, P, R> newNetwork, Set<BlockPos> poses){
        // 分离导线
        for (BlockPos pos : poses){
            newNetwork.addPos(pos, getNode(pos), getConnectionsAt(pos));
            deleteWire(pos);
            connections.removeAll(pos);
        }
        // 重新分配输出端
        for (BlockPos pos : new HashSet<>(providers.keySet())){
            P provider = providers.get(pos);
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
                newNetwork.addProvider(pos, provider);
            }
        }
        // 重新分配输入端
        for (BlockPos pos : new HashSet<>(receivers.keySet())){
            R receiver = receivers.get(pos);
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
                newNetwork.addReceiver(pos, receiver);
            }
        }
    }
    public boolean hasNoMachine(){
        return providers.isEmpty() && receivers.isEmpty();
    }
}
