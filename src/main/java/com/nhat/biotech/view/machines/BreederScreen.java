package com.nhat.biotech.view.machines;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.view.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;

public class BreederScreen extends AbstractContainerScreen<BreederMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Biotech.MODID, "textures/gui/breeder.png");
    private FluidTankRenderer renderer;
    public BreederScreen(BreederMenu menu, Inventory inventory, Component component) {
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

        if (isHovering(8, 17, 16, 52, pMouseX, pMouseY)) {
            if (menu.getStructureValid())
                pGuiGraphics.renderTooltip(font, List.of(Component.literal(menu.getStoredEnergy() + " / " + 128000 + " FE")), Optional.empty(), pMouseX - leftPos, pMouseY - topPos);
            else
                pGuiGraphics.renderTooltip(font, List.of(Component.literal("Invalid Structure")), Optional.empty(), pMouseX - leftPos, pMouseY - topPos);
        }
        if (isHovering(152, 17, 16, 52, pMouseX, pMouseY)) {
            if (menu.getStructureValid())
                pGuiGraphics.renderTooltip(font, renderer.getTooltip(menu.getStoredFluid(), TooltipFlag.NORMAL), Optional.empty(), pMouseX - leftPos, pMouseY - topPos);
            else
                pGuiGraphics.renderTooltip(font, List.of(Component.literal("Invalid Structure")), Optional.empty(), pMouseX - leftPos, pMouseY - topPos);
        }

        if (menu.getCarried().isEmpty() && hoveredSlot != null && hoveredSlot.hasItem()) {
            ItemStack itemstack = hoveredSlot.getItem();
            pGuiGraphics.renderTooltip(font, getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, pMouseX - leftPos, pMouseY - topPos);
        }
    }
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        renderBackground(graphics);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (menu.getStructureValid())
        {
            if (menu.getStoredEnergy() > 0) {
                graphics.blit(TEXTURE, leftPos + 8, topPos + 69 - menu.getEnergyHeight(), 176, 69 - menu.getEnergyHeight(), 16, menu.getEnergyHeight());
            }
            if (!menu.getStoredFluid().isEmpty())
            {
                renderer.render(graphics.pose() , leftPos + 152, topPos + 17, menu.getStoredFluid());
            }
            if (this.menu.isOperating()) {
                graphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 34, 176, 0, this.menu.getProgressWidth() + 1, 16);
            }
        }
    }
}
