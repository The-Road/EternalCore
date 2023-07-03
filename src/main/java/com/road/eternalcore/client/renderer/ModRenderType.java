package com.road.eternalcore.client.renderer;

import com.road.eternalcore.common.block.machine.MachineBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

import java.util.function.Predicate;

public class ModRenderType {
    protected static Predicate<RenderType> machineRenderTypes = type -> type == RenderType.solid() || type == RenderType.cutoutMipped();
    public static void registerBlockRenderType(){
        for (Block machine : MachineBlocks.getAll()){
            RenderTypeLookup.setRenderLayer(machine, machineRenderTypes);
        }
    }
}
