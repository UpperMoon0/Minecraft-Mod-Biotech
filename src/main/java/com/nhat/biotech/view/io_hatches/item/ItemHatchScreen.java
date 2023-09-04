package com.nhat.biotech.view.io_hatches.item;

import com.nhat.biotech.Biotech;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class ItemHatchScreen<T extends ItemHatchMenu> extends AbstractContainerScreen<T> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Biotech.MODID, "textures/gui/item_hatch.png");

    public ItemHatchScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
