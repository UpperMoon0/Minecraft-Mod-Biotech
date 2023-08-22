package com.nhat.biotech.View;

import com.nhat.biotech.Biotech;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BreederScreen extends AbstractContainerScreen<BreederMenu> {
    private static final ResourceLocation background = new ResourceLocation(Biotech.MODID, "textures/gui/breeder.png");

    public BreederScreen(BreederMenu breederMenu, Inventory inventory, Component component) {
        super(breederMenu, inventory, component);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(background, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
