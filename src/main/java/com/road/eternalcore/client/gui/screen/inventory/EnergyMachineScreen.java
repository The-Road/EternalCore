package com.road.eternalcore.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.road.eternalcore.api.energy.EnergyUtils;
import com.road.eternalcore.api.energy.eu.EUTier;
import com.road.eternalcore.common.inventory.container.machine.MachineContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class EnergyMachineScreen<T extends MachineContainer> extends ContainerScreen<T> {
    // 电量条在GUI中的位置
    protected Rectangle2d energyBarRect = new Rectangle2d(78, 91, 20, 4);
    // 电量条在材质贴图中的位置（只用到了左上角坐标，W和H用的是energyBarRect的）
    protected Rectangle2d energyBarTextureRect = new Rectangle2d(176, 0, 20, 4);
    public EnergyMachineScreen(T machineContainer, PlayerInventory inventory, ITextComponent title) {
        super(machineContainer, inventory, title);
    }

    abstract protected ResourceLocation guiLocation();

    public void init(){
        super.init();
        this.imageHeight = 184;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);
    }

    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(guiLocation());
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        renderEnergyBar(matrixStack, mouseX, mouseY);
    }
    protected void renderEnergyBar(MatrixStack matrixStack, int mouseX, int mouseY){
        // 根据电量计算电量条长度
        int energy = this.menu.getEnergy();
        int maxEnergy = this.menu.getMaxEnergy();
        double energyRate = maxEnergy == 0 ? 0 : Math.min(1.0 * energy / maxEnergy, 1);
        int energyBarLength = (int) (energyRate * energyBarRect.getWidth());
        if (energy > 0 && energyBarLength == 0) energyBarLength = 1;
        // 获取左上角坐标
        int x = this.leftPos + energyBarRect.getX();
        int y = this.topPos + energyBarRect.getY();
        blit(matrixStack, x, y, energyBarTextureRect.getX(), energyBarTextureRect.getY(), energyBarLength, energyBarRect.getHeight());
        // 移到电量槽上时显示当前机器储电量
        if (this.minecraft.player.inventory.getCarried().isEmpty() && energyBarRect.contains(mouseX - this.leftPos, mouseY - this.topPos)){
            renderTooltip(matrixStack, EnergyUtils.energyStorageText(EUTier.tier(this.menu.getTierLevel()), this.menu.getEnergy(), this.menu.getMaxEnergy(), false), mouseX, mouseY);
        }
    }
}
