package com.nhat.biotech.view.io_hatches.fluid;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.view.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class FluidHatchScreen<T extends FluidHatchMenu> extends AbstractContainerScreen<T> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Biotech.MODID, "textures/gui/fluid_hatch.png");
    private FluidTankRenderer renderer;
    @Override
    protected void init() {
        super.init();
        renderer = new FluidTankRenderer(16000, true, 16, 52);
    }
    public FluidHatchScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        renderer.render(graphics.pose() , leftPos + 62, topPos + 17, menu.getFluidStack());
    }
}
