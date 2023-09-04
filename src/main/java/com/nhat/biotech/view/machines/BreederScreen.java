package com.nhat.biotech.view.machines;

import com.nhat.biotech.Biotech;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BreederScreen extends AbstractContainerScreen<BreederMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Biotech.MODID, "textures/gui/breeder.png");

    public BreederScreen(BreederMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.isOperating()) {
            graphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 34, 176, 0, this.menu.getProgress() + 1, 16);
        }
    }
}
