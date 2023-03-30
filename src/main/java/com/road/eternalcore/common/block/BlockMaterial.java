package com.road.eternalcore.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;

public class BlockMaterial {
    public static final Material Machine = material(
            MaterialColor.METAL,
            false,
            true,
            true,
            true,
            false,
            false,
            PushReaction.BLOCK
    );

    private static Material material(MaterialColor color, boolean liquid, boolean solid, boolean blocksMotion, boolean solidBlocking, boolean flammable, boolean replaceable, PushReaction pushReaction) {
        return new Material(color, liquid, solid, blocksMotion, solidBlocking, flammable, replaceable, pushReaction);
    }
}
