package com.road.eternalcore.common.item.tool;

import com.road.eternalcore.Utils;
import com.road.eternalcore.api.energy.network.IEnergyNetworkWire;
import com.road.eternalcore.api.material.MaterialBlockData;
import com.road.eternalcore.common.block.machine.MachineBlock;
import com.road.eternalcore.common.item.ModItem;
import com.road.eternalcore.common.item.group.ModGroup;
import com.road.eternalcore.common.tileentity.EnergyMachineTileEntity;
import com.road.eternalcore.common.world.pipenetwork.EnergyNetwork;
import com.road.eternalcore.common.world.pipenetwork.EnergyNetworkManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.text.DecimalFormat;

public class DebugToolItem extends ModItem {
    // 调试用工具，可以输出各种方块的信息
    public DebugToolItem(){
        super(new Properties().tab(ModGroup.toolGroup));
    }

    // 对空气使用
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (world.isClientSide()){
            return ActionResult.pass(itemStack);
        }
        // 输出

        return ActionResult.pass(itemStack);
    }

    // 对方块使用
    public ActionResultType useOn(ItemUseContext itemUseContext) {
        World world = itemUseContext.getLevel();
        if (world.isClientSide()){
            return ActionResultType.SUCCESS;
        }
        PlayerEntity player = itemUseContext.getPlayer();
        BlockPos blockpos = itemUseContext.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        Block block = blockstate.getBlock();
        DebugToolMsgHelper msgHelper = new DebugToolMsgHelper(player, "block");
        msgHelper.msg("title");
        msgHelper.msg("name", block.getName());
        if (block instanceof MachineBlock) {
            MaterialBlockData blockData = ((MachineBlock) block).getMaterialBlockData(blockstate, world, blockpos);
            // 获取机器的材料、硬度和爆炸抗性
            msgHelper.msg("material", blockData.getMaterial().getText());
            msgHelper.msg("destroyTime", blockData.getHullData().getDestroyTime());
            msgHelper.msg("explosionResistance", blockData.getHullData().getExplosionResistance());
        } else {
            // 普通方块返回硬度和爆炸抗性
            msgHelper.msg("destroyTime", blockstate.getDestroySpeed(world, blockpos));
            msgHelper.msg("explosionResistance", block.getExplosionResistance());
        }
        TileEntity tileEntity = world.getBlockEntity(blockpos);
        if (tileEntity != null){
            DebugToolMsgHelper teMsgHelper = new DebugToolMsgHelper(player, "tileEntity");
            teMsgHelper.msg("title");
            if (tileEntity instanceof EnergyMachineTileEntity){
                EnergyMachineTileEntity energyMachineTileEntity = (EnergyMachineTileEntity) tileEntity;
                teMsgHelper.msg("euTier", energyMachineTileEntity.getTier().getText());
                teMsgHelper.msg("euStorage", energyMachineTileEntity.getEnergyStored());
                teMsgHelper.msg("euMaxStorage", energyMachineTileEntity.getMaxEnergyStored());
            }
        }
        EnergyNetwork energyNetwork = EnergyNetworkManager.get(world).getNetworkAtPos(blockpos);
        if (energyNetwork != null){
            DebugToolMsgHelper euMsgHelper = new DebugToolMsgHelper(player, "energy");
            euMsgHelper.msg("title");
            euMsgHelper.msg("network", energyNetwork);
            IEnergyNetworkWire wire = energyNetwork.getNode(blockpos);
            euMsgHelper.msg("euTier", wire.getTier().getText());
            euMsgHelper.msg("maxCurrent", wire.getMaxCurrent());
            euMsgHelper.msg("lineLoss", wire.getLineLoss());
            euMsgHelper.msg("wireLoad", energyNetwork.getWireLoadAt(blockpos));
        }
        return ActionResultType.SUCCESS;
    }

    // 对实体使用
    public ActionResultType interactLivingEntity(ItemStack itemStack, PlayerEntity player, LivingEntity livingEntity, Hand hand) {
        if (!(player instanceof ServerPlayerEntity)){
            return ActionResultType.PASS;
        }
        DebugToolMsgHelper msgHelper = new DebugToolMsgHelper(player, "livingEntity");
        DecimalFormat doubleFormat = new DecimalFormat("#.####");
        msgHelper.msg("title");
        // 获取实体属性
        msgHelper.msg("attributes");
        ListNBT attributesNBT = livingEntity.getAttributes().save();
        for (int i=0; i<attributesNBT.size(); i++){
            CompoundNBT attribute = (CompoundNBT) attributesNBT.get(i);
            msgHelper.msg("attribute",
                    new TranslationTextComponent("attribute.name." + new ResourceLocation(attribute.getString("Name")).getPath()),
                    doubleFormat.format(attribute.getDouble("Base"))
            );
            if (attribute.contains("Modifiers")){
                ListNBT modifiers = attribute.getList("Modifiers", 10);
                for (int j=0; j<modifiers.size(); j++) {
                    CompoundNBT modifier = (CompoundNBT) modifiers.get(j);
                    msgHelper.msg("attribute.modifier",
                            modifier.getString("Name"),
                            new TranslationTextComponent(String.format("attribute.modifier.operation.%d", modifier.getInt("Operation"))),
                            doubleFormat.format(modifier.getDouble("Amount"))
                    );
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    // 用于在聊天栏中发送消息
    protected static class DebugToolMsgHelper{
        private final PlayerEntity player;
        private final String keyHead;
        protected DebugToolMsgHelper(PlayerEntity player, String keyHead) {
            this.player = player;
            this.keyHead = keyHead;
        }
        private String getTextComponentKey(String key){
            return Utils.MOD_ID + ".debugTool." + keyHead + "." + key;
        }
        protected void msgT(String text){
            player.sendMessage(new StringTextComponent(text), Util.NIL_UUID);
        }
        protected void msg(String key){
            player.sendMessage(
                    new TranslationTextComponent(getTextComponentKey(key)),
                    Util.NIL_UUID
            );
        }
        protected void msg(String key, Object... args){
            player.sendMessage(
                    new TranslationTextComponent(getTextComponentKey(key), args),
                    Util.NIL_UUID
            );
        }
    }
}
