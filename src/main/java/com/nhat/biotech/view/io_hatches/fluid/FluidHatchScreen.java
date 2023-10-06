package com.nhat.biotech.view.io_hatches.fluid;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.view.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.Optional;

public abstract class FluidHatchScreen<T extends FluidHatchMenu> extends AbstractContainerScreen<T> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Biotech.MODID, "textures/gui/fluid_hatch.png");
    private FluidTankRenderer renderer;
    public FluidHatchScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }
    @Override
    protected void init() {
        super.init();
        renderer = new FluidTankRenderer(32000, true, 16, 52);
    }
    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
        if (isHovering(62, 17, 16, 52, pMouseX, pMouseY)) {
            pGuiGraphics.renderTooltip(font, renderer.getTooltip(menu.getFluidStack(), TooltipFlag.NORMAL), Optional.empty(), pMouseX - leftPos, pMouseY - topPos);
        }
        if (menu.getCarried().isEmpty() && hoveredSlot != null && hoveredSlot.hasItem()) {
            ItemStack itemstack = hoveredSlot.getItem();
            pGuiGraphics.renderTooltip(font, getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, pMouseX - leftPos, pMouseY - topPos);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        renderBackground(graphics);
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        renderer.render(graphics.pose() , leftPos + 62, topPos + 17, menu.getFluidStack());
    }
}
