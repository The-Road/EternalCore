package com.road.eternalcore.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;

public class BlockMaterial {
    // 记录方块的属性
    public static final Material MACHINE = material(
            MaterialColor.METAL,
            false,
            true,
            true,
            true,
            false,
            false,
            PushReaction.BLOCK
    );
    public static final Material PIPE = material(
            MaterialColor.NONE,
            false,
            true,
            true,
            false,
            false,
            false,
            PushReaction.BLOCK
    );

    private static Material material(MaterialColor color, boolean liquid, boolean solid, boolean blocksMotion, boolean solidBlocking, boolean flammable, boolean replaceable, PushReaction pushReaction) {
        return new Material(color, liquid, solid, blocksMotion, solidBlocking, flammable, replaceable, pushReaction);
    }
}
