package com.road.eternalcore.common.world.energy;

import com.mojang.datafixers.util.Pair;
import com.road.eternalcore.Utils;
import com.road.eternalcore.api.energy.eu.EUTier;
import com.road.eternalcore.api.energy.network.IEnergyNetworkWire;
import com.road.eternalcore.common.tileentity.IEnergyProviderTileEntity;
import com.road.eternalcore.common.tileentity.IEnergyReceiverTileEntity;
import com.road.eternalcore.common.util.TaskQueue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nullable;
import java.util.*;

public class EnergyNetworkManager extends WorldSavedData {
    // 管理电网，每帧开始时更新所有电线的连接状态，每帧结束时所有机器通过电网输电
    private static final String NAME = Utils.MOD_ID + "_energy_network";
    private final ServerWorld world;
    private final Set<EnergyNetwork> networks = new HashSet<>();
    // 记录所有需要更新的机器
    private final Map<BlockPos, IEnergyProviderTileEntity> providers = new HashMap<>();
    private final Map<BlockPos, IEnergyReceiverTileEntity> receivers = new HashMap<>();
    // 记录所有机器的输电次数
    private final Map<IEnergyProviderTileEntity, Integer> providerCount = new HashMap<>();
    private final Map<IEnergyReceiverTileEntity, Integer> receiverCount = new HashMap<>();
    // 记录所有需要更新电线状态的位置
    private final Set<BlockPos> updatedPoses = new HashSet<>();
    private final Set<BlockPos> updatedConnectionPoses = new HashSet<>();
    public EnergyNetworkManager(ServerWorld world) {
        super(NAME);
        this.world = world;
    }

    public static EnergyNetworkManager get(World world){
        if (world.isClientSide()){
            throw new RuntimeException("Get energy network from client side.");
        }
        ServerWorld serverWorld = (ServerWorld) world;
        DimensionSavedDataManager storage = serverWorld.getDataStorage();
        return storage.computeIfAbsent(() -> new EnergyNetworkManager(serverWorld), NAME);
    }
    public static void updateWirePos(World world, BlockState state, BlockPos pos, boolean isPrior){
        if (world.isClientSide() || !(state.getBlock() instanceof IEnergyNetworkWire)){
            return;
        }
        EnergyNetworkManager networkManager = EnergyNetworkManager.get(world);
        if (isPrior) {
            networkManager.updatedPoses.add(pos.immutable());
        } else {
            networkManager.updatedConnectionPoses.add(pos.immutable());
        }
    }

    public void load(CompoundNBT nbt) {
        // 虽然是WorldSavedData，但是并不保存数据，而是每次加载世界的时候重新计算导线网络数据
    }
    public CompoundNBT save(CompoundNBT nbt) {
        return nbt;
    }

