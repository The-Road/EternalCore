package com.road.eternalcore.common.block.pipe;

import com.road.eternalcore.Utils;
import com.road.eternalcore.api.block.properties.PipeConnection;
import com.road.eternalcore.api.energy.CapEnergy;
import com.road.eternalcore.api.energy.eu.EUTier;
import com.road.eternalcore.api.energy.network.IEnergyNetworkWire;
import com.road.eternalcore.api.material.MaterialWireData;
import com.road.eternalcore.common.item.tool.ModToolType;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;

public class WireBlock extends AbstractPipeBlock implements IEnergyNetworkWire {
    // 电线半径：1x=2, 2x=3, 4x=4, 8x=6, 12x=7, 16x=8

    private final String wireName;
    protected final MaterialWireData wireData;
    protected final MaterialWireData.WireType wireType;
    private final int maxCurrent;
    private final int lineLoss;
    public WireBlock(MaterialWireData wireData, MaterialWireData.WireType wireType) {
        super(1.0F, 1.0F, ModToolType.WIRE_CUTTER, wireType.radius);
        this.wireName = Utils.BlockDescriptionId(wireType.name);
        this.wireData = wireData;
        this.wireType = wireType;
        this.maxCurrent = wireData.getMaxCurrent() * wireType.currentRate;
        this.lineLoss = wireData.getLineLoss() * wireType.lossRate;
    }
    public boolean isConnectedTo(BlockState blockState, Direction direction) {
        EnumProperty<PipeConnection> property = DIRECTION_CONNECTION.get(direction);
        return blockState.hasProperty(property) && blockState.getValue(property).isConnected();
    }
    public EUTier getTier(){
        return wireData.getEuTier();
    }
    public int getMaxCurrent(){
        return maxCurrent;
    }
    public int getLineLoss(){
        return lineLoss;
    }
    protected boolean canConnectTo(BlockState blockState, Direction direction, BlockState facingState, IWorld world, BlockPos blockPos, BlockPos facingPos) {
        if (blockState.getBlock() instanceof WireBlock){
            PipeConnection connection = blockState.getValue(DIRECTION_CONNECTION.get(direction));
            if (connection == PipeConnection.CUT){
                return false;
            }
            if (facingState.getBlock() instanceof WireBlock){
                PipeConnection facingConnection = facingState.getValue(DIRECTION_CONNECTION.get(direction.getOpposite()));
                return facingConnection != PipeConnection.CUT;
            } else if (facingState.hasTileEntity()){
                TileEntity tileEntity = world.getBlockEntity(facingPos);
                return tileEntity.getCapability(CapEnergy.EU, direction.getOpposite()).isPresent();
            }
        }
        return false;
    }

    public IFormattableTextComponent customBlockName(){
        return new TranslationTextComponent(wireName, wireData.getMaterial().getText());
    }
}
