package com.road.eternalcore.common.item.tool;

import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.common.block.machine.MachineBlock;
import com.road.eternalcore.common.item.group.ModGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class DebugToolItem extends Item {
    // 调试用工具，可以输出各种方块的信息
    public DebugToolItem(){
        super(new Properties().tab(ModGroup.toolGroup));
    }


    public ActionResultType useOn(ItemUseContext itemUseContext) {
        World world = itemUseContext.getLevel();
        if (world.isClientSide()){
            return ActionResultType.SUCCESS;
        }
        PlayerEntity player = itemUseContext.getPlayer();
        BlockPos blockpos = itemUseContext.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        Block block = blockstate.getBlock();
        msg(player, "blockTitle");
        msg(player, "blockName", block.getName());
        if (block instanceof MachineBlock) {
            MaterialBlockData blockData = ((MachineBlock) block).getMaterialBlockData(blockstate, world, blockpos);
            // 获取机器的材料、硬度和爆炸抗性
            msg(player, "blockMaterial", blockData.getMaterial().getText());
            msg(player, "blockDestroyTime", blockData.getHullData().getDestroyTime());
            msg(player, "blockExplosionResistance", blockData.getHullData().getExplosionResistance());
            return ActionResultType.SUCCESS;
        }
        // 普通方块返回硬度和爆炸抗性
        msg(player, "blockDestroyTime", blockstate.getDestroySpeed(world, blockpos));
        msg(player, "blockExplosionResistance", block.getExplosionResistance());
        return ActionResultType.SUCCESS;
    }
    private void msgT(PlayerEntity player, String text){
        player.sendMessage(new StringTextComponent(text), Util.NIL_UUID);
    }
    private void msg(PlayerEntity player, String key){
        player.sendMessage(
                new TranslationTextComponent("eternalcore.debugTool." + key),
                Util.NIL_UUID
        );
    }
    private void msg(PlayerEntity player, String key, Object... args){
        player.sendMessage(
                new TranslationTextComponent("eternalcore.debugTool." + key, args),
                Util.NIL_UUID
        );
    }
}