    public void addEnergyProviderMachine(BlockPos pos, IEnergyProviderTileEntity machine){
        this.providers.put(pos, machine);
    }
    public void addEnergyReceiverMachine(BlockPos pos, IEnergyReceiverTileEntity machine){
        this.receivers.put(pos, machine);
    }
    public EnergyNetwork getNetworkAtPos(BlockPos pos){
        for (EnergyNetwork network : this.networks){
            if (network.containsPos(pos)){
                return network;
            }
        }
        return null;
    }
    public void transferEnergy(IEnergyProviderTileEntity provider, IEnergyReceiverTileEntity receiver, EnergyCurrent current){
        int countP = providerCount.getOrDefault(provider, 0);
        int countR = receiverCount.getOrDefault(receiver, 0);
        while (countP < provider.maxProvideCurrent() && countR < receiver.maxReceiveCurrent() &&
                provider.canProvideEnergyToNetwork() && receiver.canReceiveEnergyFromNetwork()){
            int lineLoss;
            if (current != null){
                if (current.workAndCheck(provider.getTier())){
                    checkMachineExplode(receiver, provider.getTier());
                    return;
                }
                lineLoss = current.getLineLoss();
            } else {
                lineLoss = 0;
            }
            if (checkMachineExplode(receiver, provider.getTier())){
                return;
            }
            int energy = provider.getTier().getMaxVoltage();
            // 从电网接收到的能量可以超过机器的储能上限
            provider.extractEnergy(energy, false);
            receiver.receiveEnergy(energy - lineLoss, false, true);
            providerCount.put(provider, ++countP);
            receiverCount.put(receiver, ++countR);
        }
    }
    // 检查机器是否因为电压过高而爆炸
    private boolean checkMachineExplode(IEnergyReceiverTileEntity machine, EUTier tier){
        if (tier.tierHigherThan(machine)){
            TileEntity tileEntity = (TileEntity) machine;
            this.world.removeBlock(tileEntity.getBlockPos(), false);
            return true;
        }
        return false;
    }
    public void tickStart(){
        // 更新电网状态
        updatedPoses.forEach(this::updateNetworkPos);
        updatedConnectionPoses.forEach(this::updateNetworkPos);
        this.networks.forEach(EnergyNetwork::clearWireLoad);
        // 清空待处理的电网列表
        this.updatedPoses.clear();
        this.updatedConnectionPoses.clear();
        this.providers.clear();
        this.receivers.clear();
        this.providerCount.clear();
        this.receiverCount.clear();
    }
    public void tickEnd(){
        // 所有能源接受端接入电网
        this.receivers.forEach((pos, machine) -> {
            for (Direction direction : Direction.values()){
                if (machine.canReceive(direction)){
                    BlockPos connectedPos = pos.relative(direction);
                    // 如果连接到了不在电网中的电线，将其加入电网
                    if (getNetworkAtPos(connectedPos) == null){
                        BlockState state = world.getBlockState(connectedPos);
                        Block block = state.getBlock();
                        if (block instanceof IEnergyNetworkWire){
                            IEnergyNetworkWire wire = (IEnergyNetworkWire) block;
                            TaskQueue.runTask((t) -> addPosToNetwork(t, createNewNetwork(), state, wire, connectedPos));
                        }
                    }
                }
            }
        });
        // 所有能源输出端向相邻机器或电网输电
        List<EnergyCurrent.WithMachine> currents = new ArrayList<>();
        this.providers.forEach((pos, machine) -> {
            for (Direction direction : Direction.values()){
                if (machine.canExtract(direction)){
                    BlockPos connectedPos = pos.relative(direction);
                    BlockState state = this.world.getBlockState(connectedPos);
                    if (state.hasTileEntity()){
                        TileEntity tileEntity = this.world.getBlockEntity(connectedPos);
                        if (tileEntity instanceof IEnergyReceiverTileEntity){
                            // 向该机器输电
                            transferEnergy(machine, (IEnergyReceiverTileEntity) tileEntity, null);
                        }
                    } else {
                        Block block = state.getBlock();
                        if (block instanceof IEnergyNetworkWire){
                            EnergyNetwork network = getNetworkAtPos(connectedPos);
                            if (network == null){
                                EnergyNetwork newNetwork = createNewNetwork();
                                network = newNetwork;
                                IEnergyNetworkWire wire = (IEnergyNetworkWire) block;
                                TaskQueue.runTask((t) -> addPosToNetwork(t, newNetwork, state, wire, connectedPos));
                            }
                            // 查找电网中的所有可达机器并加入待输电列表
                            currents.addAll(network.getCurrents(machine, connectedPos, receivers.keySet()));
                        }
                    }
                }
            }
        });
        // 电网输电优先
        currents.sort(Comparator.comparingInt(c -> c.current.getLineLoss()));
        for (EnergyCurrent.WithMachine c: currents){
            transferEnergy(c.provider, c.receiver, c.current);
        }
    }
    // ----------更新电网所需的方法----------
    private void updateNetworkPos(BlockPos pos){
        EnergyNetwork network = getNetworkAtPos(pos);
        BlockState state = this.world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof IEnergyNetworkWire){
            IEnergyNetworkWire wire = (IEnergyNetworkWire) block;
            if (network != null){
                // 在电网中的电线，检查其连接状态
                updateInNetworkWire(network, state, wire, pos);
            } else {
                // 不在电网中的电线，将其加入附近的电网
                updateNoNetworkWire(state, wire, pos);
            }
        } else if (network != null){
            // 在电网中但不是电线，将这一点断开
            deletePosFromNetwork(network, pos);
        }
    }
    private void updateInNetworkWire(EnergyNetwork network, BlockState state, IEnergyNetworkWire wire, BlockPos pos){
        // 检查该点所有断开的连接
        Set<Direction> connections = network.getConnectionsAt(pos);
        for (Direction direction : Direction.values()){
            if (connections.contains(direction) && !wire.isConnectedTo(state, direction)){
                deleteConnectionFromNetwork(network, pos, direction);
            }
        }
        // 重新检查该点所在的电网（可能分裂）
        EnergyNetwork network1 = getNetworkAtPos(pos);
        if (network1 != null){
            // 如果该点仍在电网中，检查所有新增的连接
            network1.addWire(pos, wire);
            Set<Direction> connections1 = network1 == network ? connections : network1.getConnectionsAt(pos);
            for (Direction direction : Direction.values()){
                if (!connections1.contains(direction) && wire.isConnectedTo(state, direction)){
                    TaskQueue.runTask((t) -> addConnectionToNetwork(t, network1, pos, direction));
                }
            }
        } else {
            // 不在电网中，将其加入附近的电网
            updateNoNetworkWire(state, wire, pos);
        }
    }
    private void updateNoNetworkWire(BlockState state, IEnergyNetworkWire wire, BlockPos pos){
        // 不在电网中的电线，如果连接到电网中的电线则加入该电网
        for (Direction direction : Direction.values()){
            if (wire.isConnectedTo(state, direction)){
                BlockPos connectedPos = pos.relative(direction);
                EnergyNetwork connectedNetwork = getNetworkAtPos(connectedPos);
                if (connectedNetwork != null){
                    TaskQueue.runTask((t) -> addPosToNetwork(t, connectedNetwork, state, wire, pos));
                    break;
                }
            }
        }
    }
    private void addPosToNetwork(TaskQueue taskQueue, EnergyNetwork network, BlockState state, IEnergyNetworkWire wire, BlockPos pos){
        network.addWire(pos, wire);
        for (Direction direction : Direction.values()){
            if (wire.isConnectedTo(state, direction)) {
                taskQueue.addTask((t) -> addConnectionToNetwork(t, network, pos, direction));
            }
        }
    }
    private void addConnectionToNetwork(TaskQueue taskQueue, EnergyNetwork network, BlockPos pos, Direction direction){
        if (network.containsConnection(pos, direction)){
            return;
        }
        // 将该连接方向加入电网
        network.addConnection(pos, direction);
        BlockPos connectedPos = pos.relative(direction);
        // 如果连接到不属于本电网的其他方块
        if (!network.containsPos(connectedPos)){
            BlockState connectedState = this.world.getBlockState(connectedPos);
            Block connectedBlock = connectedState.getBlock();
            if (connectedBlock instanceof IEnergyNetworkWire){
                // 如果连接到其他电网的电线，则合并两个电网
                EnergyNetwork connectedNetwork = getNetworkAtPos(connectedPos);
                if (connectedNetwork != null){
                    combineNetworks(network, connectedNetwork);
                } else {
                    // 连接到野生电线，将其加入电网
                    taskQueue.addTask((t) -> addPosToNetwork(t, network, connectedState, (IEnergyNetworkWire) connectedBlock, connectedPos));
                }
            } else if (connectedState.hasTileEntity()){
                // 如果连接到机器，则将机器加入电网
                TileEntity tileEntity = this.world.getBlockEntity(connectedPos);
                if (tileEntity instanceof IEnergyProviderTileEntity){
                    network.addProvider(connectedPos, (IEnergyProviderTileEntity) tileEntity);
                }
                if (tileEntity instanceof IEnergyReceiverTileEntity){
                    network.addReceiver(connectedPos, (IEnergyReceiverTileEntity) tileEntity);
                }
            }
        }
    }
    private void deletePosFromNetwork(EnergyNetwork network, BlockPos pos){
        if (!network.containsPos(pos)){
            return;
        }
        Set<Direction> connectedSides = EnumSet.copyOf(network.getConnectionsAt(pos));
        // 删除该点的所有连接
        network.deleteWire(pos);
        for (Direction direction : connectedSides){
            network.deleteConnection(pos, direction, true);
        }
        if (checkNetworkValid(network) && connectedSides.size() > 1){
            // 如果这一点链接了电网中的其他两个以上的电线，则检查其连接的电线之间是否仍然相连
            BlockPos mainPos = null;
            for (Direction direction : connectedSides) {
                BlockPos newPos = pos.relative(direction);
                if (network.containsPos(newPos)) {
                    if (mainPos == null) {
                        mainPos = newPos;
                    } else {
                        mainPos = checkNetworkConnection(network, mainPos, newPos);
                    }
                }
            }
        }
    }
    private void deleteConnectionFromNetwork(EnergyNetwork network, BlockPos pos, Direction direction){
        if (!network.containsConnection(pos, direction)){
            return;
        }
        network.deleteConnection(pos, direction, true);
        if (checkNetworkValid(network)){
            checkNetworkConnection(network, pos, pos.relative(direction));
        }
    }
    // 根据两点之间是否连通检查是否需要分裂电网，返回两点中仍在电网中的位置
    private BlockPos checkNetworkConnection(EnergyNetwork network, BlockPos pos1, BlockPos pos2){
        if (!network.containsPos(pos1)) return pos2;
        if (!network.containsPos(pos2)) return pos1;
        // BFS搜索pos1和pos2的所有可达位置
        NetworkConnectionBFS bfs1 = new NetworkConnectionBFS(network, pos1);
        NetworkConnectionBFS bfs2 = new NetworkConnectionBFS(network, pos2);
        bfs1.another = bfs2;
        bfs2.another = bfs1;
        // 如果可达位置重合则仍在同一电网
        while (!bfs1.searchQueue.isEmpty() && !bfs2.searchQueue.isEmpty()){
            if (bfs1.step() || bfs2.step()){
                return pos1;
            }
        }
        // 否则根据pos1和pos2的可达域分裂成两个电网
        EnergyNetwork newNetwork = bfs1.searchQueue.isEmpty() ?
                network.splitByPoses(bfs1.arrivedPoses) :
                network.splitByPoses(bfs2.arrivedPoses);
        networks.add(newNetwork);
        checkNetworkValid(network);
        checkNetworkValid(newNetwork);
        return network.containsPos(pos1) ? pos1 : pos2;
    }

    private EnergyNetwork createNewNetwork(){
        EnergyNetwork network = new EnergyNetwork(world);
        networks.add(network);
        return network;
    }
    private void combineNetworks(EnergyNetwork network1, EnergyNetwork network2){
        if (network1 == network2){
            return;
        }
        network1.combine(network2);
        networks.remove(network2);
    }
    private boolean checkNetworkValid(EnergyNetwork network){
        if (network.hasNoMachine()){
            networks.remove(network);
            return false;
        }
        return true;
    }

    private static class NetworkConnectionBFS{
        private NetworkConnectionBFS another;
        private final EnergyNetwork network;
        private final Set<BlockPos> arrivedPoses = new HashSet<>();
        private final Queue<Pair<BlockPos, Set<Direction>>> searchQueue = new ArrayDeque<>();
        private NetworkConnectionBFS(EnergyNetwork network, BlockPos startPos){
            this.network = network;
            this.arrivedPoses.add(startPos);
            addPosToSearchQueue(startPos, null);
        }
        // 执行一步BFS搜索，如果连接到了另一侧则返回true
        private boolean step(){
            if (searchQueue.isEmpty()){
                return false;
            }
            Pair<BlockPos, Set<Direction>> pair = searchQueue.poll();
            BlockPos pos = pair.getFirst();
            arrivedPoses.add(pos);
            for (Direction direction : pair.getSecond()){
                BlockPos newPos = pos.relative(direction);
                if (network.containsPos(newPos)){
                    if (another.arrivedPoses.contains(newPos)){
                        return true;
                    } else {
                        addPosToSearchQueue(newPos, direction);
                    }
                }
            }
            return false;
        }
        private void addPosToSearchQueue(BlockPos pos, @Nullable Direction sideFrom){
            Set<Direction> dirSet = EnumSet.noneOf(Direction.class);
            dirSet.addAll(network.getConnectionsAt(pos));
            if (sideFrom != null){
                dirSet.remove(sideFrom);
            }
            searchQueue.offer(Pair.of(pos, dirSet));
        }
    }
}
