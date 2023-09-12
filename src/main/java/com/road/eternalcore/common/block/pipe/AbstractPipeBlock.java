package com.road.eternalcore.common.block.pipe;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.road.eternalcore.api.block.properties.PipeConnection;
import com.road.eternalcore.common.block.BlockMaterial;
import com.road.eternalcore.common.world.energy.EnergyNetworkManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.HashMap;
import java.util.Map;

import static com.road.eternalcore.api.block.ModBlockStateProperties.*;

public abstract class AbstractPipeBlock extends Block {
    // 管道类方块，包括管道和电线
    // 每个方向对应的property
    public static final Map<Direction, EnumProperty<PipeConnection>> DIRECTION_CONNECTION = makeDirectionPipeConnection();
    // 分别对应pipeRadius和pipeStateId
    private static final Table<Integer, Integer, VoxelShape> SHAPES = makeShapes();
    protected final int pipeRadius; //管道半径1-8
    public AbstractPipeBlock(float destroyTime, float explosionResistance, ToolType harvestTool, int pipeRadius) {
        super(Properties.of(BlockMaterial.PIPE)
                .requiresCorrectToolForDrops().harvestTool(harvestTool)
                .strength(destroyTime, explosionResistance)
                .sound(SoundType.METAL)
        );
        if (pipeRadius < 1) pipeRadius = 1;
        if (pipeRadius > 8) pipeRadius = 8;
        this.pipeRadius = pipeRadius;
    }

    private static Map<Direction, EnumProperty<PipeConnection>> makeDirectionPipeConnection(){
        Map<Direction, EnumProperty<PipeConnection>> map = new HashMap<>();
        map.put(Direction.UP, UP_PIPE);
        map.put(Direction.DOWN, DOWN_PIPE);
        map.put(Direction.NORTH, NORTH_PIPE);
        map.put(Direction.EAST, EAST_PIPE);
        map.put(Direction.SOUTH, SOUTH_PIPE);
        map.put(Direction.WEST, WEST_PIPE);
        return map;
    }
    private static Table<Integer, Integer, VoxelShape> makeShapes(){
        Table<Integer, Integer, VoxelShape> shapes = HashBasedTable.create(8, 64);
        for (int radius = 1; radius < 8 ; radius++){
            double small = 8.0 - radius;
            double big = 8.0 + radius;
            // 北z-，东x+，南z+，西x-
            VoxelShape centerPart = Block.box(small, small, small, big, big, big);
            Map<Direction, VoxelShape> directParts = new HashMap<>();
            directParts.put(Direction.UP, Block.box(small, big, small, big, 16, big));
            directParts.put(Direction.DOWN, Block.box(small, 0, small, big, small, big));
            directParts.put(Direction.NORTH, Block.box(small, small, 0, big, big, small));
            directParts.put(Direction.EAST, Block.box(big, small, small, 16, big, big));
            directParts.put(Direction.SOUTH, Block.box(small, small, big, big, big, 16));
            directParts.put(Direction.WEST, Block.box(0, small, small, small, big, big));
            for (int pipeState = 0; pipeState < 64; pipeState++){
                VoxelShape shape = VoxelShapes.empty();
                shape = VoxelShapes.or(shape, centerPart);
                for (Direction direction : Direction.values()) {
                    int directionId = 1 << direction.get3DDataValue();
                    if ((directionId & pipeState) != 0){
                        shape = VoxelShapes.or(shape, directParts.get(direction));
                    }
                }
                shapes.put(radius, pipeState, shape);
            }
        }
        // 半径等于8时直接返回一整格
        for (int pipeState = 0; pipeState < 64; pipeState++){
            shapes.put(8, pipeState, VoxelShapes.block());
        }
        return shapes;
    }
    // 禁止生物生成
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, EntityType<?> entityType) {
        return false;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        DIRECTION_CONNECTION.values().forEach(builder::add);
    }
    // 六个方向是否联通对应64种状态
    protected int getPipeStateId(BlockState state){
        int id = 0;
        for (Direction direction : Direction.values()) {
            EnumProperty<PipeConnection> property = DIRECTION_CONNECTION.get(direction);
            if (state.hasProperty(property) && state.getValue(property).isConnected()){
                id += 1 << direction.get3DDataValue();
            }
        }
        return id;
    }

    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selectionContext) {
        return SHAPES.get(pipeRadius, getPipeStateId(state));
    }

    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        BlockState placeState = defaultBlockState();
        World world = useContext.getLevel();
        BlockPos pos = useContext.getClickedPos();
        for (Direction direction : Direction.values()){
            BlockPos facingPos = pos.relative(direction);
            BlockState facingState = world.getBlockState(facingPos);
            if (canConnectTo(placeState, direction, facingState, world, pos, facingPos)){
                placeState = placeState.setValue(DIRECTION_CONNECTION.get(direction), PipeConnection.ON);
            }
        }
        return placeState;
    }
    public void onPlace(BlockState thisState, World world, BlockPos pos, BlockState lastState, boolean blockUpdate) {
        if (!lastState.is(thisState.getBlock())) {
            EnergyNetworkManager.updateWirePos(world, thisState, pos, true);
        }
        super.onPlace(thisState, world, pos, lastState, blockUpdate);
    }
    public void onRemove(BlockState thisState, World world, BlockPos pos, BlockState newState, boolean blockUpdate) {
        if (!thisState.is(newState.getBlock())) {
            EnergyNetworkManager.updateWirePos(world, thisState, pos, true);
        }
        super.onRemove(thisState, world, pos, newState, blockUpdate);
    }
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState facingState, IWorld world, BlockPos blockPos, BlockPos facingPos) {
        EnumProperty<PipeConnection> property = DIRECTION_CONNECTION.get(direction);
        if (canConnectTo(blockState, direction, facingState, world, blockPos, facingPos)){
            if (world instanceof ServerWorld) {
                EnergyNetworkManager.updateWirePos((World) world, blockState, blockPos, false);
            }
            return blockState.setValue(property, PipeConnection.ON);
        } else if (blockState.getValue(property).isConnected()){
            if (world instanceof ServerWorld) {
                EnergyNetworkManager.updateWirePos((World) world, blockState, blockPos, false);
            }
            return blockState.setValue(property, PipeConnection.OFF);
        }
        return super.updateShape(blockState, direction, facingState, world, blockPos, facingPos);
    }

    abstract protected boolean canConnectTo(BlockState blockState, Direction direction, BlockState facingState, IWorld world, BlockPos blockPos, BlockPos facingPos);
}
