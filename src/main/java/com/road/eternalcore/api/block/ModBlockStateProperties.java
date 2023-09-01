package com.road.eternalcore.api.block;

import com.road.eternalcore.api.block.properties.PipeConnection;
import net.minecraft.state.EnumProperty;

public class ModBlockStateProperties {
    public static final MaterialBlockProperty MATERIAL = MaterialBlockProperty.create();
    public static final EnumProperty<PipeConnection> UP_PIPE = EnumProperty.create("up", PipeConnection.class);
    public static final EnumProperty<PipeConnection> DOWN_PIPE = EnumProperty.create("down", PipeConnection.class);
    public static final EnumProperty<PipeConnection> NORTH_PIPE = EnumProperty.create("north", PipeConnection.class);
    public static final EnumProperty<PipeConnection> EAST_PIPE = EnumProperty.create("east", PipeConnection.class);
    public static final EnumProperty<PipeConnection> SOUTH_PIPE = EnumProperty.create("south", PipeConnection.class);
    public static final EnumProperty<PipeConnection> WEST_PIPE = EnumProperty.create("west", PipeConnection.class);

}
